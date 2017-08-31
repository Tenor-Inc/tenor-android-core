package com.tenor.android.core.network;


import android.support.annotation.NonNull;

public interface IApiService<T> {

    T get();

    T create(@NonNull ApiService.Builder<T> builder);

    @NonNull
    String getApiKey();

    /**
     * Return host endpoint.
     * If in BuildConfig.DEBUG, use the developer servers.
     * Otherwise, use the production servers
     *
     * @return String of the api <b>endpoint host</b>
     */
    @NonNull
    String getEndpoint();
}
