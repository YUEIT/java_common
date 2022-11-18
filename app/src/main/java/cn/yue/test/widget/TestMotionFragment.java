package cn.yue.test.widget;

import android.os.Bundle;

import androidx.constraintlayout.motion.widget.MotionLayout;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.middle.mvp.components.BaseHintFragment;
import cn.yue.test.R;


@Route(path = "/app/testMotion")
public class TestMotionFragment extends BaseHintFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test_motion_editor;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        MotionLayout motionLayout = findViewById(R.id.motionL);
//        motionLayout.transitionToEnd();
    }
}
