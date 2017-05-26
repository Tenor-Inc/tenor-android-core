package com.tenor.android.core.concurrency;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class WeakRefOnClickListener<CTX> implements View.OnClickListener {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefOnClickListener(@Nullable final CTX ctx) {
        mWeakRef = new WeakReference<>(ctx);
    }

    public WeakRefOnClickListener(@NonNull final WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
    }


    @Nullable
    protected WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    protected boolean isRefAlive() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    @Override
    public void onClick(View v) {
        if (isRefAlive()) {
            //noinspection ConstantConditions
            onClick(getWeakRef().get(), v);
        }
    }

    public abstract void onClick(@NonNull CTX ctx, @NonNull View v);
}
