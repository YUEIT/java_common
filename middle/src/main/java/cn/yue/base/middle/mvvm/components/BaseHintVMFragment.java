package cn.yue.base.middle.mvvm.components;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.components.load.PageStatus;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.view.PageHintView;

public abstract class BaseHintVMFragment<VM extends BaseViewModel> extends BaseFragment implements IStatusView, IWaitView {

    protected VM viewModel;
    protected PageHintView hintView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        viewModel = getViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(modelClass);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ILifecycleProvider<Lifecycle.Event> initLifecycleProvider() {
        return viewModel;
    }

    protected VM getViewModel() {
        return null;
    }

    public VM createViewModel(Class<VM> cls) {
        return new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication())).get(cls);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_hint;
    }

    protected abstract int getContentLayoutId();

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        hintView.setOnReloadListener(new PageHintView.OnReloadListener() {
            @Override
            public void onReload() {
                if (NetworkUtils.isConnected()) {
                    mActivity.recreateFragment(BaseHintVMFragment.this.getClass().getName());
                } else {
                    ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
                }
            }
        });
        ViewStub baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                bindLayout(inflated);
            }
        });
        baseVS.inflate();
    }

    protected void bindLayout(View inflated) { }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            viewModel.loader.setPageStatus(PageStatus.NORMAL);
        } else {
            viewModel.loader.setPageStatus(PageStatus.NO_NET);
        }
        viewModel.showWait.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (TextUtils.isEmpty(s)) {
                    dismissWaitDialog();
                } else {
                    showWaitDialog(s);
                }
            }
        });
        viewModel.loader.observePage(this, new Observer<PageStatus>() {
            @Override
            public void onChanged(PageStatus pageStatus) {
                showStatusView(pageStatus);
            }
        });
    }

    @Override
    public void showStatusView(PageStatus status) {
        if (hintView != null && viewModel.loader.isFirstLoad()) {
            hintView.show(status);
        }
        if (status == PageStatus.NORMAL) {
            viewModel.loader.setFirstLoad(false);
        }
    }

    private WaitDialog waitDialog;
    @Override
    public void showWaitDialog(String title) {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity);
        }
        waitDialog.show(title, true, null);
    }

    @Override
    public void dismissWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(viewModel);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (viewModel != null) {
            viewModel.onActivityResult(requestCode, resultCode, data);
        }
    }
}
