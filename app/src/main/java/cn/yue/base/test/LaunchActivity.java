package cn.yue.base.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import cn.yue.base.common.activity.BaseActivity;
import cn.yue.base.middle.router.FRouter;

/**
 * Description :
 * Created by yue on 2019/3/21
 */
public class LaunchActivity extends BaseActivity {


    private void onSystemBarShow() {
        Window window = getWindow();
        window.addFlags(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
            }
        }
        // [END handle_data_extras]
        handleIntent(getIntent());

        toStart();
        // ATTENTION: This was auto-generated to handle app links.
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("content://192.168.10.135:8080/").buildUpon()
                    .appendPath(recipeId).build();
            Log.d("luobiao", "handleIntent: " + appData);
        }
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
