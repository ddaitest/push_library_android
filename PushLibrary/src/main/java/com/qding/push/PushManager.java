package com.qding.push;

import android.content.Context;

/**
 * Created by n.d on 2017/5/5.
 */

public class PushManager {

    public static void init(Context context){
        // decide kind of os.
        if (Util.checkProcess(context,context.getPackageName(),Constants.PROCESS_CHANNEL)){
            //TODO init UM.
        }
    }

    public static void init2(Context context){
        int type = Util.checkOS(context);
        if (type == Constants.OS_HUAWEI){
            //TODO active hw
        }else if (type == Constants.OS_MI){
            //TODO active MI

        }
    }

}