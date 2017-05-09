package com.tenor.android.core.concurrency;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * {@link WeakRefRunnable} with a convenient {@link #runOnUiThread} method to run things on UI thread
 */
public abstract class WeakRefUiRunnable<T> extends WeakRefRunnable<T> {

    private static Handler sUiThread;

    public WeakRefUiRunnable(@Nullable T t) {
        super(t);
    }

    public WeakRefUiRunnable(@NonNull WeakReference<T> weakRef) {
        super(weakRef);
    }

    private static Handler getUiThread() {
        if (sUiThread == null) {
            sUiThread = new Handler(Looper.getMainLooper());
        }
        return sUiThread;
    }

    protected static void runOnUiThread(Runnable runnable) {
        getUiThread().post(runnable);
    }
}
