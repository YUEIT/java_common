package cn.yue.base.test.component;

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

    @Override
    protected int getItemType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test_other;
    }


}
