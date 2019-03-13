package cn.yue.base.test;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yue.base.common.widget.TopBar;
import cn.yue.base.test.databinding.ItemOrderListDatabindingBinding;
import cn.yue.base.middle.components.BasePullPageDBFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
@Route(path = "/app/pullPageTestDB")
public class PullPageTestDBFragment extends BasePullPageDBFragment<ItemOrderListDatabindingBinding, OrderBean>{

    Date date = new Date();
    String today = new SimpleDateFormat("yyyyMMdd").format(date);

    @Override
    protected void initTopBar(TopBar topBar) {
        super.initTopBar(topBar);
        topBar.setLeftClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAll();
                    }
                })
                .setCenterTextStr("测试")
                .setRightTextStr("确定");
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_order_list;
    }

    @Override
    protected void bindItemData(int position, OrderBean orderBean) {
        binding.tvOrderTag.setText(orderBean.getMarkCode() + "");
        binding.tvOrderCode.setText(orderBean.getOrderId());
        binding.tvOrderCreateTime.setText(orderBean.getCreateTime() + "");
    }

    @Override
    protected Single<BaseListBean<OrderBean>> getRequestSingle(String nt) {
        return RetrofitUtils.getJavaService().getOrderList("534c0aca0b17caa95e65d838c9359524", today, nt, 20);
    }

}
