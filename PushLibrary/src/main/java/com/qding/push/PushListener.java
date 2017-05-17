package com.qding.push;

/**
 * Created by n.d on 2017/5/15.
 */

public interface PushListener {

    void updateStatus(int type, String text);

    void gotToken(int type, String token);

    void onMessage(int type, String content);
}
