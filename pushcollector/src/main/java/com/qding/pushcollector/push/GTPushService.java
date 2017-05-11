package com.qding.pushcollector.push;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

/**
 * Created by qdhl on 2017/4/19.
 */

public class GTPushService extends GTIntentService{

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.e("DDAI_GT","onReceiveServicePid="+pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String payload = new String(msg.getPayload());
        String log = "{"+payload+"}";
        Log.e("DDAI_GT","onReceiveMessageData="+log);
        UpdateHandler.updateContent(3,log);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e("DDAI_GT", "onReceiveClientId -> " + "clientid = " + clientid);
        UpdateHandler.updateStatus(3,"Connected");
        UpdateHandler.updateToken(3,clientid);
//        UpdateHandler.updateContent(3,"{token:"+clientid+"}");
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.e("DDAI_GT","onReceiveOnlineState="+online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e("DDAI_GT","onReceiveCommandResult="+cmdMessage);
    }
}
