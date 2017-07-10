package com.tenor.android.core.network.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v4.util.ArrayMap;

import com.tenor.android.core.listener.IWeakRefObject;
import com.tenor.android.core.network.IConnectivityChangeReceiver;
import com.tenor.android.core.util.AbstractNetworkUtils;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public class ConnectivityChangeReceiver<CTX extends IConnectivityChangeReceiver>
        extends BroadcastReceiver implements IWeakRefObject<CTX> {

    private static IntentFilter sIntentFilter;
    private static NetworkStatus sNetworkStatus;
    private static ArrayMap<String, NetworkStatus> sNetworkHistory;
    private final WeakReference<CTX> mWeakRef;
    private final String mRegisterName;

    public ConnectivityChangeReceiver(@NonNull final WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
        mRegisterName = weakRef.get().getClass().getName();
    }

    public ConnectivityChangeReceiver(@NonNull final CTX context) {
        this(new WeakReference<>(context));
    }

    @Nullable
    @Override
    public CTX getRef() {
        return mWeakRef.get();
    }

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    public <T extends IConnectivityChangeReceiver> boolean isRegisterChanged(@Nullable final T context) {
        return context == null || context.getClass() == null
                || !mRegisterName.equals(context.getClass().getName());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            return;
        }

        /*
         * [ANDROID-1950]
         *
         * use ConnectivityManagerCompat.getNetworkInfoFromBroadcast() to obtain
         * the current state from {@link ConnectivityManager} instead of using the
         * potentially-stale value from {@link ConnectivityManager#EXTRA_NETWORK_INFO}
         */
        final NetworkStatus networkStatus = new NetworkStatus(ConnectivityManagerCompat.getNetworkInfoFromBroadcast(
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE), intent));

        // [ANDROID-1406] handle the case that init() is not being called yet
        if (sNetworkStatus == null) {
            sNetworkStatus = networkStatus;
            return;
        }

        if (getHistory().containsKey(mRegisterName)
                && !getHistory().get(mRegisterName).isStatusChanged(networkStatus)) {
            // network state hasn't changed from the last check
            return;
        }

        getHistory().put(mRegisterName, networkStatus);
        sNetworkStatus = networkStatus;

        if (!hasRef()) {
            return;
        }

        if (isOnline(context)) {
            getWeakRef().get().onNetworkAvailable(sNetworkStatus);
        } else {
            getWeakRef().get().onNetworkLost(sNetworkStatus);
        }
    }

    /**
     * Initialize on {@link android.app.Application}
     *
     * @param context the context
     */
    public static void init(@Nullable final Context context) {
        sNetworkStatus = new NetworkStatus(context);
    }

    /**
     * Get the network availability; it will try to obtain the current state from {@link ConnectivityManager}
     * Using {@link ConnectivityManagerCompat#getNetworkInfoFromBroadcast(ConnectivityManager, Intent)};
     * if it fails, it will obtain the potentially-stale value from {@link ConnectivityManager#EXTRA_NETWORK_INFO}
     *
     * @param context the context
     */
    public static boolean isOnline(@Nullable final Context context) {
        if (sNetworkStatus == null) {
            sNetworkStatus = new NetworkStatus(AbstractNetworkUtils.getNetworkInfo(context));
        }
        return sNetworkStatus.isOnline();
    }

    /**
     * Get the history of contexts the receiver has been registered to
     */
    private static ArrayMap<String, NetworkStatus> getHistory() {
        if (sNetworkHistory == null) {
            sNetworkHistory = new ArrayMap<>(4);
        }
        return sNetworkHistory;
    }

    public static IntentFilter getIntentFilter() {
        if (sIntentFilter == null) {
            sIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        }
        return sIntentFilter;
    }
}
