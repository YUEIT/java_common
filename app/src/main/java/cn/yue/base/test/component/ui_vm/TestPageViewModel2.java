package cn.yue.base.test.component.ui_vm;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.middle.mvvm.ListViewModel;
import cn.yue.base.middle.mvvm.data.UiViewModels;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import cn.yue.base.test.data.TestItemBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2022/3/7
 */

public class TestPageViewModel2 extends ListViewModel<UiViewModels, ItemViewModel> {

    public TestPageViewModel2(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void doLoadData(String nt) {
        getRequestSingle(nt)
                .compose(new PageTransformer())
                .subscribe();
    }

    protected Single<UiViewModels> getRequestSingle(String nt) {
        UiViewModels uiViewModels = new UiViewModels();
        for (int i=0; i < 20; i++) {
            TestItemBean testItemBean = new TestItemBean();
            testItemBean.setIndex(i);
            testItemBean.setName("this is " + i);
            if (i % 2 == 0) {
                TestItemViewModel testItemViewModel = new TestItemViewModel(testItemBean,this);
                uiViewModels.add(testItemViewModel);
            } else {
                TestItemViewModel2 testItemViewModel = new TestItemViewModel2(testItemBean,this);
                uiViewModels.add(testItemViewModel);
            }
        }
        return Single.just(uiViewModels);
    }
}
