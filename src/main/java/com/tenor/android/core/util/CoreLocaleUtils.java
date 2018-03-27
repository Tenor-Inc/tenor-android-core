package com.tenor.android.core.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import com.tenor.android.core.constant.StringConstant;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The locale utility class
 */
public class CoreLocaleUtils {

    public static final String ISO_US = "us";

    /**
     * Get the {@link Locale} that is currently used by the device
     *
     * @return the {@link Locale} that is currently used by the device
     */
    @NonNull
    public static Locale getCurrentLocale(@Nullable Context context) {
        if (context == null) {
            return Locale.getDefault();
        }

        Resources resources = context.getResources();
        if (resources == null) {
            return Locale.getDefault();
        }

        Configuration configuration = resources.getConfiguration();
        if (configuration == null || configuration.locale == null) {
            return Locale.getDefault();
        }
        return configuration.locale;
    }

    /**
     * Get the name of {@link Locale} that is currently used by the device
     *
     * @return the name {@link Locale} that is currently used by the device
     */
    @NonNull
    public static String getCurrentLocaleName(@NonNull final Context context) {
        return getCurrentLocale(context).toString();
    }

    /**
     * Get current UTC offset, such as "-0700" using the {@link Locale#US}
     *
     * @param context the given context
     * @return the current UTC offset, such as "-0700"
     */
    @NonNull
    public static String getUtcOffset(@Nullable Context context) {
        return getUtcOffset(context, Locale.US);
    }

    /**
     * Get current UTC offset, such as "-0700"
     *
     * @param context the given context
     * @param locale  the {@link Locale}
     * @return the current UTC offset, such as "-0700"
     */
    @NonNull
    public static String getUtcOffset(@Nullable Context context, @Nullable Locale locale) {
        if (context == null) {
            return StringConstant.EMPTY;
        }

        if (locale == null) {
            return getUtcOffset(context, Locale.US);
        }
        return new SimpleDateFormat("ZZZ", locale).format(System.currentTimeMillis());
    }

    /**
     * Is the phone currently within the U.S.
     *
     * @param context the given context
     * @return true if the phone is currently within the U.S.
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isUSRegion(@Nullable final Context context) {
        if (context == null) {
            return false;
        }

        /*
         * TELEPHONY_SERVICE needs permission, "android.permission.READ_PHONE_STATE"
         */
        final TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        } catch (Throwable ignored) {
            // no "android.permission.READ_PHONE_STATE" permission
            return false;
        }

        if (telephonyManager == null) {
            return false;
        }

        if (CoreNetworkUtils.isWifiConnected(context)
                && telephonyManager.getPhoneType() != TelephonyManager.NETWORK_TYPE_CDMA) {
            return ISO_US.equals(telephonyManager.getNetworkCountryIso());
        }

        return telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT
                && ISO_US.equals(telephonyManager.getSimCountryIso());
    }
}
