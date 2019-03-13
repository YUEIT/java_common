package cn.yue.base.common.utils.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cn.yue.base.common.activity.BaseActivity;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.activity.PermissionCallBack;

/**
 * Description :
 * Created by yue on 2018/11/12
 */
public class RunTimePermissionUtil {

    public static int REQUEST_CODE = 100;

    public static void requestPermissions(BaseFragmentActivity context, int requestCode, PermissionCallBack permissionCallBack, String... permissions) {
        //检查权限是否授权
        context.setPermissionCallBack(permissionCallBack);
        if (shouldShowRequestPermissionRationale(context, permissions)) {
            context.showFailDialog();
        }
        if(RunTimePermissionUtil.checkPermissions(context, permissions)) {
            if(permissionCallBack != null) {
                for(String permission : permissions) {
                    permissionCallBack.requestSuccess(permission);
                }
            }
        } else {
            ActivityCompat.requestPermissions(context, getNeedRequestPermissions(context, permissions), requestCode);
        }
    }

    public static void requestPermissions(BaseActivity context, int requestCode, PermissionCallBack permissionCallBack, String... permissions) {
        //检查权限是否授权
        context.setPermissionCallBack(permissionCallBack);
        if (shouldShowRequestPermissionRationale(context, permissions)) {
            context.showFailDialog();
        }
        if(RunTimePermissionUtil.checkPermissions(context, permissions)) {
            if(permissionCallBack != null) {
                for(String permission : permissions) {
                    permissionCallBack.requestSuccess(permission);
                }
            }
        } else {
            ActivityCompat.requestPermissions(context, getNeedRequestPermissions(context, permissions), requestCode);
        }
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public static boolean checkPermissions(Activity context, String[] permissions) {
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M) {
            return true;
        }
        for(String permission:permissions) {
            if(ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限是否被拒绝过
     * @param context
     * @param permissions
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity context, String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取需要请求的权限
     * @param permissions
     * @return
     */
    public static String[] getNeedRequestPermissions(Activity context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }
}
