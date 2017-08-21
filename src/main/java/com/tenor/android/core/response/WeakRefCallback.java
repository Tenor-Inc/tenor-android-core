package com.tenor.android.core.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tenor.android.core.weakref.WeakRefRunnable;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.view.IBaseView;

import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A {@link BaseCallback} with {@link WeakReference} associated with the caller view
 * <p>
 * This class is view safe and it is preferred over A {@link BaseCallback} for classes
 * implement {@link IBaseView} or any of its sub-interfaces.
 * <p>
 * In case of the view is being recycled, this callback will end gracefully
 */
public abstract class WeakRefCallback<T, P> extends BaseCallback<T> {
    private static Gson mGsonParser = new GsonBuilder().create();
    private final WeakReference<P> mWeakRef;

    /**
     * Constructor for a <b>view safe</b> {@link BaseCallback}
     * <p>
     * In case of the view is being recycled, this callback will end gracefully
     *
     * @param view the caller view
     */
    public WeakRefCallback(@Nullable final P view) {
        mWeakRef = new WeakReference<>(view);
    }

    public WeakRefCallback(@Nullable final WeakReference<P> weakRef) {
        mWeakRef = weakRef;
    }

    private void runOnUiThread(WeakRefRunnable<P> runnable) {
        if (AbstractWeakReferenceUtils.isAlive(mWeakRef)) {
            getHandler().post(runnable);
        }
    }

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        runOnUiThread(new WeakRefRunnable<P>(mWeakRef) {
            @Override
            public void run(@NonNull P p) {
                if (response == null) {
                    failure(p, new BaseError(UNKNOWN_ERROR), null);
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    success(p, response.body(), response.raw());
                } else {
                    failure(p, processError(response.errorBody()), response.raw());
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
                return mGsonParser.fromJson(errorBody.string(), BaseError.class);
            }
        } catch (Throwable ignored) {
        }
        return new BaseError(UNKNOWN_ERROR);
    }

    @Override
    public final void onFailure(Call<T> call, final Throwable throwable) {
        runOnUiThread(new WeakRefRunnable<P>(mWeakRef) {
            @Override
            public void run(@NonNull P p) {
                if (throwable != null && "canceled".equalsIgnoreCase(throwable.getMessage())) {
                    return;
                }

                final String errorMessage = throwable != null && !TextUtils.isEmpty(throwable.getMessage())
                        ? throwable.getMessage() : UNKNOWN_ERROR;
                failure(p, new BaseError(errorMessage), null);
            }
        });
    }

    public abstract void success(@NonNull P view, @Nullable T response);

    public abstract void failure(@NonNull P view, @Nullable BaseError error);

    public void success(@NonNull P view, @Nullable T response, @NonNull okhttp3.Response rawResponse) {
        try {
            measureResponse(rawResponse);
        } catch (Throwable ignored) {
        }
        success(view, response);
    }

    public void failure(@NonNull P view, @Nullable BaseError error, @Nullable okhttp3.Response rawResponse) {
        try {
            measureResponse(rawResponse);
        } catch (Throwable ignored) {
        }
        failure(view, error);
    }

    @Override
    public final void success(T response) {
        // block non-view safe implementation
    }

    @Override
    public final void failure(BaseError error) {
        // block non-view safe implementation
    }

    @Override
    public final void success(T response, @NonNull okhttp3.Response rawResponse) {
        // block non-view safe implementation
    }

    @Override
    public final void failure(BaseError error, @Nullable okhttp3.Response rawResponse) {
        // block non-view safe implementation
    }
}
