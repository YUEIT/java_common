package cn.yue.base.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.android.arouter.launcher.ARouter;

import java.io.Serializable;
import java.util.ArrayList;

import cn.yue.base.common.Constant;
import cn.yue.base.common.R;


/**
 * Created by yue on 2018/6/5.
 */

public class FRouter implements Parcelable{

    public static final String TAG = "FRouter";
    private Uri uri;
    private Object tag;
    private Bundle mBundle;
    private String path;
    private int flags;
    private int timeout;
    private int enterAnim;
    private int exitAnim;
    private boolean isInterceptLogin; //是否登录拦截
    private String loginUri; //登录路径

    private static class FRouterHolder {
        private static FRouter instance = new FRouter();
    }

    public static FRouter getInstance(){
        FRouter fRouter = FRouterHolder.instance;
        fRouter.clear();
        return fRouter;
    }

    protected FRouter(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        //mBundle = in.readBundle();
        path = in.readString();
        flags = in.readInt();
        timeout = in.readInt();
        enterAnim = in.readInt();
        exitAnim = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        //dest.writeBundle(mBundle);
        dest.writeString(path);
        dest.writeInt(flags);
        dest.writeInt(timeout);
        dest.writeInt(enterAnim);
        dest.writeInt(exitAnim);
    }

    public static final Creator<FRouter> CREATOR = new Creator<FRouter>() {
        @Override
        public FRouter createFromParcel(Parcel in) {
            return new FRouter(in);
        }

        @Override
        public FRouter[] newArray(int size) {
            return new FRouter[size];
        }
    };

    public FRouter() {
        this(null, null, null);
    }

    public FRouter(String path, Uri uri, Bundle bundle) {
        this.flags = -1;
        this.timeout = 300;
        this.setPath(path);
        this.setUri(uri);
        this.mBundle = null == bundle?new Bundle():bundle;
        this.isInterceptLogin = false;
        this.loginUri = null;
    }

    private void clear(){
        this.mBundle = new Bundle();
        this.path = null;
        this.isInterceptLogin = false;
        this.loginUri = null;
        this.flags = 0;
        this.enterAnim = 0;
        this.exitAnim = 0;
    }

    public Object getTag() {
        return this.tag;
    }

    public FRouter setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Bundle getExtras() {
        return this.mBundle;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getRealEnterAnim() {
        return enterAnim <= 0? R.anim.right_in : enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public int getRealExitAnim() {
        return exitAnim <= 0? R.anim.left_out : exitAnim;
    }


    public int getTimeout() {
        return this.timeout;
    }

    public FRouter setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public FRouter setPath(String path) {
        this.path = path;
        return this;
    }

    public FRouter build(String path) {
        this.path = path;
        return this;
    }

    public String getPath(){
        return path;
    }

    public Uri getUri() {
        return this.uri;
    }

    public FRouter setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    private Class targetActivity;
    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public void navigation(Context context) {
        this.navigation(context, null);
    }

    public void navigation(@NonNull Context context, Class toActivity) {
        if(isInterceptLogin && interceptLogin(context)){
            return;
        }
        if(context instanceof Activity) {
            ((Activity)context).overridePendingTransition(getRealEnterAnim(), getRealExitAnim());
        }
        Intent intent = new Intent();
        intent.putExtra(TAG, this);
        intent.putExtras(mBundle);
        intent.setFlags(flags);
        if(toActivity == null){
            if (targetActivity == null) {
                intent.setClass(context, CommonActivity.class);
            } else {
                intent.setClass(context, targetActivity);
            }
        }else{
            intent.setClass(context, toActivity);
        }
        context.startActivity(intent);
    }

    public void navigation(Activity context, int requestCode) {
        this.navigation(context, null, requestCode);
    }

    public void navigation(@NonNull Activity context, Class toActivity, int requestCode) {
        if(isInterceptLogin && interceptLogin(context)){
            return;
        }
        context.overridePendingTransition(getRealEnterAnim(), getRealExitAnim());
        Intent intent = new Intent();
        intent.putExtra(TAG, this);
        intent.putExtras(mBundle);
        intent.setFlags(flags);
        if(toActivity == null) {
            if (targetActivity == null) {
                intent.setClass(context, CommonActivity.class);
            } else {
                intent.setClass(context, targetActivity);
            }
        }else{
            intent.setClass(context, toActivity);
        }
        context.startActivityForResult(intent, requestCode);
    }


    public FRouter with(Bundle bundle) {
        if(null != bundle) {
            this.mBundle = bundle;
        }

        return this;
    }

    public FRouter withFlags(int flag) {
        this.flags = flag;
        return this;
    }

    public int getFlags() {
        return this.flags;
    }

    public FRouter withString(@Nullable String key, @Nullable String value) {
        this.mBundle.putString(key, value);
        return this;
    }

    public FRouter withBoolean(@Nullable String key, boolean value) {
        this.mBundle.putBoolean(key, value);
        return this;
    }

    public FRouter withShort(@Nullable String key, short value) {
        this.mBundle.putShort(key, value);
        return this;
    }

    public FRouter withInt(@Nullable String key, int value) {
        this.mBundle.putInt(key, value);
        return this;
    }

    public FRouter withLong(@Nullable String key, long value) {
        this.mBundle.putLong(key, value);
        return this;
    }

    public FRouter withDouble(@Nullable String key, double value) {
        this.mBundle.putDouble(key, value);
        return this;
    }

    public FRouter withByte(@Nullable String key, byte value) {
        this.mBundle.putByte(key, value);
        return this;
    }

    public FRouter withChar(@Nullable String key, char value) {
        this.mBundle.putChar(key, value);
        return this;
    }

    public FRouter withFloat(@Nullable String key, float value) {
        this.mBundle.putFloat(key, value);
        return this;
    }

    public FRouter withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        this.mBundle.putCharSequence(key, value);
        return this;
    }

    public FRouter withParcelable(@Nullable String key, @Nullable Parcelable value) {
        this.mBundle.putParcelable(key, value);
        return this;
    }

    public FRouter withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        this.mBundle.putParcelableArray(key, value);
        return this;
    }

    public FRouter withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        this.mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public FRouter withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        this.mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public FRouter withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        this.mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public FRouter withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        this.mBundle.putStringArrayList(key, value);
        return this;
    }

    public FRouter withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        this.mBundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public FRouter withSerializable(@Nullable String key, @Nullable Serializable value) {
        this.mBundle.putSerializable(key, value);
        return this;
    }

    public FRouter withByteArray(@Nullable String key, @Nullable byte[] value) {
        this.mBundle.putByteArray(key, value);
        return this;
    }

    public FRouter withShortArray(@Nullable String key, @Nullable short[] value) {
        this.mBundle.putShortArray(key, value);
        return this;
    }

    public FRouter withCharArray(@Nullable String key, @Nullable char[] value) {
        this.mBundle.putCharArray(key, value);
        return this;
    }

    public FRouter withFloatArray(@Nullable String key, @Nullable float[] value) {
        this.mBundle.putFloatArray(key, value);
        return this;
    }

    public FRouter withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        this.mBundle.putCharSequenceArray(key, value);
        return this;
    }

    public FRouter withBundle(@Nullable String key, @Nullable Bundle value) {
        this.mBundle.putBundle(key, value);
        return this;
    }

    public FRouter withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    public String toString() {
        return "FRouter{uri=" + this.uri + ", tag=" + this.tag + ", mBundle=" + this.mBundle + ", flags=" + this.flags + ", timeout=" + this.timeout + ", provider=" + ", greenChannel=" + ", enterAnim=" + this.enterAnim + ", exitAnim=" + this.exitAnim + "}\n" + super.toString();
    }

    public FRouter setInterceptLogin(){
        return setInterceptLogin(null);
    }

    public FRouter setInterceptLogin(String loginUri) {
        isInterceptLogin = true;
        this.loginUri = loginUri;
        return this;
    }

    private boolean interceptLogin(Context context){
        if(!Constant.logined){
            ARouter.getInstance().build(TextUtils.isEmpty(loginUri)? defaultLoginUrl: loginUri).navigation(context);
            return true;
        }
        return false;
    }

    private String defaultLoginUrl;

    private void setDefault(String loginUrl) {
        this.defaultLoginUrl = loginUrl;
    }

    public void navigationLogin(Context context) {
        navigationLogin(context, null);
    }

    public void navigationLogin(Context context, String url) {
        if (TextUtils.isEmpty(defaultLoginUrl)) {
            return;
        }
        ARouter.getInstance().build(TextUtils.isEmpty(url)? defaultLoginUrl: url).navigation(context);
    }

}

