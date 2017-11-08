package com.tenor.android.core.service;

import android.content.pm.PackageManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Subclass of AdvertisingIdClient#Info
 */
public final class AaidInfo {

    @IntDef({AAID_GRANTED, AAID_DENIED, AAID_FAILURE_NO_GOOGLE_PLAY, AAID_FAILURE_NO_AAID_LIBRARY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    /**
     * Android Advertising ID is granted
     */
    public static final int AAID_GRANTED = PackageManager.PERMISSION_GRANTED;

    /**
     * Android Advertising ID is denied because {@link #isLimitAdTrackingEnabled()} is {@code true} or it is {@link StringConstant#EMPTY}
     */
    public static final int AAID_DENIED = PackageManager.PERMISSION_DENIED;

    public static final int AAID_FAILURE_NO_GOOGLE_PLAY = AAID_DENIED - 1;
    public static final int AAID_FAILURE_NO_AAID_LIBRARY = AAID_DENIED - 2;
    public static final int AAID_FAILURE = AAID_DENIED - 3;

    @NonNull
    private final String mAdvertisingId;
    private final boolean mLimitAdTrackingEnabled;

    private final int mState;

    AaidInfo(@State int state) {
        this(StringConstant.EMPTY, false, state);
    }

    AaidInfo(@Nullable String advertisingId, boolean limitAdTrackingEnabled) {
        this(advertisingId, limitAdTrackingEnabled,
                TextUtils.isEmpty(advertisingId) || limitAdTrackingEnabled ? AAID_DENIED : AAID_GRANTED);
    }

    private AaidInfo(@Nullable String advertisingId, boolean limitAdTrackingEnabled, int state) {
        mAdvertisingId = StringConstant.getOrEmpty(advertisingId);
        mLimitAdTrackingEnabled = limitAdTrackingEnabled;
        mState = state;
    }

    @NonNull
    public String getId() {
        return mAdvertisingId;
    }

    public boolean isLimitAdTrackingEnabled() {
        return mLimitAdTrackingEnabled;
    }

    @State
    public int getState() {
        return mState;
    }
}
