package cn.yue.base.test.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.headerviewpager.HeaderScrollHelper;
import cn.yue.base.common.widget.headerviewpager.HeaderScrollView;
import cn.yue.base.common.widget.headerviewpager.SampleTabStrip;
import cn.yue.base.middle.components.BaseHintFragment;
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
        headerScrollView = findViewById(R.id.headerViewPager);
//        headerViewPager.setTopOffset(DisplayUtils.dip2px(50));
        pageAdapter = new MyPageAdapter(mActivity, mFragmentManager);
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        SampleTabStrip tab = findViewById(R.id.tabs);
        tab.setViewPagerAutoRefresh(viewPager, true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                HeaderScrollHelper.ScrollableContainer container = (HeaderScrollHelper.ScrollableContainer) pageAdapter.getFragment(position);
                headerScrollView.setCurrentScrollableContainer(container);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyPageAdapter extends FragmentPagerAdapter implements SampleTabStrip.LayoutTabProvider {

        private List<Fragment> mFragments = new ArrayList<>();
        private Context context;
        public MyPageAdapter(Context context,  @NonNull FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new ChildFragment2();
        }

        public Fragment getFragment(int position) {
            return mFragments.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Fragment fragment =  (Fragment) super.instantiateItem(container, position);
            while (mFragments.size() <= position) {
                mFragments.add(null);
            }
            mFragments.set(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public View createTabView(int position) {
            return View.inflate(context, R.layout.item_viewpager_test, null);
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

        boolean isFirst = true;
        @Override
        public void finishUpdate(@NonNull ViewGroup container) {
            super.finishUpdate(container);
            if (isFirst) {
                HeaderScrollHelper.ScrollableContainer icontainer = (HeaderScrollHelper.ScrollableContainer) pageAdapter.getFragment(0);
                headerScrollView.setCurrentScrollableContainer(icontainer);
                isFirst = false;
            }
        }
    }
}
