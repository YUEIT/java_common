package cn.yue.base.common.utils.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yue.base.common.R;
import cn.yue.base.common.activity.BaseFragmentActivity;
import cn.yue.base.common.activity.PermissionCallBack;
import cn.yue.base.common.utils.variable.ResourceUtils;

/**
 * Description : 运行时权限工具
 * Created by yue on 2019/3/11
 */
public class RunTimePermissionUtil {

    public static int REQUEST_CODE = 100;

    public static void requestPermissions(Context context, PermissionCallBack permissionCallBack, String... permissions) {
        if (context instanceof BaseFragmentActivity) {
            requestPermissions((BaseFragmentActivity) context, permissionCallBack, permissions);
        }
    }

    public static void requestPermissions(BaseFragmentActivity context, PermissionCallBack permissionCallBack, String... permissions) {
        requestPermissions(context, REQUEST_CODE, permissionCallBack, permissions);
    }

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

    public static String getPermissionName(String permission) {
        if (permissionMap.isEmpty()) {
            permissionMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, ResourceUtils.getString(R.string.app_permission_write_external_storage));
            permissionMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, ResourceUtils.getString(R.string.app_permission_read_external_storage));
            permissionMap.put(Manifest.permission.READ_PHONE_STATE, ResourceUtils.getString(R.string.app_permission_read_phone_state));
            permissionMap.put(Manifest.permission.CAMERA, ResourceUtils.getString(R.string.app_permission_camera));
            permissionMap.put(Manifest.permission.ACCESS_FINE_LOCATION, ResourceUtils.getString(R.string.app_permission_access_fine_location));
        }
        String permissionName = permissionMap.get(permission);
        if (!TextUtils.isEmpty(permissionName)) {
            return permissionName;
        }
        return "";
    }

    private static final HashMap<String, String> permissionMap = new HashMap<>();
}
