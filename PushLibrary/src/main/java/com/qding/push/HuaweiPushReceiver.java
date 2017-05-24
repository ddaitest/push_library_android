package com.qding.push;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;

/**
 * Receiver for Hua wei.
 * Created by daivp.com on 2017/4/17.
 */

public class HuaweiPushReceiver extends PushReceiver {
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        String belongId = extras.getString("belongId");
        String content = "PushReceiver:onToken, token = " + token + ",belongId = " + belongId;
        System.out.println(content);
        Log.e("DDAI_HUAWEI_", content);
//        UpdateHandler.updateToken(1,token);
        PushManager.gotToken(Constants.OS_HUAWEI, token);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String text = new String(msg, "UTF-8");
            Log.e("DDAI_HUAWEI", "PushReceiver:onPushMsg： " + text);
//            UpdateHandler.updateContent(1,"{"+text+"}");
//            for (PushListener listener : PushManager.pushListeners) {
//                listener.onMessage(Constants.OS_HUAWEI, "{" + text + "}");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "PushReceiver:onEvent: " + extras.getString(BOUND_KEY.pushMsgKey);
            Log.e("DDAI_HUAWEI", content);
//            UpdateHandler.updateContent(1,content);
//            for (PushListener listener : PushManager.pushListeners) {
//                listener.onMessage(Constants.OS_HUAWEI, content);
//            }
        }
        super.onEvent(context, event, extras);
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        try {
            String content = "PushReceiver:onPushState： " + (pushState ? "Connected" : "Disconnected");
            Log.e("DDAI_HUAWEI", content);
//            UpdateHandler.updateStatus(1,(pushState ? "_Connected" : "_Disconnected"));
//            for (PushListener listener : PushManager.pushListeners) {
//                listener.updateStatus(Constants.OS_HUAWEI, (pushState ? "_Connected" : "_Disconnected"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
