package com.tenor.android.core.weakref;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * {@link Runnable} with a {@link WeakReference} on its calling context
 * <p/>
 * This is intended to avoid unintentional leakage on {@link Activity} and {@link android.app.Fragment}
 */
public abstract class WeakRefRunnable<T> extends WeakRefObject<T> implements Runnable {

    private boolean mConsumed;

    /**
     * @param t the type of caller
     */
    public WeakRefRunnable(@Nullable final T t) {
        this(new WeakReference<>(t));
    }

    public WeakRefRunnable(@NonNull final WeakReference<T> weakRef) {
        super(weakRef);
    }

    @Override
    public void run() {
        if (hasRef()) {
            //noinspection ConstantConditions
            run(getWeakRef().get());
            onRunCompleted();
        }
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
