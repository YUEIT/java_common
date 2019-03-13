package cn.yue.base.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseActivity;
import cn.yue.base.common.activity.FRouter;
import cn.yue.base.common.activity.PermissionCallBack;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.common.utils.debug.ToastUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        findViewById(R.id.jump0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/common/selectPhoto")
//                        .withTransition(R.anim.bottom_in, R.anim.top_out)
                        .navigation(MainActivity.this, 101);

            }
        });

        findViewById(R.id.jump1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/common/viewPhoto")
                        .withStringArrayList("list", (ArrayList<String>) photos)
                        .navigation(MainActivity.this);

            }
        });

        findViewById(R.id.jump2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/app/pullTest").navigation(MainActivity.this);
            }
        });

        findViewById(R.id.jump3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/app/pullPageTest").navigation(MainActivity.this);
            }
        });
        findViewById(R.id.jump4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/app/hintDBTest").navigation(MainActivity.this);
            }
        });
        findViewById(R.id.jump5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance().build("/app/pullPageTestDB").navigation(MainActivity.this);
            }
        });
        requestPermission(Manifest.permission.READ_PHONE_STATE, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                ToastUtils.showShortToast("success");
            }

            @Override
            public void requestFailed(String permission) {
                ToastUtils.showShortToast("failed");
            }
        });
    }

    List<String> photos = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                List<String> list = data.getStringArrayListExtra("photos");
                LogUtils.i(""+list);
                photos.clear();
                photos.addAll(list);
            }
        }
    }
}
