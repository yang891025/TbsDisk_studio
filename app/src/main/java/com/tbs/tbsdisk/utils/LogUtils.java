package com.tbs.tbsdisk.utils;

import android.util.Log;

/**
 * 日志工具类
 * Created by EchoGe on 16/4/5.
 */
public class LogUtils
{
    private LogUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String TAG = LogUtils.class.getSimpleName();

    public static void i(String msg)
    {
        if (Settings.isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (Settings.isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg)
    {
        if (Settings.isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (Settings.isDebug)
            Log.v(TAG, msg);
    }

    public static void i(String tag, String msg)
    {
        if (Settings.isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (Settings.isDebug)
            Log.i(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (Settings.isDebug)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (Settings.isDebug)
            Log.i(tag, msg);
    }

}
