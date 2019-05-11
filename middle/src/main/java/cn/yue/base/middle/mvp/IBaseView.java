package cn.yue.base.middle.mvp;

import com.trello.rxlifecycle2.android.FragmentEvent;

import cn.yue.base.common.activity.ILifecycleProvider;

/**
 * Description :
 * Created by yue on 2019/3/13
 */
public interface IBaseView extends IStatusView, IWaitView, ILifecycleProvider<FragmentEvent> {
}
