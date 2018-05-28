package com.chou.android.mediaplayerlibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chou.android.mediaplayerlibrary.BuildConfig;

/**
 * Created by 眼神 on 2018/5/19.
 * 在debug状态下打印log
 */

public class LogUtil {
    private static final String TAG = "ChouVideoPlayer";

    public static void i(String message) {
        if (BuildConfig.DEBUG) android.util.Log.i(TAG, TextUtils.isEmpty(message)?"data is null":message);
    }
    public static void e( String message) {
        if (BuildConfig.DEBUG) android.util.Log.e(TAG, TextUtils.isEmpty(message)?"data is null":message);
    }
    public static void d( String message) {
        if (BuildConfig.DEBUG) android.util.Log.d(TAG, TextUtils.isEmpty(message)?"data is null":message);
    }
    public static void v( String message) {
        if (BuildConfig.DEBUG) android.util.Log.v(TAG, TextUtils.isEmpty(message)?"data is null":message);
    }
    public static void w( String message) {
        if (BuildConfig.DEBUG) android.util.Log.w(TAG, TextUtils.isEmpty(message)?"data is null":message);
    }
}
