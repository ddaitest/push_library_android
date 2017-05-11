package com.qding.pushcollector.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.igexin.sdk.GTServiceManager;

/**
 * Created by qdhl on 2017/4/19.
 */

public class GTService extends Service {
    @Override
    public void onCreate() {
        Log.e("DDAI_GT","onCreate");
        super.onCreate();
        GTServiceManager.getInstance().onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DDAI_GT","onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {Log.e("DDAI_GT","onBind");
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("DDAI_GT","onDestroy");
        super.onDestroy();
        GTServiceManager.getInstance().onDestroy();
    }

    @Override
    public void onLowMemory() {
        Log.e("DDAI_GT","onLowMemory");
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
