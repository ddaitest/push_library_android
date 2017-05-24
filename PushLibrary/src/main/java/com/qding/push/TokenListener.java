package com.qding.push;

/**
 * Created by n.d on 2017/5/15.
 */

public interface TokenListener {

    void gotToken(@Constants.OSType int type, String token);

}
