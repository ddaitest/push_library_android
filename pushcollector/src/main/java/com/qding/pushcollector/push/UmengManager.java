package com.qding.pushcollector.push;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by qdhl on 2017/4/21.
 */

public class UmengManager {

    public static void init(Application application) {
        PushAgent mPushAgent = PushAgent.getInstance(application);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("DDAI_Umeng","register.onSuccess()="+deviceToken);
                UpdateHandler.updateStatus(4,"Connected");
//                UpdateHandler.updateContent(4,"{token:"+deviceToken+"}");
                UpdateHandler.updateToken(4,deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("DDAI_Umeng","register.onFailure()="+s+";"+s1);
                UpdateHandler.updateStatus(4,"Failure");
                UpdateHandler.updateContent(4,"{onFailure:"+s1+"}");
            }
        });
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                String log = "{title:"+msg.title+";content:"+msg.text+";extra:"+msg.custom+"}";
                Log.e("DDAI_Umeng", "dealWithCustomMessage = "+log);
                UpdateHandler.updateContent(4,"{"+msg.title+";"+msg.text+";"+msg.custom+"}");
            }
        };
        mPushAgent.setMessageHandler(messageHandler);
    }

    public static void onStart(){
//        PushAgent.getInstance(MyApplication.getContext()).onAppStart();
    }
}
