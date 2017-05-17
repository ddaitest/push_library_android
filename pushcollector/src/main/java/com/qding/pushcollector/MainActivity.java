package com.qding.pushcollector;

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
import com.qding.push.PushManager;
import com.qding.pushcollector.push.GTManager;
import com.qding.push.HuaweiManager;
import com.qding.push.MiManager;
import com.qding.push.UmengManager;
import com.qding.pushcollector.push.UpdateHandler;
import com.qding.pushcollector.push.XGManager;
import com.qding.pushcollector.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PushManager.touch(getApplicationContext());
//                UmengManager.init(getApplication());
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                intent.putExtra("ABC", "aaa123");
//                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
//                builder.setSmallIcon(R.mipmap.ic_launcher).setContentText("CCCCC").setContentTitle("TTT").setContentIntent(pendingIntent);
//                notificationManager.notify(1, builder.build());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("QDPUSH://com.qding.push/parser"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("abc","000");
                String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
                Log.e("DDAI_URI",intentUri);
                startActivity(intent);
            }
        });
        et1 = (EditText) findViewById(R.id.et_1);
        et2 = (EditText) findViewById(R.id.et_2);
        et4 = (EditText) findViewById(R.id.et_4);
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv4 = (TextView) findViewById(R.id.tv_4);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        etIntent = (EditText) findViewById(R.id.et_intent);
        //init push sdk
        UpdateHandler.init();
//        HuaweiManager.init(this);
//        MiManager.init(this);
        PushManager.start(this);
//        GTManager.init(this);
//        XGManager.init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        HuaweiManager.connect(this);
        UmengManager.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            String abc = intent.getStringExtra("ABC");
            if (!TextUtils.isEmpty(abc)) {
                etIntent.setText(etIntent.getText().append(abc + ";"));
            }
        }
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
