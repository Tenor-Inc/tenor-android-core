package com.tenor.android.core.network;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.tenor.android.core.constant.ScreenDensities;
import com.tenor.android.core.listener.IAnonIdListener;
import com.tenor.android.core.measurable.MeasurableViewHolderEvent;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.BaseCallback;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.AnonIdResponse;
import com.tenor.android.core.service.AaidService;
import com.tenor.android.core.util.AbstractGsonUtils;
import com.tenor.android.core.util.AbstractLocaleUtils;
import com.tenor.android.core.util.AbstractSessionUtils;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * API Client to make network calls to retrieve contents
 */
public class ApiClient {

    private static volatile IApiService<IApiClient> sApiService;

    private static synchronized void init(@NonNull final Context context) {
        init(context, new ApiService.Builder<>(context, IApiClient.class));
    }

    /**
     * Initialize the ApiClient instance with your custom interceptor
     *
     * @param context the context
     * @param builder {@link ApiService.Builder}
     */
    public static synchronized void init(@NonNull final Context context,
                                         @NonNull ApiService.Builder<IApiClient> builder) {
        init(context, builder, null);
    }

    public static synchronized void init(@NonNull final Context context,
                                         @NonNull ApiService.Builder<IApiClient> builder,
                                         @Nullable IAnonIdListener listener) {
        if (sApiService == null) {
            sApiService = builder.build();
        }

        if (!AbstractSessionUtils.hasAnonId(context)) {
            getAnonId(context, listener);
        }
    }

    /**
     * Retrieve instance of the {@link ApiClient}, and create instance if not already created
     *
     * @return the {@link ApiClient} instance
     */
    @NonNull
    public static synchronized IApiClient getInstance() {
        if (sApiService == null) {
            throw new IllegalStateException("Client cannot be null, please run ApiClient#Builder() first");
        }
        return sApiService.get();
    }

    /**
     * Lazy initialize a {@link IApiClient} with default {@link IApiService} configuration
     */
    @NonNull
    public static synchronized IApiClient getInstance(@NonNull Context context) {
        if (sApiService == null) {
            init(context);
        }
        return sApiService.get();
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
        map.put("key", sApiService.getApiKey());

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
        map.put("screen_density", ScreenDensities.get(context));
        return map;
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
                .getAnonId(sApiService.getApiKey(), AbstractLocaleUtils.getCurrentLocaleName(context));

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
     * Report shared gif id for better search experience in the future
     *
     * @param context the application context
     * @param id      the gif id
     * @return {@link Call}<{@link Void}>
     */
    public static Call<Void> registerShare(@NonNull final Context context,
                                           @NonNull final String id) {
        Call<Void> call = ApiClient.getInstance(context)
                .registerShare(getServiceIds(context), Integer.valueOf(id));
        call.enqueue(new VoidCallBack());
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
        Call<Void> call = ApiClient.getInstance(context).registerAction(getServiceIds(context), event);
        call.enqueue(new VoidCallBack());
        return call;
    }

    public static Call<Void> registerActions(@NonNull final Context context,
                                             @NonNull List<MeasurableViewHolderEvent> list) {
        final String data = AbstractGsonUtils.getInstance().toJson(list);
        Call<Void> call = ApiClient.getInstance().registerActions(getServiceIds(context), data);
        call.enqueue(new VoidCallBack());
        return call;
    }
}
