package cn.yue.base.test;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.widget.TopBar;
import cn.yue.base.middle.components.BasePullFragment;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
@Route(path = "/app/pullTest")
public class PullTestFragment extends BasePullFragment{

    @Override
    protected void initTopBar(TopBar topBar) {
        super.initTopBar(topBar);
        topBar.setCenterTextStr("pull test");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_pull_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView tv = findViewById(R.id.tv1);
        tv.setText("lllllllllll");
    }

    @Override
    protected void refresh() {
        stopRefreshAnim();
    }
}
