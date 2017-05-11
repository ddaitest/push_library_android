package com.qding.push;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.qding.push.Constants;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by n.d on 2017/5/2.
 */

public class Util {

    /**
     * Find OS info, Determine which system belong to.
     * @param context application context.
     * @return One of {@link Constants#OS_HUAWEI},{@link Constants#OS_MI}, or {@link Constants#OS_OTHER}
     */
    @Constants.OSType
    public static int checkOS(Context context) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
            Method methodGet = SystemProperties.getMethod("get", String.class);
            String os = (String) methodGet.invoke(SystemProperties, Constants.OS_KEY_HUAWEI);
            if (!TextUtils.isEmpty(os)) {
                return Constants.OS_HUAWEI;
            }
            os = (String) methodGet.invoke(SystemProperties, Constants.OS_KEY_MIUI);
            if (!TextUtils.isEmpty(os)) {
                return Constants.OS_MI;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constants.OS_OTHER;
    }

    /**
     * check current process, return True if exist in given processNames.
     * @param context application context.
     * @param processNames filters.
     * @return True if exist in given processNames.
     */
    public static boolean checkProcess(Context context, String... processNames) {
        int pid = android.os.Process.myPid();//获取进程pid
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);//获取系统的ActivityManager服务
        ActivityManager.RunningAppProcessInfo myInfo = null;
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                myInfo = appProcess;
            }
        }
        int s = -1;
        if (myInfo != null) {
            s = Arrays.binarySearch(processNames, myInfo.processName);
        }
        if (Constants.DEBUG) {
            Log.w("DDAI_MAN", myInfo == null ? "TEST ERROR" : "PROCESS NAME: " + myInfo.processName + "; PID:" + myInfo.pid + "; s=" + s);
        }
        return s >= 0;
    }
}
