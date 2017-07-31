package com.tenor.android.core.network;

import android.app.Application;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractGsonUtils;
import com.tenor.android.core.util.AbstractListUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * Do NOT call {@link ApiService} directly, use its wrapper, {@link ApiClient}
 */
public class ApiService<T> implements IApiService<T> {

    private final T mClient;
    private final String mApiKey;
    private final String mEndpoint;

    public ApiService(Builder<T> builder) {
        mClient = create(builder);
        mApiKey = builder.getApiKey();
        mEndpoint = builder.getEndpoint();
    }

    @Override
    public synchronized T get() {
        return mClient;
    }

    @Override
    public synchronized T create(@NonNull Builder<T> builder) {
        return ApiServiceUtils.create(builder);
    }

    @NonNull
    @Override
    public String getApiKey() {
        return mApiKey;
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return mEndpoint;
    }

    public interface IBuilder<T> extends Serializable {
        Class<T> getCls();

        @NonNull
        Context getContext();

        @NonNull
        String getApiKey();

        @NonNull
        Gson getGson();

        @NonNull
        List<Interceptor> getInterceptors();

        /**
         * Return host endpoint.
         * If in BuildConfig.DEBUG, use the developer servers.
         * Otherwise, use the production servers
         *
         * @return String of the api <b>endpoint host</b>
         */
        @NonNull
        String getEndpoint();

        @NonNull
        String getAppVersionName();

        @IntRange(from = 0, to = 30)
        int getTimeout();

        IBuilder<T> apiKey(@NonNull String apiKey);

        IBuilder<T> gson(@NonNull Gson gson);

        /**
         * Set API protocol type
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param protocol {@link ApiParams#HTTP} or {@link ApiParams#HTTPS}
         */
        IBuilder<T> protocol(@NonNull String protocol);

        /**
         * Set API server
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param serverName server name, default value is "api"
         */
        IBuilder<T> server(@NonNull String serverName);

        /**
         * Set {@link Interceptor}
         *
         * @param interceptor the interceptor
         */
        IBuilder<T> interceptor(@NonNull Interceptor interceptor);

        /**
         * Set {@link List}<{@link Interceptor}>
         *
         * @param interceptors the list of interceptors
         */
        IBuilder<T> interceptors(@NonNull List<Interceptor> interceptors);

        /**
         * Network timeout
         *
         * @param timeout between 0 to {@link ApiParams#TIMEOUT_SLOW_CONNECTION} seconds
         */
        IBuilder<T> timeout(@IntRange(from = 0, to = 30) int timeout);

        /**
         * Set API endpoint directly; however, using {@link #protocol(String)} and
         * {@link #server(String)} are recommended over using this.
         */
        IBuilder<T> endpoint(@NonNull String endpoint);

        IBuilder<T> appVersionName(@NonNull String versionName);

        IApiService<T> build();
    }

    public static class Builder<T> extends ApiParams implements IBuilder<T> {

        private static final long serialVersionUID = -3581428418516126896L;

        @NonNull
        private String mProtocolType = HTTPS;
        @NonNull
        private String mServerName = SERVER_API;
        @NonNull
        private String mEndpoint = StringConstant.EMPTY;

        @IntRange(from = 0, to = 30)
        private int mTimeout = TIMEOUT_FAST_CONNECTION;

        private List<Interceptor> mInterceptors;
        /**
         * This variable is limited to 20 characters
         */
        @NonNull
        private String mAppVersionName = StringConstant.EMPTY;
        @NonNull
        private String mApiKey = StringConstant.EMPTY;
        @NonNull
        private Gson mGson = AbstractGsonUtils.getInstance();

        private final Context mContext;
        private final Class<T> mCls;

        /**
         * Call this on the onCreate() of your subclass of {@link Application}
         */
        public Builder(@NonNull Context context, @NonNull Class<T> cls) {
            mContext = context;
            mCls = cls;
        }

        @NonNull
        @Override
        public Class<T> getCls() {
            return mCls;
        }

        @NonNull
        @Override
        public Context getContext() {
            return mContext;
        }

        @NonNull
        @Override
        public String getApiKey() {
            if (TextUtils.isEmpty(mApiKey)) {
                throw new IllegalStateException("API key cannot be null or empty.");
            }
            return mApiKey;
        }

        @NonNull
        @Override
        public Gson getGson() {
            return mGson;
        }

        @Override
        public IBuilder<T> apiKey(@NonNull String apiKey) {
            if (TextUtils.isEmpty(apiKey)) {
                throw new IllegalStateException("API key cannot be null or empty.");
            }
            mApiKey = apiKey;
            return this;
        }

        @Override
        public IBuilder<T> gson(@NonNull Gson gson) {
            mGson = gson;
            return this;
        }

        /**
         * Set API protocol type
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param protocol {@link #HTTP} or {@link #HTTPS}
         */
        @Override
        public IBuilder<T> protocol(@NonNull String protocol) {
            if (HTTP.equals(protocol) || HTTPS.equals(protocol)) {
                mProtocolType = protocol;
            }
            return this;
        }

        /**
         * Set API server
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param serverName server name, default value is "api"
         */
        @Override
        public IBuilder<T> server(@NonNull String serverName) {
            if (!TextUtils.isEmpty(serverName)) {
                mServerName = serverName;
            }
            return this;
        }

        @NonNull
        @Override
        public List<Interceptor> getInterceptors() {
            if (mInterceptors == null) {
                mInterceptors = new ArrayList<>();
            }
            return mInterceptors;
        }

        /**
         * Set {@link Interceptor}
         *
         * @param interceptor the interceptor
         */
        @Override
        public IBuilder<T> interceptor(@Nullable final Interceptor interceptor) {
            if (interceptor != null) {
                getInterceptors().add(interceptor);
            }
            return this;
        }

        /**
         * Set {@link List}<{@link Interceptor}>
         *
         * @param interceptors the list of interceptors
         */
        @Override
        public IBuilder<T> interceptors(@Nullable final List<Interceptor> interceptors) {
            if (!AbstractListUtils.isEmpty(interceptors)) {
                getInterceptors().addAll(interceptors);
            }
            return this;
        }

        @Override
        public int getTimeout() {
            return mTimeout;
        }

        @Override
        public IBuilder<T> timeout(@IntRange(from = 0, to = 30) int timeout) {
            if (timeout > -1 && mTimeout != timeout) {
                mTimeout = timeout;
            }
            return this;
        }

        /**
         * Return host endpoint.
         * If in BuildConfig.DEBUG, use the developer servers.
         * Otherwise, use the production servers
         *
         * @return String of the api <b>endpoint host</b>
         */
        @NonNull
        @Override
        public String getEndpoint() {
            if (!TextUtils.isEmpty(mEndpoint)) {
                return mEndpoint;
            }
            return String.format(API_ENDPOINT_FORMATTER, mProtocolType, mServerName);
        }

        /**
         * Set API endpoint directly; however, using {@link #protocol(String)} and
         * {@link #server(String)} are recommended over using this.
         */
        @Override
        public IBuilder<T> endpoint(@NonNull final String endpoint) {
            if (!TextUtils.isEmpty(endpoint)) {
                mEndpoint = endpoint;
            }
            return this;
        }

        @Override
        public IBuilder<T> appVersionName(@NonNull String versionName) {
            if (!TextUtils.isEmpty(versionName)) {
                mAppVersionName += versionName;
            }
            return this;
        }

        @NonNull
        @Override
        public String getAppVersionName() {
            return mAppVersionName;
        }

        @Override
        public IApiService<T> build() {
            return new ApiService<>(this);
        }
    }
}
