package cn.yue.base.middle.mvvm.data;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.middle.net.wrapper.IListModel;

/**
 * Description :
 * Created by yue on 2022/3/7
 */

public class UiViewModels extends ArrayList<ItemViewModel> implements IListModel<ItemViewModel> {

    @Override
    public List<ItemViewModel> getList() {
        return this;
    }

    @Override
    public int getTotal() {
        return 0;
    }

    @Override
    public int getPageNo() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return size();
    }

    @Override
    public String getPageNt() {
        return null;
    }

    @Override
    public int getCurrentPageTotal() {
        return size();
    }
}
