package cn.yue.base.test.component.ui_vm;

import androidx.databinding.ObservableField;

import com.google.android.exoplayer2.C;

import cn.yue.base.common.binding.action.Consumer;
import cn.yue.base.middle.mvvm.BaseViewModel;
import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.middle.router.FRouter;
import cn.yue.base.test.R;
import cn.yue.base.test.data.TestItemBean;

public class TestItemViewModel2 extends ItemViewModel {

    public TestItemBean itemBean;
    public TestItemViewModel2(TestItemBean itemBean, BaseViewModel parentViewModel) {
        super(parentViewModel);
        this.itemBean = itemBean;
        nameField.set(itemBean.getName());
        onClick.set(new Consumer() {
            @Override
            public void accept() {
                FRouter.getInstance().build("/app/testPull").navigation(TestItemViewModel2.this);
                itemBean.setName("change");
                nameField.set(itemBean.getName());
            }
        });
    }

    public ObservableField<String> nameField = new ObservableField<>();

    public ObservableField<Consumer> onClick = new ObservableField<>();

}
