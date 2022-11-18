package cn.yue.test.utils;

import androidx.annotation.Keep;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Description :
 * Created by yue on 2022/11/7
 */

public class DesopxNative {

    static {
        try {
            System.loadLibrary("desopx_native");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public static native boolean auth();

    public static Object invokeMethod(String className, String methodName, Object[] args) {
        try {
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();
            Object result;
            if (args == null || args.length == 0) {
                Method method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                result = method.invoke(object, args);
            } else {
                Class<?>[] parameterTypes = new Class[args.length];
                for (int i = 0; i< args.length; i++) {
                    Object param = args[i];
                    parameterTypes[i] = param.getClass();
                }
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                result = method.invoke(object, args);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Keep
    public static boolean testClassLoader(String param) {
        try {
            Class.forName(param);
            return true;
        } catch (ClassNotFoundException e) {
            return e.getMessage() == null;
        }
    }

    @Keep
    public static boolean testException(String params) {
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] arrayOfStackTraceElement = e.getStackTrace();
            for (StackTraceElement s : arrayOfStackTraceElement) {
                if (s.getClassName() != null) {
                    for (String param : params.split(";")) {
                        if (s.getClassName().contains(param)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    @Keep
    public static boolean testStackTrace(String param) {
        try {
            Method method = Throwable.class.getDeclaredMethod(param);
            return Modifier.isNative(method.getModifiers());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
}
