package cn.yue.base.common.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.trello.rxlifecycle2.components.RxActivity;

import cn.yue.base.common.utils.app.RunTimePermissionUtil;

/**
 * 介绍：
 * 作者：luobiao
 * 邮箱：luobiao@imcoming.cn
 * 时间：2017/2/20.
 */

public abstract class BaseActivity extends RxActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasContentView()) {
            setContentView(getLayoutId());
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            initBundle(getIntent().getExtras());
        }
        initView();
    }

    protected boolean hasContentView() {
        return true;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initBundle(Bundle bundle) {}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private AlertDialog failDialog;
    public void showFailDialog() {
        if (failDialog == null) {
            failDialog = new AlertDialog.Builder(this)
                    .setTitle("消息")
                    .setMessage("当前应用无此权限，该功能暂时无法使用。如若需要，请单击确定按钮进行权限授权！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
