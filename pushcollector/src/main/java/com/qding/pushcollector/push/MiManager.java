package com.qding.pushcollector.push;

import android.app.Activity;

import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Manager for pack all functions.
 * Created by qdhl on 2017/4/18.
 */

public class MiManager {

    public static void init(Activity activity){
        MiPushClient.registerPush(activity.getApplicationContext(), "2882303761517568046", "5661756891046");
        UpdateHandler.updateStatus(2,"Connecting...");
    }
}
