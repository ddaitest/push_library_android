package com.qding.pushcollector;

import android.annotation.SuppressLint;
import android.app.Application;

import com.qding.pushcollector.push.UmengManager;
import com.qding.pushcollector.push.XXX;

/**
 * Created by qdhl on 2017/4/21.
 */

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Application app;

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        XXX.test1(this);
        if (XXX.check(this, "com.daivp.pushcollector:channel")) {
            UmengManager.init(this);
        }
    }

    public static Application getContext() {
        return app;
    }
}
