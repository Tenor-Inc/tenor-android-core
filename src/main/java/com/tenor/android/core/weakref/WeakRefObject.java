package com.tenor.android.core.weakref;


import android.support.annotation.NonNull;

import com.tenor.android.core.util.CoreWeakReferenceUtils;

import java.lang.ref.WeakReference;

public class WeakRefObject<CTX> implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefObject(@NonNull final CTX ctx) {
        this(new WeakReference<>(ctx));
    }

    public WeakRefObject(@NonNull final WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
    }

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return CoreWeakReferenceUtils.isAlive(mWeakRef);
    }
}
