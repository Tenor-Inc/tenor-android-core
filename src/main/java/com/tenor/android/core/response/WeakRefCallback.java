package com.tenor.android.core.response;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractIOUtils;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.weakref.WeakRefRunnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

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

    public static final String ERROR_UNKNOWN = "unknown error";
    public static final String ERROR_NULL_RESPONSE = "response is null";

    private final WeakReference<CTX> mWeakRef;
    private static Handler sUiThread;
    private boolean mReportNetworkDropAsException;

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

    public void setReportNetworkDropAsException(boolean report) {
        mReportNetworkDropAsException = report;
    }

    public boolean isReportNetworkDropAsException() {
        return mReportNetworkDropAsException;
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
                    failure(ctx, new NullPointerException(ERROR_NULL_RESPONSE), null);
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    success(ctx, response.body(), response.raw());
                } else {
                    failure(ctx, createThrowable(response.errorBody()), response.raw());
                }
            }
        });
    }

    @NonNull
    private static String parseError(@NonNull InputStream is) {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ignored) {
        } finally {
            AbstractIOUtils.close(bufferedReader);
        }
        return sb.toString();
    }

    @NonNull
    private Throwable createThrowable(@Nullable ResponseBody errorBody) {
        if (errorBody == null) {
            return new Throwable(ERROR_UNKNOWN);
        }

        String message = StringConstant.EMPTY;
        try {
            message = errorBody.string();
        } catch (Throwable ignored) {
        }

        if (!TextUtils.isEmpty(message)) {
            return new Throwable(message);
        }

        try {
            message = parseError(errorBody.byteStream());
        } catch (Throwable ignored) {
        }

        if (!TextUtils.isEmpty(message)) {
            return new Throwable(message);
        }
        return new Throwable(ERROR_UNKNOWN);
    }

    @Override
    public final void onFailure(Call<T> call, final Throwable throwable) {
        runOnUiThread(new WeakRefRunnable<CTX>(mWeakRef) {
            @Override
            public void run(@NonNull CTX CTX) {

                if (throwable == null) {
                    failure(CTX, new Throwable(ERROR_UNKNOWN), null);
                    return;
                }

                if (isReportNetworkDropAsException()) {
                    failure(CTX, throwable, null);
                    return;
                }

                final boolean isNetworkDrop = "canceled".equalsIgnoreCase(throwable.getMessage())
                        || throwable instanceof UnknownHostException;
                if (isNetworkDrop) {
                    onNetworkDropCatched(throwable);
                    return;
                }
                failure(CTX, throwable, null);
            }
        });
    }

    public void onNetworkDropCatched(@NonNull Throwable throwable) {

    }

    public abstract void success(@NonNull CTX ctx, @Nullable T response);

    public abstract void failure(@NonNull CTX ctx, @Nullable Throwable throwable);

    private void success(@NonNull CTX ctx, @Nullable T response, @NonNull okhttp3.Response rawResponse) {
        try {
            measureResponse(ctx, rawResponse);
        } catch (Throwable ignored) {
        }
        success(ctx, response);
    }

    private void failure(@NonNull CTX ctx, @Nullable Throwable throwable, @Nullable okhttp3.Response rawResponse) {
        try {
            measureResponse(ctx, rawResponse);
        } catch (Throwable ignored) {
        }
        failure(ctx, throwable);
    }
}
