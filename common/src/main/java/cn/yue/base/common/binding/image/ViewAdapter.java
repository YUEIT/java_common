package cn.yue.base.common.binding.image;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes", "viewCorner", "isCenterCrop", "startTopCorner", "endTopCorner", "startBottomCorner", "endBottomCorner"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes, int viewCorner, boolean isCenterCrop,
                                   boolean startTopCorner, boolean endTopCorner, boolean startBottomCorner, boolean endBottomCorner) {

    }

    @BindingAdapter(value = {"gif"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int gifRes) {

    }
}

