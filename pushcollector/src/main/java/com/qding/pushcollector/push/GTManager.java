package com.qding.pushcollector.push;

import android.app.Activity;

import com.igexin.sdk.PushManager;

/**
 * Created by qdhl on 2017/4/19.
 */

public class GTManager {

    public static void init(Activity activity) {
        PushManager.getInstance().initialize(activity.getApplicationContext(), com.qding.pushcollector.push.GTService.class);
        PushManager.getInstance().registerPushIntentService(activity.getApplicationContext(), com.qding.pushcollector.push.GTPushService.class);
    }
}
