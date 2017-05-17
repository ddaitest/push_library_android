package com.qding.push;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Manager for pack all functions.
 * Created by qdhl on 2017/4/18.
 */

public class MiManager {

    public static void init(Activity activity) {
        try {
            ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(),
                    PackageManager.GET_META_DATA);
            String appId = String.valueOf(appInfo.metaData.get("MI_APP_ID"));
            String appKey = String.valueOf(appInfo.metaData.get("MI_APP_KEY"));
            Log.d("Tag", " appId = " + appId+" ; appKey = "+appKey);
            MiPushClient.registerPush(activity.getApplicationContext(), appId, appKey);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        UpdateHandler.updateStatus(2,"Connecting...");
    }
}
