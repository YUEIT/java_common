package cn.yue.base.common.activity;

/**
 * Description :
 * Created by yue on 2018/11/12
 */
public interface PermissionCallBack {
    void requestSuccess(String permission);
    void requestFailed(String permission);
}
