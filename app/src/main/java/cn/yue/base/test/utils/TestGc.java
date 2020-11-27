package cn.yue.base.test.utils;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class TestGc {

    private final static String TAG = "TestGc";

    private static WeakReference<Activity> activityWeakReference;
    private static WeakReference<Fragment> fragmentWeakReference;
    public static void setActivityWeakReference(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    public static void setFragmentWeakReference(Fragment fragment) {
        fragmentWeakReference = new WeakReference<>(fragment);
    }

    public static void show() {
        if (activityWeakReference != null) {
            Log.d(TAG, "cache activity: " + activityWeakReference.get());
        } else {
            Log.d(TAG, "cache activity: " + null);
        }
        if (fragmentWeakReference != null) {
            Log.d(TAG, "cache fragment: " + fragmentWeakReference.get());
        } else {
            Log.d(TAG, "cache fragment: ");
        }
    }

    public static void startTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                show();
            }
        }, 0, 5000);
    }

    public static void gc() {
        Runtime.getRuntime().gc();
    }
}
