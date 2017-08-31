package com.tenor.android.core.util;

import android.support.annotation.NonNull;
import android.util.Log;

public abstract class AbstractLogUtils {

    public static boolean sDebuggable = false;

    public static boolean isDebuggable() {
        return sDebuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        sDebuggable = debuggable;
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param caller the {@link Object} class
     * @param msg    The message you would like logged.
     */
    public static int e(@NonNull Object caller, @NonNull String msg) {
        return isDebuggable() ? Log.e(caller.getClass().getCanonicalName(), msg) : 0;
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(@NonNull String tag, @NonNull String msg) {
        return isDebuggable() ? Log.e(tag, msg) : 0;
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     *
     * @param caller the {@link Object} class
     * @param msg    The message you would like logged.
     * @param tr     An exception to log
     */
    public static int e(@NonNull Object caller, @NonNull String msg, @NonNull Throwable tr) {
        return isDebuggable() ? e(caller.getClass().getCanonicalName(), msg, tr) : 0;
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {
        return isDebuggable() ? Log.e(tag, msg, tr) : 0;
    }
}
