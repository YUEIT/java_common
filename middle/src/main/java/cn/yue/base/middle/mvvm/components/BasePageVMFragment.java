package cn.yue.base.middle.mvvm.components;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.mvvm.PageViewModel;

public class BasePageVMFragment<VM extends PageViewModel<S>, S> extends BaseListVMFragment<VM> {

    @Override
    public CommonAdapter<S> getAdapter() {
        return null;
    }

}
