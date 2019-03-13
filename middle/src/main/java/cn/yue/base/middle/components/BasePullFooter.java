package cn.yue.base.middle.components;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yue.base.middle.R;
import cn.yue.base.middle.mvp.IStatusView;
import cn.yue.base.middle.mvp.PageStatus;

/**
 * Description :
 * Created by yue on 2019/3/7
 */
public class BasePullFooter extends RelativeLayout implements IStatusView {

    private PageStatus status = PageStatus.STATUS_LOADING_ADD;
    private OnReloadListener onReloadListener;
    private TextView hintTV;
    public BasePullFooter(Context context) {
        super(context);
        View.inflate(context, R.layout.layout_footer_base_pull, this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == PageStatus.STATUS_ERROR_NET && onReloadListener != null) {
                    onReloadListener.onReload();
                }
            }
        });
        hintTV = findViewById(R.id.hintTV);
    }

    @Override
    public void showStatusView(PageStatus status) {
        this.status = status;
        switch (status) {
            case STATUS_LOADING_ADD:
                hintTV.setText("加载中~");
                break;
            case STATUS_END:
                hintTV.setText("- END -");
                break;
            case STATUS_ERROR_NET:
                hintTV.setText("网络异常，点击重新加载~");
                break;
        }
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    interface OnReloadListener {
        void onReload();
    }
}
