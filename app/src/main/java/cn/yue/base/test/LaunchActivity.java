package cn.yue.base.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseActivity;
import cn.yue.base.middle.router.FRouter;

/**
 * Description :
 * Created by yue on 2019/3/21
 */
public class LaunchActivity extends BaseActivity {


    private void onSystemBarShow() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Window window = getWindow();
            window.addFlags(Window.FEATURE_NO_TITLE);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Translucent status bar
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSystemBarShow();
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        toStart();
    }

    @Override
    protected boolean hasContentView() {
        return false;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private void toStart() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FRouter.getInstance()
                        .build("/app/test")
                        .navigation(LaunchActivity.this);
                finish();

            }
        }, 2000);

    }


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }
}
