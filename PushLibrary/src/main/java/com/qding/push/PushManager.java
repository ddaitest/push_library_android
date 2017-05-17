package com.qding.push;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by n.d on 2017/5/5.
 */

public class PushManager {

    public static void init(Application context) {
        // decide kind of os.
        if (Util.checkProcess(context, context.getPackageName(), Constants.PROCESS_CHANNEL)) {
            // init UM.
            UmengManager.init(context);

        }
    }

    public static void start(Activity context) {
        int type = Util.checkOS(context);
        if (type == Constants.OS_HUAWEI) {
            //active hw
            HuaweiManager.init(context);
        } else if (type == Constants.OS_MI) {
            // active MI
            MiManager.init(context);
        }
    }

    static ArrayList<PushListener> pushListeners = new ArrayList<>();

    public static void reglistener(PushListener listener) {
        if (!pushListeners.contains(listener)) {
            pushListeners.add(listener);
        }
    }

    public static void unreglistener(PushListener listener) {
        pushListeners.remove(listener);
    }

}