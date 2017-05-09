package com.tenor.android.core.concurrency;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class WeakRefCountDownTimer<CTX> extends CountDownTimer {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefCountDownTimer(@NonNull final CTX ctx,
                                 long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mWeakRef = new WeakReference<>(ctx);
    }

    public WeakRefCountDownTimer(@NonNull final WeakReference<CTX> weakRef,
                                 long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        mWeakRef = weakRef;
    }

    @Nullable
    protected WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    protected boolean isRefAlive() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }
}
