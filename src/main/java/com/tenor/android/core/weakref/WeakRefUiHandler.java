package com.tenor.android.core.weakref;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * {@link WeakRefHandler} with a {@link Looper#getMainLooper()}, which is associated with the UI thread
 */
public class WeakRefUiHandler<CTX> extends WeakRefHandler<CTX> implements IWeakRefObject<CTX> {

    /**
     * @param ctx {@link CTX} must be in {@link @UiThread}
     */
    public WeakRefUiHandler(@NonNull CTX ctx) {
        super(ctx, Looper.getMainLooper());
    }

    public WeakRefUiHandler(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef, Looper.getMainLooper());
    }

    public WeakRefUiHandler(@NonNull CTX ctx, @Nullable Callback callback) {
        super(ctx, Looper.getMainLooper(), callback);
    }

    public WeakRefUiHandler(@NonNull WeakReference<CTX> weakRef, @Nullable Callback callback) {
        super(weakRef, Looper.getMainLooper(), callback);
    }
}