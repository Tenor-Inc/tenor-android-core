package com.tenor.android.core.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoidCallBack implements Callback<Void> {
    @Override
    public final void onResponse(Call<Void> call, Response<Void> response) {
        // do nothing
    }

    @Override
    public final void onFailure(Call<Void> call, Throwable t) {
        // do nothing
    }
}
