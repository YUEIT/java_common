package cn.yue.base.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yue.base.common.R;

/**
 * Description :
 * Created by yue on 2019/3/8
 */
public class TopBar extends RelativeLayout{

    private LinearLayout leftLL;
    private LinearLayout rightLL;
    private TextView leftTV;
    private ImageView leftIV;
    private TextView centerTV;
    private TextView rightTV;
    private ImageView rightIV;
    public TopBar(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_top_bar, this);
        leftLL = findViewById(R.id.leftLL);
        rightLL = findViewById(R.id.rightLL);
        leftTV = findViewById(R.id.leftTV);
        leftIV = findViewById(R.id.leftIV);
        centerTV = findViewById(R.id.centerTV);
        rightTV = findViewById(R.id.rightTV);
        rightIV = findViewById(R.id.rightIV);
        defaultStyle();
    }

    private void defaultStyle() {
        leftIV.setImageResource(R.drawable.app_icon_left_back);
        leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        leftTV.setTextColor(Color.parseColor("#4a4a4a"));
        centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        centerTV.setTextColor(Color.parseColor("#4a4a4a"));
        rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        rightTV.setTextColor(Color.parseColor("#4a4a4a"));
        leftTV.setVisibility(GONE);
        rightTV.setVisibility(GONE);
        rightIV.setVisibility(GONE);
    }

    public TopBar setLeftTextStr(String s) {
        if (leftTV != null) {
            leftTV.setVisibility(VISIBLE);
            leftTV.setText(s);
        }
        return this;
    }

    public TopBar setLeftImage(@DrawableRes int resId) {
        if (leftIV != null) {
            leftIV.setVisibility(VISIBLE);
            leftIV.setImageResource(resId);
        }
        return this;
    }

    public TopBar setLeftClickListener(OnClickListener onClickListener) {
        if (leftLL != null) {
            leftLL.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setCenterTextStr(String s) {
        if (centerTV != null) {
            centerTV.setVisibility(VISIBLE);
            centerTV.setText(s);
        }
        return this;
    }

    public TopBar setCenterClickListener(OnClickListener onClickListener) {
        if (centerTV != null) {
            centerTV.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setRightTextStr(String s) {
        if (rightTV != null) {
            rightTV.setVisibility(VISIBLE);
            rightTV.setText(s);
        }
        return this;
    }

    public TopBar setRightImage(@DrawableRes int resId) {
        if (rightIV != null) {
            rightIV.setVisibility(VISIBLE);
            rightIV.setImageResource(resId);
        }
        return this;
    }

    public TopBar setRightClickListener(OnClickListener onClickListener) {
        if (rightLL != null) {
            rightLL.setOnClickListener(onClickListener);
        }
        return this;
    }

    public TopBar setLeftTextSize(float sp) {
        if (leftTV != null) {
            leftTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setLeftTextColor(@ColorInt int color) {
        if (leftTV != null) {
            leftTV.setTextColor(color);
        }
        return this;
    }

    public TopBar setCenterTextSize(float sp) {
        if (centerTV != null) {
            centerTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setCenterTextColor(@ColorInt int color) {
        if (centerTV != null) {
            centerTV.setTextColor(color);
        }
        return this;
    }

    public TopBar setRightTextSize(float sp) {
        if (rightTV != null) {
            rightTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        }
        return this;
    }

    public TopBar setRightTextColor(@ColorInt int color) {
        if (rightTV != null) {
            rightTV.setTextColor(color);
        }
        return this;
    }
}
