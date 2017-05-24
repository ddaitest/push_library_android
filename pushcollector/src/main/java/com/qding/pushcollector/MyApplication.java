package com.qding.pushcollector;

import android.annotation.SuppressLint;
import android.app.Application;

import com.qding.push.PushManager;
import com.qding.push.UmengManager;
import com.qding.pushcollector.push.XXX;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application app;

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        PushManager.init(this);
    }

    public static Application getContext() {
        return app;
    }
}
