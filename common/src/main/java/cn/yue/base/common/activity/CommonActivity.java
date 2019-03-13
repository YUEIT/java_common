package cn.yue.base.common.activity;

import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;

import cn.yue.base.common.utils.debug.LogUtils;


/**
 * Created by yue on 2018/6/4.
 */

public class CommonActivity extends BaseFragmentActivity{

    @Override
    public Fragment getFragment() {
        Fragment fragment = null;
        if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getParcelable(FRouter.TAG) != null) {
            FRouter fRouter = getIntent().getExtras().getParcelable(FRouter.TAG);
            try {
                fragment = (Fragment) ARouter.getInstance()
                        .build(fRouter.getPath())
                        .with(getIntent().getExtras())
                        .setTimeout(fRouter.getTimeout())
//                        .withTransition(fRouter.getEnterAnim(), fRouter.getExitAnim())
                        .navigation(this, new NavigationCallback() {
                            @Override
                            public void onFound(Postcard postcard) {

                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                //showError();
                                LogUtils.e("no find page " + postcard);
                            }

                            @Override
                            public void onArrival(Postcard postcard) {

                            }

                            @Override
                            public void onInterrupt(Postcard postcard) {

                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return fragment;
    }
}