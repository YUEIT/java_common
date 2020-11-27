package cn.yue.base.test.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;

import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.test.R;

@Route(path = "/app/memory")
public class MemoryActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        TextView tv = findViewById(R.id.tv0);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishMessage = handler.obtainMessage(FINISH_MESSAGE, MemoryActivity.this);
                Message.obtain(finishMessage).sendToTarget();
            }
        });
        handler = new MemoryHandler();

    }

    private static final int FINISH_MESSAGE = 1;
    Message finishMessage;
    @Override
    public Fragment getFragment() {
        return null;
    }

    private MemoryHandler handler;
    private static class MemoryHandler extends Handler {

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == FINISH_MESSAGE) {
                ((MemoryActivity)msg.obj).finish();
            }
        }
    }
}
