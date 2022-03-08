package cn.yue.base.middle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.yue.base.middle.view.load.PageStatus;
import cn.yue.base.middle.view.refresh.IRefreshLayout;

/**
 * Description :
 * Created by yue on 2022/3/7
 */

public class PageStateView extends FrameLayout {

    private View contentView;
    private final PageHintView pageHintView = new PageHintView(getContext(), null);

    public PageStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(pageHintView, params);
    }

    public void setOnReloadListener(PageHintView.OnReloadListener onReloadListener) {
        pageHintView.setOnReloadListener(onReloadListener);
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setContentViewById(@LayoutRes int layoutId) {
        this.contentView = View.inflate(getContext(), layoutId, null);
    }

    public void setNoNetView(View noNetView) {
        pageHintView.setNoNetView(noNetView);
    }

    public View setNoNetViewById(@LayoutRes int layoutId) {
        return pageHintView.setNoNetViewById(layoutId);
    }

    public void setNoDataView(View noDataView) {
        pageHintView.setNoDataView(noDataView);
    }

    public View setNoDataViewById(@LayoutRes int layoutId) {
        return pageHintView.setNoDataViewById(layoutId);
    }

    public void setLoadingView(View loadingView) {
        pageHintView.setLoadingView(loadingView);
    }

    public View setLoadingViewById(@LayoutRes int layoutId) {
        return pageHintView.setLoadingViewById(layoutId);
    }

    public void show(PageStatus status) {
        switch (status) {
            case NORMAL:
                showSuccess();
                break;
            case LOADING:
                showLoading();
                break;
            case NO_NET:
                showErrorNet();
                break;
            case NO_DATA:
                showErrorNoData();
                break;
            case ERROR:
                showErrorOperation();
                break;
        }
    }

    public void showLoading() {
        pageHintView.showLoading();
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    public void showSuccess() {
        pageHintView.showSuccess();
        if (contentView != null) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    public void showErrorNet() {
        pageHintView.showErrorNet();
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    public void showErrorNoData() {
        pageHintView.showErrorNoData();
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    public void showErrorOperation() {
        pageHintView.showErrorOperation();
        if (contentView != null) {
            contentView.setVisibility(View.GONE);
        }
    }

    public void setRefreshTarget(IRefreshLayout refreshLayout) {
        pageHintView.setRefreshTarget(refreshLayout);
    }

}
