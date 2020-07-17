package cn.yue.base.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.middle.components.BaseHintFragment;
import cn.yue.base.middle.router.FRouter;

/**
 * Description :
 * Created by yue on 2019/6/5
 */
@Route(path = "/app/test")
public class TestFragment extends BaseHintFragment{

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_test;
    }

    private CommonAdapter commonAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView option1TV = findViewById(R.id.option1TV);
        option1TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPull")
                        .navigation(mActivity);
//                FRouter.getInstance().build("/common/selectPhoto").navigation(mActivity, 1);
            }
        });
        TextView option2TV = findViewById(R.id.option2TV);
        option2TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPageVM")
                        .navigation(mActivity);
//                FRouter.getInstance().build("/common/selectPhoto").navigation(mActivity, 1);
            }
        });

    }
    ImageView testImage;


    private void hookOnClickListener(View view) {
        try {
            // 得到 View 的 ListenerInfo 对象
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object listenerInfo = getListenerInfo.invoke(view);
            // 得到 原始的 OnClickListener 对象
            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = listenerInfoClz.getDeclaredField("mOnClickListener");
            mOnClickListener.setAccessible(true);
            View.OnClickListener originOnClickListener = (View.OnClickListener) mOnClickListener.get(listenerInfo);
            // 用自定义的 OnClickListener 替换原始的 OnClickListener
            View.OnClickListener hookedOnClickListener = new HookedOnClickListener(originOnClickListener);
            mOnClickListener.set(listenerInfo, hookedOnClickListener);
        } catch (Exception e) {

        }
    }

    class HookedOnClickListener implements View.OnClickListener {
        private View.OnClickListener origin;

        HookedOnClickListener(View.OnClickListener origin) {
            this.origin = origin;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mActivity, "hook click", Toast.LENGTH_SHORT).show();
            if (origin != null) {
                origin.onClick(v);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            List<Uri> uris = data.getParcelableArrayListExtra("photos");
            commonAdapter.setList(uris);
        }
    }

}
