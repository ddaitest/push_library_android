package com.qding.pushcollector;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.daivp.pushcollector.R;

public class ParserActivity extends Activity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parser);
        tv = (TextView) findViewById(R.id.textView);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                finish();
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            StringBuilder builder = new StringBuilder();
            String message = intent.getStringExtra("notify_hw");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_hw=");
                builder.append(message);
                builder.append(";");
            }
            message = intent.getStringExtra("notify_mi");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_mi=");
                builder.append(message);
                builder.append(";");
            }
            message = intent.getStringExtra("notify_um");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_um=");
                builder.append(message);
                builder.append(";");
            }

            tv.setText(builder.toString());
        }
    }
}
