package com.tenor.android.core.weakref;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * {@link Handler} with a {@link WeakReference} on its calling context
 * <p/>
 * This is intended to avoid unintentional leakage on {@link Activity} and {@link android.app.Fragment}
 */
public class WeakRefHandler<CTX> extends Handler implements IWeakRefObject<CTX> {

    private static Handler sUiThread;
    private final WeakReference<CTX> mWeakRef;

    public WeakRefHandler(@NonNull final Looper looper,
                          @NonNull final CTX ctx) {
        super(looper);
        mWeakRef = new WeakReference<>(ctx);
    }

    public WeakRefHandler(@NonNull final Looper looper,
                          @NonNull final WeakReference<CTX> weakRef) {
        super(looper);
        mWeakRef = weakRef;
    }

    @Nullable
    @Override
    public CTX getRef() {
        return mWeakRef.get();
    }

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    private static Handler getUiThread() {
        if (sUiThread == null) {
            sUiThread = new Handler(Looper.getMainLooper());
        }
        return sUiThread;
    }

    /**
     * Run {@link Runnable} on UI Thread
     *
     * @param runnable the {@link Runnable}
     */
    protected static void runOnUiThread(@Nullable final Runnable runnable) {
        if (runnable != null) {
            getUiThread().post(runnable);
        }
    }
}