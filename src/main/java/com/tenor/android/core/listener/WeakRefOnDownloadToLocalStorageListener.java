package com.tenor.android.core.listener;

import android.support.annotation.NonNull;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

/**
 * Abstract class to handle action on write completed in local storage
 */
public abstract class WeakRefOnDownloadToLocalStorageListener<CTX> implements OnDownloadToLocalStorageListener {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefOnDownloadToLocalStorageListener(CTX ctx) {
        this(new WeakReference<>(ctx));
    }

    public WeakRefOnDownloadToLocalStorageListener(WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
    }

    public CTX getCTX() {
        return mWeakRef.get();
    }

    public boolean hasCTX() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    /**
     * This method will be called both on process succeeded and failed
     * <br/>
     * Utilize it to execute tasks that is needed for both scenarios, such dismiss progress dialog
     */
    public void onProcessCompleted(@NonNull CTX ctx) {
        // do nothing
    }

    public abstract void success(@NonNull CTX ctx, @NonNull String path);

    public void failure(@NonNull CTX ctx, @NonNull Throwable throwable) {
        // do nothing
    }

    @Override
    public final void success(@NonNull String path) {
        if (!hasCTX()) {
            return;
        }
        onProcessCompleted(getCTX());
        success(getCTX(), path);
    }

    @Override
    public final void failure(@NonNull Throwable throwable) {
        if (!hasCTX()) {
            return;
        }
        onProcessCompleted(getCTX());
        failure(getCTX(), throwable);
    }
}
