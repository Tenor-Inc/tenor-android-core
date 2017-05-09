package com.tenor.android.core.response;

import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * A Class extends {@link WeakRefCallback} with a weak reference on the given {@link ResultReceiver}
 *
 * @param <R> the type of response
 * @param <S> the type of calling service
 */
public abstract class ServiceReceiverWeakRefCallback<R, S> extends WeakRefCallback<R, S> {

    private final WeakReference<ResultReceiver> mWeakRefReceiver;

    public ServiceReceiverWeakRefCallback(@NonNull final S service,
                                          @NonNull final ResultReceiver receiver) {
        super(service);
        mWeakRefReceiver = new WeakReference<>(receiver);
    }

    @Override
    public final void success(@NonNull S service, R response) {
        if (AbstractWeakReferenceUtils.isAlive(mWeakRefReceiver)) {
            success(mWeakRefReceiver.get(), service, response);
        }
    }

    @Override
    public final void failure(@NonNull S service, BaseError error) {
        if (AbstractWeakReferenceUtils.isAlive(mWeakRefReceiver)) {
            failure(mWeakRefReceiver.get(), service, error);
        }
    }

    public abstract void success(@NonNull ResultReceiver receiver,
                                 @NonNull S service, @Nullable R response);

    public abstract void failure(@NonNull ResultReceiver receiver,
                                 @NonNull S service, @Nullable BaseError error);
}
