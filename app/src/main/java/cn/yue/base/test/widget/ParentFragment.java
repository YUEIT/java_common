package cn.yue.base.test.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.widget.viewpager.SampleTabStrip;
import cn.yue.base.common.widget.viewpager.SampleTabStrip2;
import cn.yue.base.middle.mvp.components.BaseHintFragment;
import cn.yue.base.test.R;

@Route(path = "/app/parent")
public class ParentFragment extends BaseHintFragment {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_parent;
    }


    MyPageAdapter2 pageAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        pageAdapter = new MyPageAdapter2(mActivity);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pageAdapter);
        SampleTabStrip2 tab = findViewById(R.id.tabs);
        tab.setViewPager(viewPager);
    }

    class  MyPageAdapter2 extends FragmentStateAdapter implements SampleTabStrip2.LayoutTabProvider {

        public MyPageAdapter2(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ChildFragment();
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public View createTabView() {
            return View.inflate(mActivity, R.layout.item_viewpager_test, null);
        }

        @Override
        public void bindTabView(View view, int position, boolean isSelected) {
            TextView tab = view.findViewById(R.id.itemTV);
            if (isSelected) {
                tab.setTextColor(Color.parseColor("#ff0000"));
            } else {
                tab.setTextColor(Color.parseColor("#00ff00"));
            }
        }
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
            return new ChildFragment();
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
    }
}
