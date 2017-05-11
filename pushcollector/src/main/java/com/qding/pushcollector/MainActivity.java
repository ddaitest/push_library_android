package com.qding.pushcollector;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qding.pushcollector.push.GTManager;
import com.qding.push.HuaweiManager;
import com.qding.pushcollector.push.MiManager;
import com.qding.pushcollector.push.UmengManager;
import com.qding.pushcollector.push.UpdateHandler;
import com.qding.pushcollector.push.XGManager;
import com.qding.pushcollector.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    //
//    TextView tv1,tv2,tv3,tv4;
    EditText et1,et2,et3,et4,et5;
    TextView tvs1,tvs2,tvs3,tvs4,tvs5;
    ProgressBar bar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.qding.pushcollector.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.qding.pushcollector.R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(com.qding.pushcollector.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PushManager.touch(getApplicationContext());
                UmengManager.init(getApplication());

            }
        });
        et1 = (EditText) findViewById(R.id.et_1);
        et2 = (EditText) findViewById(R.id.et_2);
        et3 = (EditText) findViewById(R.id.et_3);
        et4 = (EditText) findViewById(R.id.et_4);
        et5 = (EditText) findViewById(R.id.et_5);
        tvs1 = (TextView) findViewById(R.id.tv_1s);
        tvs2 = (TextView) findViewById(R.id.tv_2s);
        tvs3 = (TextView) findViewById(R.id.tv_3s);
        tvs4 = (TextView) findViewById(R.id.tv_4s);
        tvs5 = (TextView) findViewById(R.id.tv_5s);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        //init push sdk
        UpdateHandler.init();
        HuaweiManager.init(this);
        MiManager.init(this);
        GTManager.init(this);
        XGManager.init();
    }

    public static Context context() {
        return context;
    }

    private void fabClick() {
        new Thread() {
            @Override
            public void run() {
                SparseArray<String> tokens = UpdateHandler.tokens;
                HashMap<String, String> map = new HashMap<>();
                map.put("hw", tokens.get(1));
                map.put("mi", tokens.get(2));
                map.put("gt", tokens.get(3));
                map.put("umeng", tokens.get(4));
                Log.e("DDAI",String.format("HW=%s;MI=%s;GT=%s;UM=%s",tokens.get(1),tokens.get(2),tokens.get(3),tokens.get(4)));
                String url = "http://daivp.com/queen/register";
                try {
                    final String result = HttpUtil.post(url).setParam(map).open().getString();
                    Log.e("DDAI", result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        HuaweiManager.connect(this);
        UmengManager.onStart();
    }

    @Override
    protected void onResume() {
        UpdateHandler.regContent(et1,tvs1,1);
        UpdateHandler.regContent(et2,tvs2,2);
        UpdateHandler.regContent(et3,tvs3,3);
        UpdateHandler.regContent(et4,tvs4,4);
        UpdateHandler.regContent(et5,tvs5,5);
        UpdateHandler.regProgress(bar);
        super.onResume();
    }

    @Override
    protected void onStop() {
        UpdateHandler.unreg();
        super.onStop();
    }


}
