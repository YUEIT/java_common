package cn.yue.base.common.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.util.List;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.widget.TopBar;

/**
 * 介绍：
 * 作者：luobiao
 * 邮箱：luobiao@imcoming.cn
 * 时间：2017/2/20.
 */

public abstract class BaseFragmentActivity extends RxFragmentActivity {

    private TopBar topBar;
    private FrameLayout topFL;
    protected FragmentManager fragmentManager;
    protected BaseFragment currentFragment;
    private int resultCode;
    private Bundle resultBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0以上，调用系统方法
            Window window = getWindow();
            window.setStatusBarColor(Color.WHITE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.base_activity_layout);
        initView();
        replace(getFragment(), null, false);
    }

    private void initView() {
        topFL = findViewById(R.id.topBar);
        topFL.addView(topBar = new TopBar(this));
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                currentFragment = getCurrentFragment();
                if (currentFragment != null && resultCode == RESULT_OK) {
                    currentFragment.onFragmentResult(resultCode, resultBundle);
                }
                resultCode = RESULT_CANCELED;
                resultBundle = null;
            }
        });
    }

    public TopBar getTopBar() {
        return topBar;
    }

    public void customTopBar(View view) {
        topFL.removeAllViews();
        topFL.addView(view);
    }


    public abstract Fragment getFragment();

    public Fragment instantiate(Class<Fragment> mClass, Bundle args){
        return Fragment.instantiate(this, mClass.getSimpleName(), args);
    }

    public void replace(Fragment fragment, String tag , boolean canBack){
        if (null == fragment) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        if (TextUtils.isEmpty(tag)) {
            tag = UUID.randomUUID().toString();
        }
        transaction.replace(R.id.content, fragment, tag);
        if (canBack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();
    }

    public BaseFragment getCurrentFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof BaseFragment) {
            return (BaseFragment) fragment;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onStop() {
        super.onStop();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
            currentFragment = getCurrentFragment();
            if (currentFragment != null && currentFragment.onFragmentBackPressed()) {
                return;
            }
            superOnBackPressed();
    }

    public void superOnBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0 && resultCode != RESULT_CANCELED) {
            Intent data = null;
            if (resultBundle != null) {
                data = new Intent();
                data.putExtras(resultBundle);
            }
            setResult(resultCode, data);

        }
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void setFragmentResult(int resultCode, Bundle resultBundle) {
        this.resultCode = resultCode;
        this.resultBundle = resultBundle;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseFragment && fragment.isVisible()) {
                        ((BaseFragment) fragment).onNewIntent(intent.getExtras());
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isAdded() && fragment instanceof BaseFragment && fragment.isVisible()) {
                        ((BaseFragment) fragment).onFragmentResult(requestCode, data.getExtras());
                    }
                }
            }
        }
    }

    /**
     * 权限请求
     * @param permissions
     * @param requestCode
     */
    public void requestPermission(String[] permissions, int requestCode, PermissionCallBack permissionCallBack) {
        RunTimePermissionUtil.requestPermissions(this, requestCode, permissionCallBack, permissions);
    }

    private PermissionCallBack permissionCallBack;

    public void setPermissionCallBack(PermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
    }

    private AlertDialog failDialog;
    public void showFailDialog() {
        if (failDialog == null) {
            failDialog = new AlertDialog.Builder(this)
                    .setTitle("消息")
                    .setMessage("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSettings();
                        }
                    }).create();
        }
        failDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RunTimePermissionUtil.REQUEST_CODE) {
            if (permissionCallBack != null) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (verificationPermissions(grantResults)) {
                        permissionCallBack.requestSuccess(permissions[i]);
                    } else {
                        permissionCallBack.requestFailed(permissions[i]);
                    }
                }
            }
        }
    }

    private boolean verificationPermissions(int[] results) {
        for (int result : results) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    private void startSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
