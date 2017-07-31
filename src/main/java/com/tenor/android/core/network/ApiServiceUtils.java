package com.tenor.android.core.network;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceUtils {

    public static synchronized <T> T create(@NonNull ApiService.Builder<T> builder) {

        Context context = builder.getContext();
        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }

        final File cacheDir = new File(context.getCacheDir().getAbsolutePath(), context.getPackageName());
        final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

        OkHttpClient.Builder http = new OkHttpClient.Builder()
                .cache(cache)
                .writeTimeout(builder.getTimeout(), TimeUnit.SECONDS);

        for (Interceptor interceptor : builder.getInterceptors()) {
            http.addInterceptor(interceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(builder.getEndpoint())
                .client(http.build())
                .addConverterFactory(GsonConverterFactory.create(builder.getGson()))
                .build();

        return retrofit.create(builder.getCls());
    }
}
