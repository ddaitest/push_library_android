package com.qding.push;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by n.d on 2017/5/10.
 */

public class Constants {
    public static boolean DEBUG = true;

    public static final String OS_KEY_HUAWEI = "ro.build.version.emui";
    public static final String OS_KEY_MIUI = "ro.miui.ui.version.name";

    public static final String PROCESS_CHANNEL= ":channel";

    public static final int OS_HUAWEI = 1;
    public static final int OS_MI = 2;
    public static final int OS_OTHER = 0;

    @IntDef({OS_HUAWEI,OS_MI,OS_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OSType {}
}
