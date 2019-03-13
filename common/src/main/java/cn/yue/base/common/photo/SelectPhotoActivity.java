package cn.yue.base.common.photo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.activity.PermissionCallBack;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.utils.debug.LogUtils;

/**
 * Description :
 * Created by yue on 2018/11/19
 */

@Route(path = "/common/selectPhoto")
public class SelectPhotoActivity extends BaseFragmentActivity {

    private List<String> photoList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPhotoError();
        changeFragment(SelectPhotoFragment.class.getName());
    }

    private void changeTopBar() {
        if (currentFragment instanceof SelectPhotoFolderFragment) {
            getTopBar().setCenterTextStr("相册选择")
                    .setRightTextStr("取消")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
        } else if (currentFragment instanceof SelectPhotoFragment) {
            getTopBar().setLeftTextStr("相册")
                    .setLeftClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeFragment(SelectPhotoFolderFragment.class.getName());
                        }
                    })
                    .setCenterTextStr("最近照片")
                    .setRightTextStr(photoList.isEmpty() ? "取消" : "确定（" + photoList.size() + "）")
                    .setRightClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (photoList.isEmpty()) {
                                finish();
                            } else {
                                finishAllWithResult((ArrayList<String>) photoList);
                            }
                        }
                    });
        }
    }

    private void changeFragment(String fragmentName) {
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
        changeTopBar();
    }

    private void setAnimation(FragmentTransaction transaction) {
        if (currentFragment instanceof SelectPhotoFragment) {
            transaction.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        } else {
            transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        }
    }

    public void changeToSelectPhotoFragment(String folderPath) {
        changeFragment(SelectPhotoFragment.class.getName());
        ((SelectPhotoFragment) currentFragment).refresh(folderPath);
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

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof SelectPhotoFragment) {
            changeFragment(SelectPhotoFolderFragment.class.getName());
        } else if (getCurrentFragment() instanceof SelectPhotoFolderFragment) {
            finish();
        }
    }

    private void finishAllWithResult(ArrayList<String> selectList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("photos", selectList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void initPhotoError() { // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    private final static int REQUEST_CODE_CAMERA = 102;
    private final static int REQUEST_CODE_PHOTO_CROP = 103;
    private boolean crop = false;
    private Uri targetUri;
    private String photoPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("" + requestCode + "," + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
//        if (requestCode == REQUEST_CODE_CAMERA) {
//            if (crop) {
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(targetUri, "image/*");
//                intent.putExtra("crop", "true");//可裁剪
//                //intent.putExtra("aspectX", 2);
//                //intent.putExtra("aspectY", 1);
//                intent.putExtra("scale", false);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
//                //intent.putExtra("return-data", false);//若为false则表示不返回数据
//                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                intent.putExtra("noFaceDetection", true);
//                startActivityForResult(intent, REQUEST_CODE_PHOTO_CROP);
//            } else {
//                ArrayList<String> temp = new ArrayList<>();
//                temp.add(photoPath);
//                finishAllWithResult(temp);
//            }
//        }
        if (requestCode == REQUEST_CODE_PHOTO_CROP) {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(photoPath);
            finishAllWithResult(temp);
        }

    }

    private void toCamera() {
        RunTimePermissionUtil.requestPermissions(this, RunTimePermissionUtil.REQUEST_CODE, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                String cachePath = Environment.getExternalStorageDirectory() + File.separator + "cache" + File.separator;
                File tempFile = new File(cachePath, UUID.randomUUID().toString() + ".jpg");
                photoPath = tempFile.getAbsolutePath();
                targetUri = Uri.fromFile(tempFile);

                if (targetUri != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
                    //intent.putExtra("return-data", false);//若为false则表示不返回数据
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            }

            @Override
            public void requestFailed(String permission) {

            }
        }, Manifest.permission.CAMERA);
    }

}
