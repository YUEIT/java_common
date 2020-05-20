package cn.yue.base.common.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.photo.data.MediaVO;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

@Route(path = "/common/selectPhoto")
public class SelectPhotoActivity extends BaseFragmentActivity {

    private int maxNum = 1;
    private List<MediaVO> photoList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            maxNum = getIntent().getIntExtra("maxNum", 1);
            List<Uri> defaultList = getIntent().getParcelableArrayListExtra("photos");
            if (defaultList != null) {
                for (Uri uri: defaultList) {
                    MediaVO mediaVO = new MediaVO();
                    mediaVO.setUri(uri);
                    photoList.add(mediaVO);
                }
            }
        }
        changeFragment(SelectPhotoFragment.class.getName(), "最近照片");
    }

    private void changeTopBar(String title) {
        if (currentFragment instanceof SelectPhotoFolderFragment) {
            getTopBar().setCenterTextStr(title)
                    .setLeftImage(R.drawable.app_icon_back)
                    .setLeftTextStr("")
                    .setLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setRightTextStr("取消")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
        } else if (currentFragment instanceof SelectPhotoFragment) {
            getTopBar().setLeftTextStr("相册")
                    .setLeftImage(R.drawable.app_icon_back)
                    .setLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeFragment(SelectPhotoFolderFragment.class.getName(), "相册选择");
                        }
                    })
                    .setCenterTextStr(title)
                    .setRightTextStr(photoList.isEmpty() ? "取消" : "确定（" + photoList.size() + "/" + getMaxNum() +  "）")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (photoList.isEmpty()) {
                                finish();
                            } else {
                                finishAllWithResult((ArrayList<MediaVO>) photoList);
                            }
                        }
                    });
        }
    }

    private void changeFragment(String fragmentName, String title) {
        // check input
        BaseFragment showFragment = getFragment(fragmentName);
        if (showFragment != currentFragment && fragmentManager != null) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            setAnimation(transaction);
            if (null == currentFragment) {
                transaction.add(R.id.content, showFragment, showFragment.getClass().getName()).commitAllowingStateLoss();
            } else {
                // 已经加载了
                if (showFragment.isAdded()) {
                    transaction.show(showFragment).hide(currentFragment).commitAllowingStateLoss();
                } else {
                    transaction.add(R.id.content, showFragment, showFragment.getClass().getName()).hide(currentFragment).commitAllowingStateLoss();
                }
            }
            currentFragment = showFragment;
        }
        changeTopBar(title);
    }

    private void setAnimation(FragmentTransaction transaction) {
        if (currentFragment instanceof SelectPhotoFragment) {
            transaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        } else {
            transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        }
    }

    public void changeToSelectPhotoFragment(String folderId, String name) {
        changeFragment(SelectPhotoFragment.class.getName(), name);
        ((SelectPhotoFragment) currentFragment).refresh(folderId);
    }

    private String fragmentNames[] = new String[]{SelectPhotoFragment.class.getName(), SelectPhotoFolderFragment.class.getName()};
    private Map<String, BaseFragment> fragments = new HashMap();

    private BaseFragment getFragment(String fragmentName) {
        BaseFragment fragment = fragments.get(fragmentName);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            fragment = (BaseFragment) Fragment.instantiate(this, fragmentName, bundle);
            fragments.put(fragmentName, fragment);
        }
        return fragment;
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    public List<MediaVO> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<MediaVO> photoList) {
        this.photoList = photoList;
    }

    public int getMaxNum() {
        return maxNum;
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof SelectPhotoFragment) {
            changeFragment(SelectPhotoFolderFragment.class.getName(), "相册选择");
        } else if (getCurrentFragment() instanceof SelectPhotoFolderFragment) {
            finish();
        }
    }

    private void finishAllWithResult(ArrayList<MediaVO> selectList) {
        Intent intent = new Intent();
        ArrayList<Uri> uris = new ArrayList<>();
        for (MediaVO mediaVO : selectList) {
            uris.add(mediaVO.getUri());
        }
        intent.putParcelableArrayListExtra("photos", uris);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
