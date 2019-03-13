package cn.yue.base.test;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.test.databinding.FragmentDatabingTestBinding;
import cn.yue.base.middle.components.BaseHintDBFragment;

/**
 * Description :
 * Created by yue on 2019/3/11
 */
@Route(path = "/app/hintDBTest")
public class HintDBTestFragment extends BaseHintDBFragment<FragmentDatabingTestBinding>{

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_databing_test;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        binding.tv1.setText("xixixixixix");
    }

}
