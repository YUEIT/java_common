package cn.yue.test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.middle.router.FRouter;

/**
 * Description :
 * Created by yue on 2019/3/21
 */
public class LaunchActivity extends BaseFragmentActivity {


    private void onSystemBarShow() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Window window = getWindow();
            window.addFlags(Window.FEATURE_NO_TITLE);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    private final Handler mHandler = new Handler(Looper.getMainLooper());

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
    protected void initView() {
        setStatusBar();
    }
}
