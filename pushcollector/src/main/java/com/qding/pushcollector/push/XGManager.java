package com.qding.pushcollector.push;

import android.util.Log;

import com.qding.pushcollector.MyApplication;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by qdhl on 2017/5/2.
 */

public class XGManager {
    public static void init() {
        // 开启logcat输出，方便debug，发布时请关闭
// XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
// 具体可参考详细的开发指南
// 传递的参数为ApplicationContext
        XGPushManager.registerPush(MyApplication.getContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.e("DDAI_XG","onSuccess "+i);
                UpdateHandler.updateStatus(5,"Connected");
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.e("DDAI_XG","onFail "+i+" ;"+s);
                UpdateHandler.updateStatus(5,s);
            }
        });
    }
}
