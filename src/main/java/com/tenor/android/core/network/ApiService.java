package com.tenor.android.core.network;

import android.app.Application;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.network.constant.Protocol;
import com.tenor.android.core.util.AbstractGsonUtils;
import com.tenor.android.core.util.AbstractListUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Do NOT call {@link ApiService} directly, use its wrapper, {@link ApiClient}
 */
public class ApiService<T> implements IApiService<T> {

    private final T mClient;
    private final String mApiKey;
    private final String mEndpoint;

    protected ApiService(Builder<T> builder) {
        mClient = create(builder);
        mApiKey = builder.apiKey;
        mEndpoint = builder.endpoint;
    }

    @Override
    public synchronized T get() {
        return mClient;
    }

    @Override
    public synchronized T create(@NonNull Builder<T> builder) {
        Context ctx = builder.context;
        if (!(ctx instanceof Application)) {
            ctx = ctx.getApplicationContext();
        }

        final File cacheDir = new File(ctx.getCacheDir().getAbsolutePath(), ctx.getPackageName());
        final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        OkHttpClient.Builder http = new OkHttpClient.Builder()
                .cache(cache)
                .writeTimeout(builder.timeout, TimeUnit.SECONDS);

        for (Interceptor interceptor : builder.interceptors) {
            http.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(builder.endpoint)
                .client(http.build())
                .addConverterFactory(GsonConverterFactory.create(builder.gson))
                .build();

        return retrofit.create(builder.cls);
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return mEndpoint;
    }


    @NonNull
    @Override
    public String getApiKey() {
        if (TextUtils.isEmpty(mApiKey)) {
            throw new IllegalStateException("API key cannot be null or empty.");
        }
        return mApiKey;
    }

    public interface IBuilder<T> extends Serializable {
        IBuilder<T> apiKey(@NonNull String apiKey);

        IBuilder<T> gson(@NonNull Gson gson);

        /**
         * Set API protocol type
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param protocol {@link Protocol.Value}
         */
        IBuilder<T> protocol(@Protocol.Value String protocol);

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
         * @param timeout between 0 to 30 seconds
         */
        IBuilder<T> timeout(@IntRange(from = 0, to = 30) int timeout);

        /**
         * Set API endpoint directly; however, using {@link #protocol(String)} and
         * {@link #server(String)} are recommended over using this.
         */
        IBuilder<T> endpoint(@NonNull String endpoint);

        IApiService<T> build();
    }

    public static class Builder<T> implements IBuilder<T> {

        private static final long serialVersionUID = -3581428418516126896L;

        protected static final String API_ENDPOINT_FORMATTER = "%1$s://%2$s.tenor.com/v1/";
        protected static final String SERVER_NAME = "api";

        @Protocol.Value
        private String protocol = Protocol.HTTPS;
        @NonNull
        private String serverName = SERVER_NAME;
        @NonNull
        private String endpoint = String.format(API_ENDPOINT_FORMATTER, protocol, serverName);
        @IntRange(from = 0, to = 30)
        private int timeout = 15;
        @NonNull
        private List<Interceptor> interceptors = new ArrayList<>();
        @NonNull
        private String apiKey = StringConstant.EMPTY;
        @NonNull
        private Gson gson = AbstractGsonUtils.getInstance();

        private final Context context;
        private final Class<T> cls;

        /**
         * Call this on the onCreate() of your subclass of {@link Application}
         */
        public Builder(@NonNull Context context, @NonNull Class<T> cls) {
            this.context = context;
            this.cls = cls;
        }

        @Override
        public IBuilder<T> apiKey(@NonNull String apiKey) {
            if (TextUtils.isEmpty(apiKey)) {
                throw new IllegalStateException("API key cannot be null or empty.");
            }
            this.apiKey = apiKey;
            return this;
        }

        @Override
        public IBuilder<T> gson(@NonNull Gson gson) {
            this.gson = gson;
            return this;
        }

        /**
         * Set API protocol type
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param protocol {@link Protocol}
         */
        @Override
        public IBuilder<T> protocol(@Protocol.Value String protocol) {
            this.protocol = protocol;
            this.endpoint = String.format(API_ENDPOINT_FORMATTER, this.protocol, this.serverName);
            return this;
        }

        /**
         * Set API server
         * <p>
         * Call this on the onCreate() of your Application
         *
         * @param server server name, default value is "api"
         */
        @Override
        public IBuilder<T> server(@NonNull String server) {
            this.serverName = !TextUtils.isEmpty(server) ? server : SERVER_NAME;
            this.endpoint = String.format(API_ENDPOINT_FORMATTER, this.protocol, this.serverName);
            return this;
        }

        /**
         * Set {@link Interceptor}
         *
         * @param interceptor the interceptor
         */
        @Override
        public IBuilder<T> interceptor(@NonNull Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        /**
         * Set {@link List}<{@link Interceptor}>
         *
         * @param interceptors the list of interceptors
         */
        @Override
        public IBuilder<T> interceptors(@NonNull List<Interceptor> interceptors) {
            if (!AbstractListUtils.isEmpty(interceptors)) {
                this.interceptors.addAll(interceptors);
            }
            return this;
        }

        @Override
        public IBuilder<T> timeout(@IntRange(from = 0, to = 30) int timeout) {
            if (timeout >= 0 && timeout <= 30 && this.timeout != timeout) {
                this.timeout = timeout;
            }
            return this;
        }

        /**
         * Set API endpoint directly; however, using {@link #protocol(String)} and
         * {@link #server(String)} are recommended over using this.
         */
        @Override
        public IBuilder<T> endpoint(@NonNull String endpoint) {
            if (!TextUtils.isEmpty(endpoint)) {
                this.endpoint = endpoint;
            }
            return this;
        }

        @Override
        public IApiService<T> build() {
            return new ApiService<>(this);
        }
    }
}
