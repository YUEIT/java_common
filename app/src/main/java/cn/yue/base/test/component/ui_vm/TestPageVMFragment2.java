package cn.yue.base.test.component.ui_vm;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.List;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.common.widget.recyclerview.DiffRefreshAdapter;
import cn.yue.base.middle.mvvm.ItemViewModel;
import cn.yue.base.middle.mvvm.components.BaseListVMFragment;
import cn.yue.base.middle.mvvm.data.UiViewModels;
import cn.yue.base.test.BR;
import cn.yue.base.test.R;

/**
 * Description :
 * Created by yue on 2022/3/7
 */
@Route(path = "/app/testPageVM2")
public class TestPageVMFragment2 extends BaseListVMFragment<TestPageViewModel2, ItemViewModel> {

    @Override
    public CommonAdapter<ItemViewModel> initAdapter() {
        return new DiffRefreshAdapter<ItemViewModel>(mActivity) {
            @Override
            protected boolean areItemsTheSame(ItemViewModel item1, ItemViewModel item2) {
                return item1 == item2;
            }

            @Override
            protected boolean areContentsTheSame(ItemViewModel oldItem, ItemViewModel newItem) {
                return oldItem == newItem;
            }

            @Override
            public int getLayoutIdByType(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_test_other2;
                } else {
                    return R.layout.item_test_other;
                }
            }

            @Override
            public int getViewType(int position) {
                ItemViewModel itemViewModel = getList().get(position);
                if (itemViewModel instanceof TestItemViewModel) {
                    return 0;
                } else if (itemViewModel instanceof TestItemViewModel2) {
                    return 1;
                }
                return 0;
            }

            @Override
            public void bindData(CommonViewHolder<ItemViewModel> holder, int position, ItemViewModel itemViewModel) {
                ViewDataBinding binding = DataBindingUtil.bind(holder.itemView);
                if (binding != null) {
                    binding.setVariable(BR.viewModel, itemViewModel);
                    binding.executePendingBindings();
                }
            }
        };
    }

    @Override
    protected void setData(List<ItemViewModel> list) {
        getAdapter().setList(list);
    }
}
