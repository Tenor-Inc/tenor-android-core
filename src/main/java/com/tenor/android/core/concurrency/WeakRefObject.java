package com.tenor.android.core.concurrency;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.listener.IWeakRefObject;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class WeakRefObject<CTX> implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefObject(@NonNull final CTX ctx) {
        this(new WeakReference<>(ctx));
    }

    public WeakRefObject(@NonNull final WeakReference<CTX> weakRef) {
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
}
