package cn.yue.base.middle.mvvm.components;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.mvvm.PageViewModel;

public class BasePageVMFragment<S, VM extends PageViewModel<S>> extends BaseListVMFragment<VM> {

    @Override
    public CommonAdapter<S> getAdapter() {
        return null;
    }

}
