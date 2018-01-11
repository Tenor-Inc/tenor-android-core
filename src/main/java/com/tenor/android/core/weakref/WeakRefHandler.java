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

    private final WeakReference<CTX> mWeakRef;

    public WeakRefHandler(@NonNull CTX ctx, @Nullable Looper looper) {
        this(new WeakReference<>(ctx), looper);
    }

    public WeakRefHandler(@NonNull WeakReference<CTX> weakRef, @Nullable Looper looper) {
        this(weakRef, looper, null);
    }

    public WeakRefHandler(@NonNull CTX ctx, @Nullable Looper looper, @Nullable Callback callback) {
        this(new WeakReference<>(ctx), looper, callback);
    }

    public WeakRefHandler(@NonNull WeakReference<CTX> weakRef, @Nullable Looper looper, @Nullable Callback callback) {
        super(looper, callback);
        mWeakRef = weakRef;
        /*
         * This exception is mirror from Handler(Callback callback, boolean async), and
         * the reason for having a @Nullable annotated Looper to throw out RuntimeException are:
         *
         * (1) the constructor of Handler is not annotated with @NonNull even thought its document states
         * its Looper input "must not be null".
         *
         * (2) methods, such as Looper.myLooper() is annotated with @Nullable
         */
        if (looper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }
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
}