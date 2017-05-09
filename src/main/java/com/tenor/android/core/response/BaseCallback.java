package com.tenor.android.core.response;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Use this class with application context
 * <p/>
 * Use this callback only for calls that you want to persist, such as configuration, background upload
 * <p/>
 * This method is <b>NOT</b> view safe meaning it will produce null pointer exception if it is used with views/presenters
 */
public abstract class BaseCallback<T> implements Callback<T> {
    protected static final String UNKNOWN_ERROR = "unknown error";
    private static Gson mGsonParser = new GsonBuilder().create();

    private static Handler sUiThread;

    protected static Handler getHandler() {
        if (sUiThread == null) {
            sUiThread = new Handler(Looper.getMainLooper());
        }
        return sUiThread;
    }

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (response == null) {
                    failure(processError(null), null);
                } else if (response.isSuccessful() && response.body() != null) {
                    success(response.body(), response.raw());
                } else {
                    failure(processError(response.errorBody()), response.raw());
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
    public void onFailure(Call<T> call, final Throwable throwable) {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (throwable != null && "canceled".equalsIgnoreCase(throwable.getMessage())) {
                    return;
                }

                final String errorMessage = throwable != null && !TextUtils.isEmpty(throwable.getMessage())
                        ? throwable.getMessage() : UNKNOWN_ERROR;
                failure(new BaseError(errorMessage), null);
            }
        });
    }

    public abstract void success(T response);

    public abstract void failure(BaseError error);

    public void success(T response, @NonNull okhttp3.Response rawResponse) {
        try {
            measureResponse(rawResponse);
        } catch (Throwable ignored) {
        }
        success(response);
    }

    public void failure(BaseError error, @Nullable okhttp3.Response rawResponse) {
        try {
            measureResponse(rawResponse);
        } catch (Throwable ignored) {
        }
        failure(error);
    }

    /**
     * Override this method to conduct general network performance measures
     */
    public void measureResponse(@Nullable okhttp3.Response rawResponse) {

    }
}
