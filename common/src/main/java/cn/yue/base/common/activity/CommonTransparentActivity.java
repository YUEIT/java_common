package cn.yue.base.common.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Created by yue on 2018/6/4.
 */

public class CommonTransparentActivity extends CommonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        removeTopBar();
        setContentBackground(Color.TRANSPARENT);
    }
}