package com.fanneng.android.web.utils;

import android.util.Log;

import com.fanneng.android.web.BuildConfig;
import com.fanneng.android.web.SuperWebX5Config;

/**
 * Log工具类
 */
public class LogUtils {

    private static final String PREFIX = " superwebX5 ---> ";

    public static boolean isDebug() {
        return SuperWebX5Config.DEBUG;
    }

    public static void i(String tag, String message) {

        if (isDebug())
            Log.i(PREFIX.concat(tag), message);
    }

    public static void v(String tag, String message) {

        if (isDebug())
            Log.v(PREFIX.concat(tag), message);

    }

    public static void safeCheckCrash(String tag, String msg, Throwable tr) {
        if (isDebug()) {
            throw new RuntimeException(PREFIX.concat(tag) + " " + msg, tr);
        } else {
            Log.e(PREFIX.concat(tag), msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    public static void e(String tag, String message) {

        if (BuildConfig.DEBUG)
            Log.e(PREFIX.concat(tag), message);
    }
}
