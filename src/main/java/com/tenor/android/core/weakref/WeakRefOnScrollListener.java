package com.tenor.android.core.weakref;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.tenor.android.core.listener.IWeakRefObject;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public class WeakRefOnScrollListener<CTX> extends RecyclerView.OnScrollListener
        implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefOnScrollListener(@NonNull CTX ctx) {
        super();
        mWeakRef = new WeakReference<>(ctx);
    }

    @Override
    @Nullable
    public CTX getRef() {
        return mWeakRef.get();
    }

    @Override
    @NonNull
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }
}
