package cn.yue.base.common.image;

import android.graphics.Bitmap;

/**
 * Description :
 * Created by yue on 2019/3/15
 */
public interface LoadBitmapCallBack {

    void onBitmapLoaded(Bitmap bitmap);

    void onBitmapNoFound();
}
