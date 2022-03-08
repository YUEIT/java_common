package cn.yue.base.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;
import cn.yue.base.middle.mvp.components.BaseHintFragment;
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
        recyclerView.setAdapter(new CommonAdapter<Item>(mActivity, initList()) {
            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_test;
            }

            @Override
            public void bindData(CommonViewHolder<Item> holder, int position, Item item) {
                holder.setText(R.id.testTV, item.name);
                holder.itemView.setOnClickListener(item.onClickListener);
            }
        });
    }

    private List<Item> initList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("pull", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPull")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("pull-VM", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPullVM")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("page", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPullList")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("page-VM", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPageVM")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("page-VM2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/testPageVM2")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("Header-viewPager", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/parent")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("Header-viewPager2", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/app/parent2")
                        .navigation(mActivity);
            }
        }));
        list.add(new Item("select photos", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FRouter.getInstance()
                        .build("/common/selectPhoto")
                        .navigation(mActivity);
            }
        }));
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            List<Uri> uris = data.getParcelableArrayListExtra("photos");
        }
    }

    static class Item {
        final String name;
        final View.OnClickListener onClickListener;

        Item(String name, View.OnClickListener onClickListener) {
            this.name = name;
            this.onClickListener = onClickListener;
        }
    }
}
