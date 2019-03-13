package cn.yue.base.common.image;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseActivity;

/**
 * Description :
 * Created by yue on 2018/11/27
 */
@Route(path = "/common/viewPhoto")
public class ViewPhotoActivity extends BaseActivity{

    private List<String> photoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_photo;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        if (bundle.getStringArrayList("list") != null) {
            photoList = bundle.getStringArrayList("list");
        }
    }

    @Override
    protected void initView() {
        PhotoViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(photoAdapter);
    }

    private PagerAdapter photoAdapter = new PagerAdapter() {

        private HashMap<Integer, PhotoView> mViewCache = new HashMap<>();

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            PhotoView photoView = mViewCache.get(position);
            ((PhotoViewPager)container).setCurrentPhotoView(photoView, position, photoList.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PhotoView photoView = mViewCache.get(position);
            if (photoView == null) {
                photoView = new PhotoView(ViewPhotoActivity.this);
                photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                photoView.loadImage(photoList.get(position));
                mViewCache.put(position, photoView);
            }
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            PhotoView photoView = mViewCache.get(position);
            if (photoView != null) {
                mViewCache.remove(position);
            }
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    };
}
