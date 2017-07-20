package com.guster.rxandroiddemo;

import android.util.Log;

/**
 * Created by Gusterwoei on 7/20/17.
 */

public class Util {
    private static final String TAG = "RX";

    public static void logd(String s) {
        Log.d(TAG, s);
    }

    public static void loge(Throwable e) {
        Log.d(TAG, "", e);
    }
}
