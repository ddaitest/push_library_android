package com.qding.pushcollector.push;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by n.d on 2017/5/8.
 */

public class XXX {
    public static boolean TEST = false;

//    final private static String filter[] = {};

    public static boolean check(Context context, String... filters) {
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
            s = Arrays.binarySearch(filters, myInfo.processName);
            Log.w("DDAI_MAN", "APP NAME: " + myInfo.processName + "; PID:" + myInfo.pid + "; s=" + s);
        } else {
            Log.w("DDAI_MAN", "TEST ERROR");

        }
        return s >= 0;
    }

    public static void test1(Context context) {
//        return checkOS(context, "com.daivp.pushcollector", "com.daivp.pushcollector:channel");
        int pid = android.os.Process.myPid();//获取进程pid
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);//获取系统的ActivityManager服务
        ActivityManager.RunningAppProcessInfo myInfo = null;
        for (ActivityManager.RunningAppProcessInfo appProcess : am.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                myInfo = appProcess;
            }
        }
        if (myInfo != null) {
            Log.w("DDAI_MAN", "APP NAME: " + myInfo.processName + "; PID:" + myInfo.pid);
        } else {
            Log.w("DDAI_MAN", "TEST ERROR");

        }
    }


}
