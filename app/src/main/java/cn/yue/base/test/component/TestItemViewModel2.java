package cn.yue.base.test.component;

import androidx.databinding.ObservableField;

import cn.yue.base.common.binding.action.Consumer;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;

public class TestItemViewModel2 extends ItemViewModel {

    public TestItemBean itemBean;
    public TestItemViewModel2(TestItemBean itemBean, BaseViewModel parentViewModel) {
        super(parentViewModel);
        this.itemBean = itemBean;
        nameField.set(itemBean.getName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test_other2;
    }

    public ObservableField<String> nameField = new ObservableField<>();

    public Consumer onclick = new Consumer() {
        @Override
        public void accept() {
            itemBean.setName("change");
            nameField.set(itemBean.getName());
        }
    };
}
