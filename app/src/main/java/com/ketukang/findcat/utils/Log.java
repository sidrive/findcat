package com.ketukang.findcat.utils;

import com.ketukang.findcat.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rioswarawan on 12/19/16.
 */

public class Log {

    private static boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String string) {
        if (DEBUG) android.util.Log.i(tag, string);
    }

    public static void d(String tag, String string) {
        if (DEBUG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (DEBUG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (DEBUG) android.util.Log.w(tag, string);
    }

    public static void printStackTrace(Exception ex) {
        if (DEBUG) {
            StringWriter writer = new StringWriter();
            ex.printStackTrace(new PrintWriter(writer));
            Log.e("Log", writer.toString());
        }
    }

    /**
     * Always show regardless of debug or release build
     */
    public static void e(String tag, String string) {
        android.util.Log.e(tag, string);
    }

    public static void e(String tag, String string, Throwable t) {
        android.util.Log.e(tag, string, t);
    }
}
