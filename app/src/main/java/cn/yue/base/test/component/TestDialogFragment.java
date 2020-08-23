package cn.yue.base.test.component;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.activity.BaseDialogFragment;
import cn.yue.base.common.activity.TransitionAnimation;
import cn.yue.base.test.R;

public class TestDialogFragment extends BaseDialogFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void initEnterStyle() {
        setEnterStyle(TransitionAnimation.TRANSITION_LEFT);
    }
}
