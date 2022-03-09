package cn.yue.base.common.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.rx.ILifecycleProvider;
import cn.yue.base.common.activity.rx.RxLifecycleProvider;
import cn.yue.base.common.utils.app.BarUtils;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.utils.variable.ResourceUtils;
import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.dialog.HintDialog;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public abstract class BaseActivity extends FragmentActivity {

    private ILifecycleProvider<Lifecycle.Event> lifecycleProvider;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleProvider = initLifecycleProvider();
        getLifecycle().addObserver(lifecycleProvider);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (hasContentView()) {
            setStatusBar();
            setContentView(getLayoutId());
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            initBundle(getIntent().getExtras());
        }
        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected ILifecycleProvider<Lifecycle.Event> initLifecycleProvider() {
        return new RxLifecycleProvider();
    }

    protected boolean hasContentView() {
        return true;
    }

    protected void initBundle(Bundle bundle) {}

    public void setStatusBar() {
        setStatusBar(false);
    }

    public void setStatusBar(boolean isFullScreen) {
        setSystemBar(isFullScreen, true);
    }

    public void setSystemBar(boolean isFullScreen, boolean isDarkIcon) {
        setSystemBar(isFullScreen, isDarkIcon, Color.WHITE);
    }

    public void setSystemBar(boolean isFullScreen, boolean isDarkIcon, int bgColor) {
        BarUtils.setStyle(this, isFullScreen, isDarkIcon, bgColor);
    }

    public ILifecycleProvider<Lifecycle.Event> getLifecycleProvider() {
        return lifecycleProvider;
    }

    public void requestPermission(String permission, PermissionCallBack permissionCallBack) {
        RunTimePermissionUtil.requestPermissions(this, RunTimePermissionUtil.REQUEST_CODE, permissionCallBack, permission);
    }

    public void requestPermission(String[] permissions, PermissionCallBack permissionCallBack) {
        RunTimePermissionUtil.requestPermissions(this, RunTimePermissionUtil.REQUEST_CODE, permissionCallBack, permissions);
    }

    public void requestPermission(String[] permissions, int requestCode, PermissionCallBack permissionCallBack) {
        RunTimePermissionUtil.requestPermissions(this, requestCode, permissionCallBack, permissions);
    }

    private PermissionCallBack permissionCallBack;

    public void setPermissionCallBack(PermissionCallBack permissionCallBack) {
        this.permissionCallBack = permissionCallBack;
    }

    private HintDialog failDialog;
    public void showFailDialog() {
        if (failDialog == null) {
            failDialog = new HintDialog.Builder(this)
                    .setTitleStr(ResourceUtils.getString(R.string.app_message))
                    .setContentStr(ResourceUtils.getString(R.string.app_permission_no_granted_and_to_request))
                    .setLeftClickStr(ResourceUtils.getString(R.string.app_cancel))
                    .setRightClickStr(ResourceUtils.getString(R.string.app_confirm))
                    .setOnRightClickListener(new HintDialog.OnRightClickListener() {
                        @Override
                        public void onRightClick() {
                            startSettings();
                        }
                    })
                    .build();
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
                        ToastUtils.showShort(String.format(ResourceUtils.getString(R.string.app_permission_request_fail),
                                RunTimePermissionUtil.getPermissionName(permissions[i])));
                        permissionCallBack.requestFailed(permissions[i]);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (failDialog != null) {
            failDialog.dismiss();
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
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
    }

}
