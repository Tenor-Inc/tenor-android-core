package com.tenor.android.core.response;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.util.AbstractGsonUtils;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.weakref.WeakRefRunnable;

import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A {@link Callback} with {@link WeakReference} associated with the caller {@link Context}
 * <p>
 * In case of the view is being recycled, this callback will end gracefully
 */
public abstract class WeakRefCallback<CTX, T> implements Callback<T> {

    private static final String UNKNOWN_ERROR = "unknown error";
    private final WeakReference<CTX> mWeakRef;
    private static Handler sUiThread;

    /**
     * Constructor
     * <p>
     * In case of the view is being recycled, this callback will end gracefully
     *
     * @param ctx the caller view
     */
    public WeakRefCallback(@NonNull final CTX ctx) {
        this(new WeakReference<>(ctx));
    }

    public WeakRefCallback(@NonNull final WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
    }

    protected static Handler getUiThread() {
        if (sUiThread == null) {
            sUiThread = new Handler(Looper.getMainLooper());
        }
        return sUiThread;
    }

    /**
     * Override this method to conduct general network performance measures
     */
    public void measureResponse(@NonNull CTX ctx, @Nullable okhttp3.Response rawResponse) {

    }

    private void runOnUiThread(WeakRefRunnable<CTX> runnable) {
        if (AbstractWeakReferenceUtils.isAlive(mWeakRef)) {
            getUiThread().post(runnable);
        }
    }

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        runOnUiThread(new WeakRefRunnable<CTX>(mWeakRef) {
            @Override
            public void run(@NonNull CTX ctx) {
                if (response == null) {
                    failure(ctx, new BaseError(UNKNOWN_ERROR), null);
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    success(ctx, response.body(), response.raw());
                } else {
                    failure(ctx, processError(response.errorBody()), response.raw());
                }
            }
        });
    }

    private BaseError processError(ResponseBody errorBody) {
        if (errorBody == null) {
            return new BaseError(UNKNOWN_ERROR);
        }

        try {
            if (!TextUtils.isEmpty(errorBody.string())) {
                return AbstractGsonUtils.getInstance().fromJson(errorBody.string(), BaseError.class);
            }
        } catch (Throwable ignored) {
        }
        return new BaseError(UNKNOWN_ERROR);
    }

    @Override
    public final void onFailure(Call<T> call, final Throwable throwable) {
        runOnUiThread(new WeakRefRunnable<CTX>(mWeakRef) {
            @Override
            public void run(@NonNull CTX CTX) {
                if (throwable != null && "canceled".equalsIgnoreCase(throwable.getMessage())) {
                    return;
                }

                final String errorMessage = throwable != null && !TextUtils.isEmpty(throwable.getMessage())
                        ? throwable.getMessage() : UNKNOWN_ERROR;
                failure(CTX, new BaseError(errorMessage), null);
            }
        });
    }

    public abstract void success(@NonNull CTX ctx, @Nullable T response);

    public abstract void failure(@NonNull CTX ctx, @Nullable BaseError error);

    private void success(@NonNull CTX ctx, @Nullable T response, @NonNull okhttp3.Response rawResponse) {
        try {
            measureResponse(ctx, rawResponse);
        } catch (Throwable ignored) {
        }
        success(ctx, response);
    }

    private void failure(@NonNull CTX ctx, @Nullable BaseError error, @Nullable okhttp3.Response rawResponse) {
        try {
            measureResponse(ctx, rawResponse);
        } catch (Throwable ignored) {
        }
        failure(ctx, error);
    }
}
