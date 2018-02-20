package com.tenor.android.core.weakref;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class WeakRefCountDownTimer<CTX> extends CountDownTimer implements IWeakRefObject<CTX> {

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

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    @Override
    public final void onTick(long millisUntilFinished) {
        if (!hasRef()) {
            cancel();
            return;
        }
        //noinspection ConstantConditions
        onTick(getWeakRef().get(), millisUntilFinished);
    }

    @Override
    public final void onFinish() {
        if (!hasRef()) {
            cancel();
            return;
        }
        //noinspection ConstantConditions
        onFinish(getWeakRef().get());
    }

    public abstract void onTick(@NonNull CTX ctx, long millisUntilFinished);

    public abstract void onFinish(@NonNull CTX ctx);
}
