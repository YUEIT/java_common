package cn.yue.base.middle.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yue.base.middle.R;

/**
 * Description :
 * Created by yue on 2018/11/13
 */
public class PageHintView extends RelativeLayout{

    public PageHintView(Context context) {
        this(context, null);
    }

    public PageHintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private ImageView hintIV;
    private TextView hintTV;
    private void initView(Context context) {
        inflate(context, R.layout.layout_page_hint, this);
        hintIV = findViewById(R.id.hintIV);
        hintTV = findViewById(R.id.hintTV);
        setDefault();
    }

    private void setDefault() {
        if (isNotNullHintTV()) {
            hintTV.setTextSize(13f);
            hintTV.setTextColor(0xffa4a4a4);
        }
    }

    public void setHintText(String s) {
        if (isNotNullHintTV()) {
            hintTV.setText(s);
        }
    }

    public void setHintTextColor(@ColorInt int color) {
        if (isNotNullHintTV()) {
            hintTV.setTextColor(color);
        }
    }

    public void setHintTextSize(float size) {
        if (isNotNullHintTV()) {
            hintTV.setTextSize(size);
        }
    }

    public void setHintTextSize(int unit, float size) {
        if (isNotNullHintTV()) {
            hintTV.setTextSize(unit, size);
        }
    }

    public void setHintTextTypeface(Typeface typeface) {
        if (isNotNullHintTV()) {
            hintTV.setTypeface(typeface);
        }
    }

    public void setHintTextDrawable(Drawable left, Drawable top, Drawable right, Drawable bottom, int padding) {
        if (isNotNullHintTV()) {
            hintTV.setCompoundDrawables(left, top, right, bottom);
            hintTV.setCompoundDrawablePadding(padding);
        }
    }

    public void setHintTextMarginTop(int marginTop) {
        if (isNotNullHintTV()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)hintTV.getLayoutParams();
            layoutParams.topMargin = marginTop;
            hintTV.setLayoutParams(layoutParams);
        }
    }

    public void setHintImage(@DrawableRes int resId) {
        if (isNotNullHintIV()) {
            hintIV.setImageResource(resId);
        }
    }

    public void setHintImageSize(int w, int h) {
        if (isNotNullHintIV()) {
           RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w, h);
           hintIV.setLayoutParams(layoutParams);
        }
    }

    private boolean isNotNullHintTV() {
        return hintTV != null;
    }

    private boolean isNotNullHintIV() {
        return hintIV != null;
    }

}
