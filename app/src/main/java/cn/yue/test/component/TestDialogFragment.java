package cn.yue.test.component;

import cn.yue.base.common.activity.BaseDialogFragment;
import cn.yue.base.common.activity.TransitionAnimation;
import cn.yue.test.R;


public class TestDialogFragment extends BaseDialogFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEnterStyle() {
        setEnterStyle(TransitionAnimation.TRANSITION_LEFT);
    }
}
