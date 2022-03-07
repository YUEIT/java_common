package cn.yue.base.test.component.ui_vm;

import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;

public class TestItemViewModel extends ItemViewModel {

    public TestItemBean itemBean;
    public TestItemViewModel(TestItemBean itemBean, BaseViewModel parentViewModel) {
        super(parentViewModel);
        this.itemBean = itemBean;
    }

}
