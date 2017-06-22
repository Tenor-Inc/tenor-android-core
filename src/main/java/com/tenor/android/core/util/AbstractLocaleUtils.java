package com.tenor.android.core.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

import com.tenor.android.core.constant.StringConstant;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The locale utility class
 */
public abstract class AbstractLocaleUtils {

    public static final String ISO_US = "us";
    private static Locale sEnUsLocale;

    /**
     * Get the {@link Locale} that is currently used by the device
     *
     * @return the {@link Locale} that is currently used by the device
     */
    @NonNull
    public static Locale getCurrentLocale(@Nullable final Context context) {
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
     * Get current UTC offset, such as "-0700"
     *
     * @param context the given context
     * @return the current UTC offset, such as "-0700"
     */
    @NonNull
    public static String getCurrentUtcOffset(@Nullable final Context context) {
        if (context == null) {
            return StringConstant.EMPTY;
        }
        return new SimpleDateFormat("ZZZ", getCurrentLocale(context)).format(System.currentTimeMillis());
    }

    /**
     * Get the "en_US" {@link Locale}
     *
     * @return the "en_US" {@link Locale}
     */
    @NonNull
    public static Locale getEnUsLocale() {
        if (sEnUsLocale == null) {
            /*
             * Use constructor instead of builder pattern to keep compatibility with API 21 and below
             *
             * See AbstractStringUtilsUT#test_locale_supports() for test cases
             */
            sEnUsLocale = new Locale("en", "US");
        }
        return sEnUsLocale;
    }

    /**
     * Is the phone currently within the U.S.
     *
     * @param context the given context
     * @return true if the phone is currently within the U.S.
     */
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

        if (AbstractNetworkUtils.isWifiConnected(context)
                && telephonyManager.getPhoneType() != TelephonyManager.NETWORK_TYPE_CDMA) {
            return ISO_US.equals(telephonyManager.getNetworkCountryIso());
        }

        return telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT
                && ISO_US.equals(telephonyManager.getSimCountryIso());
    }
}
