package com.tenor.android.core.weakref;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * {@link WeakRefHandler} with a {@link Looper#myLooper()}, which is associated with the current thread
 */
public class WeakRefCurrentHandler<CTX> extends WeakRefHandler<CTX> implements IWeakRefObject<CTX> {

    public WeakRefCurrentHandler(@NonNull CTX ctx) {
        super(ctx, Looper.myLooper());
    }

    public WeakRefCurrentHandler(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef, Looper.myLooper());
    }

    public WeakRefCurrentHandler(@NonNull CTX ctx, @Nullable Callback callback) {
        super(ctx, Looper.myLooper(), callback);
    }

    public WeakRefCurrentHandler(@NonNull WeakReference<CTX> weakRef, @Nullable Callback callback) {
        super(weakRef, Looper.myLooper(), callback);
    }
}