package com.tenor.android.core.network;


import android.support.annotation.NonNull;

public interface IApiService<T> {

    T get();

    T create(@NonNull ApiService.Builder<T> builder);

    @NonNull
    String getApiKey();

    @NonNull
    String getEndpoint();
}
