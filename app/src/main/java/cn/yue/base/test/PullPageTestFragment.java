package cn.yue.base.test;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BasePullPageFragment;
import cn.yue.base.middle.net.wrapper.BaseListBean;
import io.reactivex.Single;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
@Route(path = "/app/pullPageTest")
public class PullPageTestFragment extends BasePullPageFragment<OrderBean> {

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
        View view = View.inflate(mActivity, R.layout.custom_top_bar, null);
        customTopBar(view);
    }



    @Override
    protected int getItemLayoutId() {
        return R.layout.item_order_list;
    }

    @Override
    protected void bindItemData(CommonViewHolder<OrderBean> holder, int position, OrderBean orderBean) {
        holder.setText(R.id.tv_order_tag, orderBean.getMarkCode() + "");
        holder.setText(R.id.tv_order_code, orderBean.getOrderId());
        holder.setText(R.id.tv_order_create_time, orderBean.getCreateTime() + "");
    }

    @Override
    protected Single<BaseListBean<OrderBean>> getRequestSingle(String nt) {
        return RetrofitUtils.getJavaService().getOrderList("534c0aca0b17caa95e65d838c9359524", today, nt, 20);
    }

}
