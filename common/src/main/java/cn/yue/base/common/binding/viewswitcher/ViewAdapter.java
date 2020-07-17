package cn.yue.base.common.binding.viewswitcher;

import android.widget.ViewSwitcher;

import androidx.databinding.BindingAdapter;

/**
 * Created by goldze on 2017/6/16.
 */

public class ViewAdapter {

    @BindingAdapter(value = {"setChildAt"})
    public static void setChildAt(ViewSwitcher view, final int childIndex) {
        view.setDisplayedChild(childIndex);
    }


}
