package com.tenor.android.core.concurrency;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * {@link Runnable} with a {@link WeakReference} on its calling context
 * <p/>
 * This is intended to avoid unintentional leakage on {@link Activity} and {@link android.app.Fragment}
 */
public abstract class WeakRefRunnable<T> implements Runnable {

    private final WeakReference<T> mWeakRef;
    private boolean mConsumed;

    /**
     * @param t the type of caller
     */
    public WeakRefRunnable(@Nullable final T t) {
        mWeakRef = new WeakReference<>(t);
    }

    public WeakRefRunnable(@NonNull final WeakReference<T> weakRef) {
        mWeakRef = weakRef;
    }

    @Override
    public void run() {
        if (isRefAlive()) {
            run(mWeakRef.get());
            onRunCompleted();
        }
    }

    @Nullable
    protected WeakReference<T> getWeakRef() {
        return mWeakRef;
    }

    protected boolean isRefAlive() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    public boolean isConsumed() {
        return mConsumed;
    }

    public abstract void run(@NonNull T t);

    @CallSuper
    public void onRunCompleted() {
        mConsumed = true;
    }
}
