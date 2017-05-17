package com.qding.push;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

/**
 * Manager for pack all functions.
 * <p>
 * Created by qdhl on 2017/4/17.
 */

public class HuaweiManager {
    static HuaweiApiClient client;
    private static boolean mResolvingError = false;
    private static Activity ac;
//    public static String TOKEN = "";

    public static void init(Activity activity) {
        ac = activity;
        //创建了一个基本权限的登录参数HuaweiIdSignInOptions：
        client = new HuaweiApiClient.Builder(activity).addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(listener).addOnConnectionFailedListener(failedListener).build();
        client.connect();
//        UpdateHandler.updateStatus(1,"Connecting...");
    }

    public static void connect(Activity ac) {
//        Log.e("DDAI_HW", "onStart, ErrorCode: " + HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(ac));
        if (client != null) {
            client.connect();
        }
    }

    private static HuaweiApiClient.ConnectionCallbacks listener = new HuaweiApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected() {
            Log.e("DDAI_HUAWEI", "ConnectionCallbacks.onConnected()");
//            UpdateHandler.updateStatus(1,"Connected");
            for (PushListener listener:PushManager.pushListeners){
                listener.updateStatus(Constants.OS_HUAWEI,"Connected");
            }
            getToken();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.e("DDAI_HUAWEI", "ConnectionCallbacks.onConnectionSuspended(i=" + i + ")");
//            UpdateHandler.updateStatus(1,"ConnectionSuspended:"+i);
            for (PushListener listener:PushManager.pushListeners){
                listener.updateStatus(Constants.OS_HUAWEI,"ConnectionSuspended");
            }
        }
    };

    private static HuaweiApiClient.OnConnectionFailedListener failedListener = new HuaweiApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e("DDAI_HUAWEI", "OnConnectionFailedListener.onConnectionFailed()=" + connectionResult.getErrorCode());
//            UpdateHandler.updateStatus(1,"ConnectionFailed:"+connectionResult.getErrorCode());
            if (mResolvingError) {
                return;
            }
            final int errorCode = connectionResult.getErrorCode();
            final HuaweiApiAvailability availability = HuaweiApiAvailability.getInstance();
            if (availability.isUserResolvableError(errorCode)) {
                mResolvingError = true;
                availability.resolveError(ac, errorCode, 1001, new HuaweiApiAvailability.OnUpdateListener() {
                    @Override
                    public void onUpdateFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e("DDAI_HUAWEI", "OonUpdateFailed=" + connectionResult.getErrorCode());
//                        UpdateHandler.updateStatus(1,"UpdateFailed:"+connectionResult.getErrorCode());
                    }
                });
            }
        }
    };

    public static void getToken() {
        if (!isConnected()) {
            return;
        }
        // 异步调用方式
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

            @Override
            public void onResult(TokenResult result) {
                Log.e("DDAI_HUAWEI", "getToken=" + result.getTokenRes().getToken());
            }

        });
    }

    public static void getState() {
        if (!isConnected()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                // 状态结果通过广播返回
                HuaweiPush.HuaweiPushApi.getPushState(client);
            }
        }.start();
    }

    public static boolean isConnected() {
        boolean result = (client != null && client.isConnected());
        Log.e("DDAI_HUAWEI", "isConnected = " + result);
        return result;
    }

    public static void setPayload(boolean flag) {
        HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(client, flag);
    }
}
