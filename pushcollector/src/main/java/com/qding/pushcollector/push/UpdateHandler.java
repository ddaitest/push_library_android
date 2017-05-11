package com.qding.pushcollector.push;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qding.pushcollector.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by qdhl on 2017/4/25.
 */

public class UpdateHandler {
    //1=huawei, 2=mi, 3=getui, 4=umeng, 5=xg
    private static SparseArray<TextView> statusViews = new SparseArray<>();
    private static SparseArray<EditText> contentViews = new SparseArray<>();
    public static SparseArray<String> tokens = new SparseArray<>();
    public static SparseArray<String> status = new SparseArray<>();
    private static SparseArray<LinkedList<String>> contents = new SparseArray<>();
    private static Handler handler;
    private static ProgressBar bar;

    public static void init(){
        if (handler==null){
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public static void regContent(EditText content, TextView statusView, int type) {
        contentViews.put(type, content);
        statusViews.put(type, statusView);
        content.setText(getContent(type));
        statusView.setText(status.get(type));
    }

    public static void regProgress(ProgressBar _bar){
        bar = _bar;
    }

    //invoke on view's parent release.
    public static void unreg() {
        statusViews.clear();
        contentViews.clear();
        bar = null;
    }

    private static String getContent(int type){
        LinkedList<String> ll = contents.get(type);
        if (ll==null){
            ll = new LinkedList<>();
            contents.put(type,ll);
            return "";
        }
        if (ll.size()==0){
            return "";
        }
        if (ll.size()>3){
            ll.pollFirst();
        }
        final StringBuffer buffer = new StringBuffer();
        for (String a:ll){
            buffer.append(a);
            buffer.append(" \n");
        }
        int l = buffer.length();
        if (l>2){
            buffer.delete(l-2,l);
        }
        return buffer.toString();
    }

    private static String addContent(int type,String text){
        LinkedList<String> ll = contents.get(type);
        if (ll==null){
            ll = new LinkedList<>();
            contents.put(type,ll);
        }
        ll.offerLast(text);
        if (ll.size()>3){
            ll.pollFirst();
        }
        final StringBuffer buffer = new StringBuffer();
        for (String a:ll){
            buffer.append(a);
            buffer.append(" \n");
        }
        int l = buffer.length();
        if (l>2){
            buffer.delete(l-2,l);
        }
        return buffer.toString();
    }

    public static void updateContent(final int type, final String text) {
        final String content = addContent(type,text);
        init();
        handler.post(new Runnable() {
            @Override
            public void run() {
                EditText view = contentViews.get(type);
                if (view != null) {
                    view.setText(content);
                }
            }
        });

    }

    public static void updateStatus(final int type, final String text) {
        init();
        status.put(type,text);
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView view = statusViews.get(type);
                if (view != null) {
                    view.setText(text);
                }
            }
        });
        if (status.size()>4){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (bar!=null) {
                        bar.setVisibility(View.INVISIBLE);
                    }
                }
            });

        }
    }

    public static void updateToken( int type,  String text) {
        tokens.put(type,text);
        if (type==1||tokens.size()>3){
            report();
        }

    }

    private static void report(){
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
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//                        }
//                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
