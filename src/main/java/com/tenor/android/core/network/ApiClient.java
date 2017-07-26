package com.tenor.android.core.network;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.listener.IAnonIdListener;
import com.tenor.android.core.measurable.MeasurableViewHolderEvent;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.BaseCallback;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.AnonIdResponse;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.core.service.AaidService;
import com.tenor.android.core.util.AbstractGsonUtils;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.util.AbstractLocaleUtils;
import com.tenor.android.core.util.AbstractSessionUtils;
import com.tenor.android.core.util.AbstractUIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API Client to make network calls to retrieve contents
 */
public abstract class ApiClient {

    /**
     * Connection timeout in seconds
     */
    public static final int TIMEOUT_SLOW_CONNECTION = 30;

    /**
     * Connection timeout in seconds
     */
    public static final int TIMEOUT_FAST_CONNECTION = 15;

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String SERVER_API = "api";

    private static final String DEFAULT_API_ENDPOINT = "https://api.tenor.co/v1/";
    private static final String API_ENDPOINT_FORMATTER = "%1$s://%2$s.tenor.co/v1/";
    private static final String GSON_SKIP_PACKAGE_REALM = "io.realm.RealmObject";

    @NonNull
    private static String sProtocolType = HTTPS;
    @NonNull
    private static String sServerName = SERVER_API;
    @NonNull
    private static String sApiKey = StringConstant.EMPTY;
    @IntRange(from = 0, to = 30)
    private static int sTimeout = TIMEOUT_FAST_CONNECTION;

    private static volatile IApiClient sApiClient;
    @NonNull
    private static List<Interceptor> sInterceptors = new ArrayList<>();
    private static Gson sGson;
    private static String sEndpoint;

    /*
     * Server limits this variable to 20 characters
     * TODO: coordinate with backend to increase this limit both on the server app and database
     */
    private static String sAppVersionName = StringConstant.EMPTY;

    public static void setAppVersionName(String versionName) {
        if (!TextUtils.isEmpty(versionName)) {
            sAppVersionName += versionName;
        }
    }

    public static String getAppVersionName() {
        return sAppVersionName;
    }

    /**
     * Set API key
     * <p>
     * Call this on the onCreate() of your Application
     *
     * @param apiKey api key for production
     */
    public static void setApiKey(String apiKey) {
        if (!TextUtils.isEmpty(apiKey)) {
            sApiKey = apiKey;
        }
    }

    public static String getApiKey() {
        return sApiKey;
    }

    /**
     * Set API protocol type
     * <p>
     * Call this on the onCreate() of your Application
     *
     * @param protocolType {HTTP|HTTPS}
     */
    public static void setProtocolType(String protocolType) {
        if (HTTP.equals(protocolType) || HTTPS.equals(protocolType)) {
            sProtocolType = protocolType;
            sApiClient = null;
        }
    }

    /**
     * Set API server
     * <p>
     * Call this on the onCreate() of your Application
     *
     * @param serverName server name, default value is "api"
     */
    public static void setServer(String serverName) {
        if (!TextUtils.isEmpty(serverName)) {
            sServerName = serverName;
            sApiClient = null;
        }
    }

    /**
     * Initialize the ApiClient instance with your custom interceptor
     *
     * @param context the context
     */
    public static synchronized void init(@NonNull final Context context) {
        sApiClient = createApiClient(context.getApplicationContext(), IApiClient.class);

        if (!AbstractSessionUtils.hasAnonId(context)) {
            getAnonId(context, null);
        }
    }

    /**
     * Set {@link Interceptor}
     *
     * @param interceptor the interceptor
     */
    public static void setInterceptor(@Nullable final Interceptor interceptor) {
        if (interceptor != null) {
            sInterceptors.add(interceptor);
        }
    }

    /**
     * Set {@link List}<{@link Interceptor}>
     *
     * @param interceptors the list of interceptors
     */
    public static void setInterceptors(@Nullable final List<Interceptor> interceptors) {
        if (!AbstractListUtils.isEmpty(interceptors)) {
            sInterceptors.addAll(interceptors);
        }
    }

    /**
     * Remove {@link Interceptor}
     *
     * @param interceptor the interceptor
     */
    public static void removeInterceptor(@Nullable final Interceptor interceptor) {
        if (interceptor != null) {
            sInterceptors.remove(interceptor);
        }
    }

    /**
     * Clear {@link List}<{@link Interceptor}>
     */
    public static void clearInterceptors() {
        sInterceptors.clear();
    }

    /**
     * Retrieve instance of the {@link ApiClient}, and create instance if not already created
     *
     * @param context the context
     * @return the {@link ApiClient} instance
     */
    public static synchronized IApiClient getInstance(@NonNull Context context) {
        if (sApiClient == null) {
            sApiClient = createApiClient(context.getApplicationContext(), IApiClient.class);
        }
        return sApiClient;
    }

    /**
     * Return host endpoint.
     * If in BuildConfig.DEBUG, use the developer servers.
     * Otherwise, use the production servers
     *
     * @return String of the api <b>endpoint host</b>
     */
    public static String getEndpoint(@Nullable final Context context) {

        if (context == null) {
            return DEFAULT_API_ENDPOINT;
        }

        if (!TextUtils.isEmpty(sEndpoint)) {
            return sEndpoint;
        }
        return String.format(API_ENDPOINT_FORMATTER, sProtocolType, sServerName);
    }

    /**
     * Set API endpoint directly; however, using {@link #setProtocolType(String)} and
     * {@link #setServer(String)} are recommended over using this.
     */
    public static void setEndpoint(@NonNull final String endpoint) {
        if (!TextUtils.isEmpty(endpoint)) {
            sEndpoint = endpoint;
            sApiClient = null;
        }
    }

    protected static <T> T createApiClient(@NonNull final Context context,
                                           @NonNull final Class<T> cls) {

        if (!(context instanceof Application)) {
            throw new IllegalArgumentException("Please use application context to avoid memory leak");
        }

        final File cacheDir = new File(context.getCacheDir().getAbsolutePath(), context.getPackageName());
        final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .writeTimeout(sTimeout, TimeUnit.SECONDS);

        if (!AbstractListUtils.isEmpty(sInterceptors)) {
            for (Interceptor interceptor : sInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getEndpoint(context))
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();

        return retrofit.create(cls);
    }

    protected static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return GSON_SKIP_PACKAGE_REALM.equals(f.getDeclaringClass().getName());
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> cls) {
                            return false;
                        }
                    })
                    .create();
        }
        return sGson;
    }

    /**
     * Set connection timeout
     *
     * @param timeout time in second
     */
    public static void setConnectionTimeout(int timeout) {
        if (timeout > -1 && sTimeout != timeout) {
            sTimeout = timeout;
            sApiClient = null;
        }
    }

    /**
     * Get a non-id to roughly identify a user for a better content delivery experience
     *
     * @param context  the application context
     * @param listener the callback when the asynchronous keyboard id API request is done
     * @return {@link Call}<{@link AnonIdResponse}>
     */
    public static Call<AnonIdResponse> getAnonId(@NonNull final Context context,
                                                 @Nullable final IAnonIdListener listener) {
        // request for new keyboard id
        Call<AnonIdResponse> call = ApiClient.getInstance(context)
                .getAnonId(ApiClient.getApiKey(), AbstractLocaleUtils.getCurrentLocaleName(context));

        call.enqueue(new BaseCallback<AnonIdResponse>() {
            @Override
            public void success(AnonIdResponse response) {
                if (response != null && !TextUtils.isEmpty(response.getId())) {
                    AbstractSessionUtils.setAnonId(context, response.getId());

                    if (listener == null) {
                        return;
                    }

                    if (TextUtils.isEmpty(response.getId())) {
                        listener.onReceiveAnonIdFailed(
                                new BaseError("keyboard id cannot be " + response.getId()));
                    }
                    listener.onReceiveAnonIdSucceeded(response.getId());
                }
            }

            @Override
            public void failure(BaseError error) {
                if (listener != null) {
                    listener.onReceiveAnonIdFailed(error);
                }
            }
        });

        // request for aaid
        Intent mServiceIntent = new Intent(context, AaidService.class);
        mServiceIntent.setAction(AaidService.ACTION_GET_AAID);
        context.startService(mServiceIntent);

        return call;
    }

    /**
     * Get service ids that can delivery a more accurate and better experience
     *
     * @return a {@link Map} with {@code key} (API Key), {@code anon_id},
     * {@code aaid} (Android Advertise Id) and {@code locale } for authentication and better
     * content delivery experience
     */
    public static Map<String, String> getServiceIds(@NonNull final Context context) {
        final ArrayMap<String, String> map = new ArrayMap<>(4);

        // API Key
        map.put("key", getApiKey());

        /*
         * The following fields work together to delivery a more accurate and better experience
         *
         * 1. `anon_id`, a non-id or its older version, keyboard is used to roughly identify a user;
         * 2. `aaid`, Android Advertise Id, is used in case "keyboardid" or "anon_id" mutates
         * 3. `locale` is used to deliver curated language/regional specific contents to users
         * 4. `screen_density` is used to optimize the content size to the device
         */
        final String id = AbstractSessionUtils.getAnonId(context);
        map.put(id.length() <= 20 ? "keyboardid" : "anon_id", id);
        map.put("aaid", AbstractSessionUtils.getAndroidAdvertiseId(context));
        map.put("locale", AbstractLocaleUtils.getCurrentLocaleName(context));
        map.put("screen_density", AbstractUIUtils.getScreenDensity(context));
        return map;
    }

    /**
     * Report shared gif id for better search experience in the future
     *
     * @param context the application context
     * @param id      the gif id
     * @return {@link Call}<{@link GifsResponse}>
     */
    public static Call<GifsResponse> registerShare(@NonNull final Context context,
                                                   @NonNull final String id) {
        Call<GifsResponse> call = ApiClient.getInstance(context)
                .registerShare(getServiceIds(context), Integer.valueOf(id));

        call.enqueue(new BaseCallback<GifsResponse>() {
            @Override
            public void success(GifsResponse response) {
                // do nothing
            }

            @Override
            public void failure(BaseError error) {
                // do nothing
            }
        });
        return call;
    }

    /**
     * @param context  the application context
     * @param sourceId the source id of a {@link Result}
     * @param action   the action {share|tap}
     * @return {@link Call}<{@link Void}>
     */
    public static Call<Void> registerGmeAction(@NonNull final Context context,
                                               @NonNull String sourceId,
                                               @NonNull String visualPosition,
                                               @NonNull final String action) {

        final MeasurableViewHolderEvent event = new MeasurableViewHolderEvent(sourceId, action, AbstractLocaleUtils.getUtcOffset(context), visualPosition);

        Call<Void> call = ApiClient.getInstance(context)
                .registerAction(getServiceIds(context), event);

        call.enqueue(new BaseCallback<Void>() {
            @Override
            public void success(Void response) {
                // do nothing
            }

            @Override
            public void failure(BaseError error) {
                // do nothing
            }
        });
        return call;
    }

    public static Call<Void> registerActions(@NonNull final Context context,
                                             @NonNull List<MeasurableViewHolderEvent> list) {

        final String data = AbstractGsonUtils.getInstance().toJson(list);
        Call<Void> call = ApiClient.getInstance(context).registerActions(ApiClient.getServiceIds(context), data);
        call.enqueue(new BaseCallback<Void>() {
            @Override
            public void success(Void response) {
                // do nothing
            }

            @Override
            public void failure(BaseError error) {
                // do nothing
            }
        });
        return call;
    }
}
