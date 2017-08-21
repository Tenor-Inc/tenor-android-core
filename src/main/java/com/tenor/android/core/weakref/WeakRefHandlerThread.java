package com.tenor.android.core.weakref;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.listener.IWeakRefObject;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * {@link HandlerThread} with a {@link WeakReference} on its calling context
 * <p/>
 * This is intended to avoid unintentional leakage on {@link Activity} and {@link android.app.Fragment}
 */
public abstract class WeakRefHandlerThread<CTX, H extends Handler> extends HandlerThread
        implements IWeakRefObject<CTX> {

    private H mHandler;
    private final WeakReference<CTX> mWeakRef;
    private boolean mHandlerPrepared;

    public WeakRefHandlerThread(@NonNull final CTX activity, @NonNull final String id) {
        super(id);
        mWeakRef = new WeakReference<>(activity);
    }

    /**
     * Get {@link Handler}, wrap this method with {@link #isAlive()}
     * <p>
     * This method returns the {@link Handler} associated with this {@link Thread}.
     * If this thread not been started or for any reason is {@link #isAlive()} returns false,
     * this method will return null. If this thread has been started,
     * this method will block until the handler has been initialized.
     *
     * @return The {@link Handler}.
     */
    @Nullable
    public H getHandler() {
        if (!isAlive()) {
            return null;
        }

        // If the thread has been started, wait until the handler has been created.
        synchronized (this) {
            while (isAlive() && mHandler == null) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        return mHandler;
    }

    public boolean isHandlerPrepared() {
        return mHandlerPrepared;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = initHandler(getLooper(), getWeakRef());
        mHandlerPrepared = true;
        onHandlerPrepared();
    }

    protected void onHandlerPrepared() {
    }

    /**
     * Initialize the {@link Handler} that will associate with this {@link HandlerThread}
     *
     * @param looper  the looper of the current prepared {@link WeakRefHandlerThread}
     * @param weakRef the {@link WeakReference} of its calling context
     */
    public abstract H initHandler(@NonNull Looper looper, @NonNull WeakReference<CTX> weakRef);

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

    /**
     * Cancel all existing tasks
     */
    public void cancelAll() {
        getHandler().removeCallbacksAndMessages(null);
    }
}
