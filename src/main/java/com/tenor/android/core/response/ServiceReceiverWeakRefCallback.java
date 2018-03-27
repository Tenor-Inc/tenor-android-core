package com.tenor.android.core.response;

import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.CoreWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * A Class extends {@link WeakRefCallback} with a weak reference on the given {@link ResultReceiver}
 *
 * @param <T> the type of response
 * @param <CTX> the type of calling service
 */
public abstract class ServiceReceiverWeakRefCallback<CTX, T> extends WeakRefCallback<CTX, T> {

    private final WeakReference<ResultReceiver> mWeakRefReceiver;

    public ServiceReceiverWeakRefCallback(@NonNull final CTX service,
                                          @NonNull final ResultReceiver receiver) {
        super(service);
        mWeakRefReceiver = new WeakReference<>(receiver);
    }

    @Override
    public final void success(@NonNull CTX service, T response) {
        if (CoreWeakReferenceUtils.isAlive(mWeakRefReceiver)) {
            success(mWeakRefReceiver.get(), service, response);
        }
    }

    @Override
    public final void failure(@NonNull CTX service, Throwable throwable) {
        if (CoreWeakReferenceUtils.isAlive(mWeakRefReceiver)) {
            failure(mWeakRefReceiver.get(), service, throwable);
        }
    }

    public abstract void success(@NonNull ResultReceiver receiver,
                                 @NonNull CTX service, @Nullable T response);

    public abstract void failure(@NonNull ResultReceiver receiver,
                                 @NonNull CTX service, @Nullable Throwable throwable);
}
