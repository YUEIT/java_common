package cn.yue.base.middle.mvvm.components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.utils.debug.ToastUtils;
import cn.yue.base.common.utils.device.NetworkUtils;
import cn.yue.base.common.widget.dialog.WaitDialog;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.IWaitView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.mvp.photo.IPhotoView;
import cn.yue.base.middle.mvp.photo.PhotoHelper;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.view.PageHintView;

public abstract class BaseHintVMFragment<T extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragment implements IStatusView, IWaitView, IPhotoView {

    protected T binding;
    protected VM viewModel;
    private boolean isFirstLoading = true;

    protected PageHintView hintView;
    private ViewStub baseVS;
    private PhotoHelper photoHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_hint;
    }

    public abstract int getVariableId();

    protected abstract int getContentLayoutId();

    protected VM getViewModel() {
        return null;
    }

    public VM createViewModel(Class<VM> cls) {
        return new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(mActivity.getApplication())).get(cls);
    }

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
        baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                binding = DataBindingUtil.bind(inflated);
            }
        });
        baseVS.inflate();
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
        binding.setVariable(getVariableId(), viewModel);
        getLifecycle().addObserver(viewModel);
    }

    @Override
    protected void initOther() {
        super.initOther();
        if (NetworkUtils.isConnected()) {
            viewModel.status.postValue(PageStatus.STATUS_NORMAL);
        } else {
            showStatusView(PageStatus.STATUS_ERROR_NET);
        }
    }

    @Override
    public void showStatusView(PageStatus status) {
        switch (status) {
            case STATUS_NORMAL:
            case STATUS_SUCCESS:
            case STATUS_END:
                showPageHintSuccess();
                break;
            case STATUS_LOADING_REFRESH:
                showPageHintLoading();
                break;
            case STATUS_ERROR_NET:
                showPageHintErrorNet();
                break;
            case STATUS_ERROR_NO_DATA:
                showPageHintErrorNoData();
                break;
            case STATUS_ERROR_OPERATION:
                showPageHintErrorOperation();
                break;
            case STATUS_ERROR_SERVER:
                showPageHintErrorServer();
                break;
        }
    }

    private void showPageHintLoading() {
        if (hintView != null) {
            hintView.showLoading();
        }
    }

    private void showPageHintSuccess() {
        if (hintView != null) {
            hintView.showSuccess();
        }
        isFirstLoading = false;
    }

    private void showPageHintErrorNet() {
        if (hintView != null) {
            if (isFirstLoading) {
                hintView.showErrorNet();
            } else {
                ToastUtils.showShortToast("网络不给力，请检查您的网络设置~");
            }
        }
    }

    private void showPageHintErrorNoData() {
        if (hintView != null) {
            hintView.showErrorNoData();
        }
    }

    private void showPageHintErrorOperation() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
        }
    }

    private void showPageHintErrorServer() {
        if (hintView != null && isFirstLoading) {
            hintView.showErrorOperation();
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

    public PhotoHelper getPhotoHelper() {
        if (photoHelper == null) {
            photoHelper = new PhotoHelper(mActivity, this);
        }
        return photoHelper;
    }

    @Override
    public void selectImageResult(List<String> selectList) {

    }

    @Override
    public void cropImageResult(String image) {

    }

    @Override
    public void uploadImageResult(List<String> serverList) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (photoHelper != null) {
            photoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(viewModel);
    }
}
