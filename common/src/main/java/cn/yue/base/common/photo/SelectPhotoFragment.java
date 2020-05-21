package cn.yue.base.common.photo;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragment;
import cn.yue.base.common.activity.PermissionCallBack;
import cn.yue.base.common.image.ImageLoader;
import cn.yue.base.common.photo.data.MediaVO;
import cn.yue.base.common.utils.app.RunTimePermissionUtil;
import cn.yue.base.common.utils.code.ThreadPoolUtils;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.common.widget.TopBar;
import cn.yue.base.common.widget.recyclerview.CommonAdapter;
import cn.yue.base.common.widget.recyclerview.CommonViewHolder;

/**
 * Description :
 * Created by yue on 2019/3/11
 */

public class SelectPhotoFragment extends BaseFragment {

    private final int PAGE_COUNT = 50;
    private CommonAdapter<MediaVO> adapter;
    private List<MediaVO> photoList = new ArrayList<>();
    private int page;
    private boolean isCanLoadMore = true;

    @Override
    protected void initTopBar(TopBar topBar) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_photo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RecyclerView photoRV = findViewById(R.id.photoRV);
        photoRV.setLayoutManager(new GridLayoutManager(mActivity, 4));
        photoRV.setAdapter(adapter = new CommonAdapter<MediaVO>(mActivity, photoList) {

            @Override
            public int getLayoutIdByType(int viewType) {
                return R.layout.item_select_photo;
            }

            @Override
            public void bindData(CommonViewHolder<MediaVO> holder, int position, final MediaVO mediaVO) {
                ImageView photoIV = holder.getView(R.id.photoIV);
                final CheckBox checkIV = holder.getView(R.id.checkIV);

                photoIV.setBackgroundColor(Color.parseColor("#ffffff"));
                ImageLoader.getLoader().loadImage(photoIV, mediaVO.getUri());
                photoIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getSelectList().size() >= getMaxNum() && !checkIV.isChecked()){
                            return;
                        }
                        checkIV.setChecked(!checkIV.isChecked());
                        addSelectList(mediaVO, checkIV.isChecked());
                        topBar.setRightTextStr(getSelectList().isEmpty()? "取消" : "确定（" + getSelectList().size() + "/" + getMaxNum() +  "）");
                    }
                });
                if (contains(mediaVO)) {
                    checkIV.setChecked(true);
                } else {
                    checkIV.setChecked(false);
                }
            }
        });
        photoRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                LogUtils.e("" + layoutManager.findLastVisibleItemPosition());
                if ((photoList.size() - 5 <= layoutManager.findLastVisibleItemPosition()) && isCanLoadMore) {
                    isCanLoadMore = false;
                    getPhotoList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        getPhotoList();
    }

    public void refresh(String folderId) {
        this.folderId = folderId;
        allMedia = null;
        adapter.setList(null);
        page = 0;
        photoList.clear();
        getPhotoList();
    }

    private String folderId;

    private void getPhotoList() {
        RunTimePermissionUtil.requestPermissions(mActivity, RunTimePermissionUtil.REQUEST_CODE, new PermissionCallBack() {
            @Override
            public void requestSuccess(String permission) {
                ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils(ThreadPoolUtils.Type.SingleThread, 1);
                threadPoolUtils.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (allMedia == null) {
                            if (TextUtils.isEmpty(folderId)) {
                                allMedia = PhotoUtils.getTheLastPhotos(mActivity, 100);
                            } else if (Integer.parseInt(folderId) == -1){
                                allMedia = PhotoUtils.getPhotosByFolder(mActivity, true, folderId);
                            } else {
                                allMedia = PhotoUtils.getPhotosByFolder(mActivity, false, folderId);
                            }
                        }
                        if (allMedia == null) {
                            allMedia = new ArrayList<>();
                        }
                        int fromIndex = page * PAGE_COUNT;
                        if (fromIndex >= allMedia.size()) {
                            handler.sendMessage(Message.obtain(handler, 101, null));
                            return;
                        }
                        int toIndex = (page + 1) * PAGE_COUNT;
                        if (toIndex > allMedia.size()) {
                            toIndex = allMedia.size();
                        }
                        List<MediaVO> list = allMedia.subList(fromIndex, toIndex);
                        handler.sendMessage(Message.obtain(handler, 101, list));
                        page++;
                    }
                });
            }

            @Override
            public void requestFailed(String permission) {

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private List<MediaVO> allMedia;

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 101) {
                List<MediaVO> addList = (List<MediaVO>) msg.obj;
                if (addList == null || addList.isEmpty()) {
                    isCanLoadMore = false;
                } else {
                    isCanLoadMore = true;
                    photoList.addAll(addList);
                    adapter.setList(photoList);
                }
            }
            return false;
        }
    });

    private List<MediaVO> getSelectList() {
        if (((SelectPhotoActivity)mActivity).getPhotoList() == null) {
            ((SelectPhotoActivity)mActivity).setPhotoList(new ArrayList<>());
        }
        return ((SelectPhotoActivity)mActivity).getPhotoList();
    }

    private int getMaxNum() {
        if (((SelectPhotoActivity)mActivity).getMaxNum() <= 0) {
            return 1;
        }
        return ((SelectPhotoActivity)mActivity).getMaxNum();
    }

    private void addSelectList(MediaVO mediaVO, boolean checked) {
        if (checked) {
            for (MediaVO mediaVO1 : getSelectList()) {
                if (MediaVO.equals(mediaVO, mediaVO1)) {
                    return;
                }
            }
            getSelectList().add(mediaVO);
        } else {
            for (Iterator iterator = getSelectList().iterator(); iterator.hasNext();) {
                MediaVO mediaVO1 = (MediaVO)iterator.next();
                if (MediaVO.equals(mediaVO, mediaVO1)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private boolean contains(MediaVO mediaVO) {
        for (MediaVO mediaVO1 : getSelectList()) {
            if (MediaVO.equals(mediaVO, mediaVO1)) {
                return true;
            }
        }
        return false;
    }
}
