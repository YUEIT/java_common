package cn.yue.base.test;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.components.BaseHintFragment;
import cn.yue.base.middle.router.FRouter;
import cn.yue.base.test.component.TestDialogFragment;

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

    @Override
    protected void initTopBar(TopBar topBar) {
        super.initTopBar(topBar);
        topBar.setContentVisibility(View.GONE);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        List<Object> list = new ArrayList<>();
        for (int i=0; i < 8; i++) {
            list.add(new Object());
        }

        recyclerView.setAdapter(new CommonAdapter(mActivity, list) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_test;
            }

            @Override
            public void bindData(CommonViewHolder holder, int position, Object o) {
                if (position == 0) {
                    holder.setText(R.id.testTV, "pull");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/testPull")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 1) {
                    holder.setText(R.id.testTV, "pull-VM");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/testPullVM")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 2) {
                    holder.setText(R.id.testTV, "page");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/testPullList")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 3) {
                    holder.setText(R.id.testTV, "page-VM");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/testPageVM")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 4) {
                    holder.setText(R.id.testTV, "Header-viewPager");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/parent")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 5) {
                    holder.setText(R.id.testTV, "Header-viewPager2");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FRouter.getInstance()
                                    .build("/app/parent2")
                                    .navigation(mActivity);
                        }
                    });
                }
                if (position == 6) {
                    holder.setText(R.id.testTV, "Motion Layout");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            FRouter.getInstance()
//                                    .build("/app/testMotion")
//                                    .navigation(mActivity);
                            startLive();
                        }
                    });
                }
                if (position == 7) {
                    holder.setText(R.id.testTV, "dialog test");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new TestDialogFragment().show(mFragmentManager, "");
                        }
                    });
                }
            }
        });
    }


    private void startLive() {
        showActivity("com.iqoo.secure");
    }

    /**
     * 跳转到指定应用的首页
     */
    private void showActivity(@NonNull String packageName) {
        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

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
        }
    }

}
