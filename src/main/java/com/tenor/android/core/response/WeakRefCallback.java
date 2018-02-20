package com.tenor.android.core.response;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractIOUtils;
import com.tenor.android.core.weakref.WeakRefObject;
import com.tenor.android.core.weakref.WeakRefRunnable;
import com.tenor.android.core.weakref.WeakRefUiHandler;

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
public abstract class WeakRefCallback<CTX, T> extends WeakRefObject<CTX> implements Callback<T> {

    public static final String ERROR_UNKNOWN = "unknown error";
    public static final String ERROR_NULL_RESPONSE = "response is null";

    private final WeakRefUiHandler<CTX> mUiThread;
    private boolean mReportNetworkDropAsException;

    /**
     * Constructor
     * <p>
     * In case of the view is being recycled, this callback will end gracefully
     *
     * @param ctx the caller view
     */
    public WeakRefCallback(@NonNull CTX ctx) {
        this(new WeakReference<>(ctx));
    }

    public WeakRefCallback(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef);
        mUiThread = new WeakRefUiHandler<>(weakRef);
    }

    protected WeakRefUiHandler<CTX> getUiThread() {
        return mUiThread;
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

    @Override
    public void onResponse(Call<T> call, @Nullable final Response<T> response) {
        if (!hasRef()) {
            return;
        }

        getUiThread().post(new WeakRefRunnable<CTX>(getWeakRef()) {
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
    private static Throwable createThrowable(@Nullable ResponseBody errorBody) {
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
    public final void onFailure(Call<T> call, @Nullable final Throwable throwable) {
        if (!hasRef()) {
            return;
        }

        getUiThread().post(new WeakRefRunnable<CTX>(getWeakRef()) {
            @Override
            public void run(@NonNull CTX ctx) {

                if (throwable == null) {
                    failure(ctx, new Throwable(ERROR_UNKNOWN), null);
                    return;
                }

                if (isReportNetworkDropAsException()) {
                    failure(ctx, throwable, null);
                    return;
                }

                final boolean isNetworkDrop = "canceled".equalsIgnoreCase(throwable.getMessage())
                        || throwable instanceof UnknownHostException;
                if (isNetworkDrop) {
                    onNetworkDropCaught(throwable);
                    return;
                }
                failure(ctx, throwable, null);
            }
        });
    }

    public void onNetworkDropCaught(@NonNull Throwable throwable) {

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
