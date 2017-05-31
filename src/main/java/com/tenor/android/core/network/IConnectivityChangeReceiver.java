package com.tenor.android.core.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tenor.android.core.network.impl.NetworkStatus;


public interface IConnectivityChangeReceiver {
    Context getContext();

    void onNetworkAvailable(@NonNull NetworkStatus networkStatus);

    void onNetworkLost(@NonNull NetworkStatus networkStatus);
}
