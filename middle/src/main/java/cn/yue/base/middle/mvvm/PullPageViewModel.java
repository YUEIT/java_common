package cn.yue.base.middle.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;

import cn.yue.base.middle.net.wrapper.BaseListBean;

public abstract class PullPageViewModel<S> extends PullListViewModel<BaseListBean<S>, S> {

    public PullPageViewModel(@NonNull Application application) {
        super(application);
    }
}