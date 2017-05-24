package com.qding.push;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by qdhl on 2017/4/21.
 */

public class UmengManager {

    public static void init(final Application application) {
        PushAgent mPushAgent = PushAgent.getInstance(application);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("DDAI_Umeng","register.onSuccess()="+deviceToken);
//                UpdateHandler.updateStatus(4,"Connected");
//                UpdateHandler.updateContent(4,"{token:"+deviceToken+"}");
//                UpdateHandler.updateToken(4,deviceToken);
//                for (PushListener listener:PushManager.pushListeners){
//                    listener.updateStatus(Constants.OS_OTHER,"Connected");
//                }
                PushManager.gotToken(Constants.OS_OTHER,deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("DDAI_Umeng","register.onFailure()="+s+";"+s1);
                PushManager.gotToken(Constants.OS_OTHER,"");
//                UpdateHandler.updateStatus(4,"Failure");
//                UpdateHandler.updateContent(4,"{onFailure:"+s1+"}");
//                for (PushListener listener:PushManager.pushListeners){
//                    listener.updateStatus(Constants.OS_OTHER,"Failure");
//                }
            }
        });
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                String log = "{title:"+msg.title+";content:"+msg.text+";extra:"+msg.custom+"}";
                Log.e("DDAI_Umeng", "dealWithCustomMessage = "+log);
//                UpdateHandler.updateContent(4,"{"+msg.title+";"+msg.text+";"+msg.custom+"}");
//                for (PushListener listener:PushManager.pushListeners){
//                    listener.onMessage(Constants.OS_OTHER,"{"+msg.title+";"+msg.text+";"+msg.custom+"}");
//                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
        mPushAgent.setNotificationClickHandler(new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                Log.e("DDAI_Umeng", "dealWithCustomAction = "+uMessage.custom);
                Util.callActivity(application,"notify_um",uMessage.custom);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("QDPUSH://com.qding.push/parser"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("notify_um",uMessage.custom);
//                String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
//                Log.e("DDAI_URI",intentUri);
//                application.startActivity(intent);
            }
        });
    }

    public static void onStart(){
//        PushAgent.getInstance(MyApplication.getContext()).onAppStart();
    }
}
