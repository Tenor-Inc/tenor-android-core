package com.tenor.android.core.network.impl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.net.ConnectivityManagerCompat;

import com.tenor.android.core.util.AbstractNetworkUtils;

import java.io.Serializable;

public class NetworkStatus implements Serializable {

    private static final long serialVersionUID = -7942562912903083308L;
    private final boolean mOnline;
    private final boolean mWifi;
    private final boolean mCellular;

    /**
     * Testing construct {@link NetworkStatus}
     */
    @VisibleForTesting
    public NetworkStatus() {
        this(false, false, false);
    }

    /**
     * Testing construct {@link NetworkStatus}
     */
    @VisibleForTesting
    public NetworkStatus(boolean isOnline) {
        this(isOnline, false, false);
    }

    /**
     * Testing construct {@link NetworkStatus}
     */
    @VisibleForTesting
    public NetworkStatus(boolean isOnline, boolean isWifi, boolean isCellular) {
        mOnline = isOnline;
        mWifi = isWifi;
        mCellular = isCellular;
    }

    /**
     * Construct {@link NetworkStatus} using the the potentially-stale value
     * from {@link ConnectivityManager#EXTRA_NETWORK_INFO} and {@link ConnectivityManager#getActiveNetworkInfo()}
     */
    public NetworkStatus(@Nullable final Context context) {
        this(AbstractNetworkUtils.getNetworkInfo(context));
    }

    /**
     * Construct {@link NetworkStatus}.
     * <p>
     * This is the preferable constructor, users can obtain the current state from {@link ConnectivityManager}
     * through using {@link ConnectivityManagerCompat#getNetworkInfoFromBroadcast(ConnectivityManager, Intent)}
     */
    public NetworkStatus(@Nullable final NetworkInfo info) {
        if (info == null) {
            mOnline = false;
            mWifi = false;
            mCellular = false;
            return;
        }
        mOnline = info.isConnected();
        mWifi = mOnline && info.getType() == ConnectivityManager.TYPE_WIFI;
        mCellular = mOnline && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean isOnline() {
        return mOnline;
    }

    public boolean isWifi() {
        return isOnline() && mWifi;
    }

    public boolean isCellular() {
        return isOnline() && mCellular;
    }

    public boolean isStatusChanged(@Nullable final NetworkStatus networkStatus) {
        return networkStatus == null
                || mOnline != networkStatus.isOnline()
                || mWifi != networkStatus.isWifi()
                || mCellular != networkStatus.isCellular();
    }
}
