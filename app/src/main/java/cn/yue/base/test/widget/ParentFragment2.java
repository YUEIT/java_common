package cn.yue.base.test.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.utils.view.ToastUtils;
import cn.yue.base.common.widget.viewpager.HeaderScrollHelper;
import cn.yue.base.common.widget.viewpager.HeaderScrollView;
import cn.yue.base.common.widget.viewpager.SampleFragmentPagerAdapter;
import cn.yue.base.common.widget.viewpager.SampleTabStrip;
import cn.yue.base.middle.components.BaseHintFragment;
import cn.yue.base.middle.view.refresh.IRefreshLayout;
import cn.yue.base.middle.view.refresh.OverRefreshLayout;
import cn.yue.base.middle.view.refresh.SwipeRefreshLayout;
import cn.yue.base.test.R;

@Route(path = "/app/parent2")
public class ParentFragment2 extends BaseHintFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_parent2;
    }


    MyPageAdapter pageAdapter;
    HeaderScrollView headerScrollView;
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        SwipeRefreshLayout refreshL = findViewById(R.id.refreshL);
        refreshL.setOnRefreshListener(new IRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshL.finishRefreshing();
            }
        });
//        refreshL.setEnableLoadmore(false);
//        refreshL.setEnableOverScroll(false);
        headerScrollView = findViewById(R.id.headerViewPager);
//        headerViewPager.setTopOffset(DisplayUtils.dip2px(50));
        pageAdapter = new MyPageAdapter(mFragmentManager);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        SampleTabStrip tab = findViewById(R.id.tabs);
        tab.setViewPagerAutoRefresh(viewPager);
        FrameLayout headerFL = findViewById(R.id.headerFL);
        headerFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("click header");
            }
        });
    }

    class MyPageAdapter extends SampleFragmentPagerAdapter implements SampleTabStrip.LayoutTabProvider {

        public MyPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new ChildFragment2();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void onFragmentSelected(Fragment fragment, int position) {
            if (fragment instanceof HeaderScrollHelper.ScrollableContainer) {
                HeaderScrollHelper.ScrollableContainer scrollContainer = (HeaderScrollHelper.ScrollableContainer)fragment;
                headerScrollView.setCurrentScrollableContainer(scrollContainer);
            }
        }

        @Override
        public View createTabView(int position) {
            return View.inflate(mActivity, R.layout.item_viewpager_test, null);
        }

        @Override
        public void changeTabStyle(View v, boolean isSelect) {
            TextView tab = v.findViewById(R.id.itemTV);
            if (isSelect) {
                tab.setTextColor(Color.parseColor("#ff0000"));
            } else {
                tab.setTextColor(Color.parseColor("#00ff00"));
            }
        }

    }
}
