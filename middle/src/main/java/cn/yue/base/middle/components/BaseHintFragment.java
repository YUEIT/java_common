package cn.yue.base.middle.components;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.PageStatus;
import cn.yue.base.middle.view.PageHintView;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public abstract class BaseHintFragment extends BaseFragment implements IStatusView {

    protected PageStatus status = PageStatus.STATUS_NORMAL;
    private PageHintView hintView;
    private ViewStub baseVS;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_pull;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        hintView = findViewById(R.id.hintView);
        baseVS = findViewById(R.id.baseVS);
        baseVS.setLayoutResource(getContentLayoutId());
        baseVS.inflate();
    }

    protected abstract int getContentLayoutId();

    @Override
    public void showStatusView(PageStatus status) {
        this.status = status;
        switch (status) {
            case STATUS_SUCCESS:
                showPageHintSuccess();
                break;
            case STATUS_END:
                showPageHintSuccess();
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
        }
    }

    protected void showPageHintSuccess() {
        hintView.setVisibility(View.GONE);
    }

    protected void showPageHintErrorNet() {
        hintView.setVisibility(View.VISIBLE);
        hintView.setHintText("网络异常~");
    }

    protected void showPageHintErrorNoData() {
        hintView.setVisibility(View.VISIBLE);
        hintView.setHintText("无数据~");
    }

    protected void showPageHintErrorOperation() {
        hintView.setVisibility(View.VISIBLE);
        hintView.setHintText("未知异常~");
    }
}
