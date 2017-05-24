package com.qding.pushcollector;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daivp.pushcollector.R;
import com.qding.push.Constants;
import com.qding.push.PushListener;
import com.qding.push.PushManager;
import com.qding.push.TokenListener;
import com.qding.pushcollector.push.GTManager;
import com.qding.push.HuaweiManager;
import com.qding.push.MiManager;
import com.qding.push.UmengManager;
import com.qding.pushcollector.push.UpdateHandler;
import com.qding.pushcollector.push.XGManager;
import com.qding.pushcollector.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends Activity {
    public static Context context;
    //
    TextView tv1, tv2, tv3, tv4;
    EditText et1, et2, et3, et4, et5, etIntent;
    //    TextView tvs1,tvs2,tvs3,tvs4,tvs5;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        et1 = (EditText) findViewById(R.id.et_1);
        et2 = (EditText) findViewById(R.id.et_2);
        et4 = (EditText) findViewById(R.id.et_4);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv4 = (TextView) findViewById(R.id.tv_4);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        etIntent = (EditText) findViewById(R.id.et_intent);
        UpdateHandler.init();


        //init push sdk
        PushManager.onCreate(this, new TokenListener() {
            @Override
            public void gotToken(@Constants.OSType int type, String token) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        PushManager.onStart(this);
    }

    @Override
    protected void onResume() {
        UpdateHandler.regContent(et1, tv1, Constants.OS_HUAWEI);
        UpdateHandler.regContent(et2, tv2, Constants.OS_MI);
        UpdateHandler.regContent(et4, tv4, Constants.OS_OTHER);
        UpdateHandler.regProgress(bar);
        super.onResume();
    }

    @Override
    protected void onStop() {
        UpdateHandler.unreg();
        super.onStop();
    }


}
