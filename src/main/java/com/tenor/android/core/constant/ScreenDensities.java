package com.tenor.android.core.constant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import com.tenor.android.core.R;

import java.util.Locale;

public class ScreenDensities {

    public static final String UNKNOWN = "0.0";
    public static final String SD_075 = "0.75";
    public static final String SD_100 = "1.0";
    public static final String SD_150 = "1.5";
    public static final String SD_200 = "2.0";
    public static final String SD_300 = "3.0";
    public static final String SD_400 = "4.0";

    /**
     * Parse screen density
     *
     * @param context the {@link Context}
     * @return the {@link ScreenDensity}
     */
    @ScreenDensity
    public static String get(@NonNull Context context) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final String formatter = (metrics.density < 1f) ? "%.2f" : "%.1f";
        final String density = parse(String.format(Locale.US, formatter, metrics.density), UNKNOWN);
        if (UNKNOWN.equals(density)) {
            return parse(context.getResources().getString(R.string.screen_density), SD_300);
        }
        return density;
    }

    @ScreenDensity
    private static String parse(@Nullable String density, @ScreenDensity String defVal) {
        if (density == null) {
            return UNKNOWN;
        }

        switch (density) {
            case SD_075:
                return SD_075;
            case SD_100:
                return SD_100;
            case SD_150:
                return SD_150;
            case SD_200:
                return SD_200;
            case SD_300:
                return SD_300;
            case SD_400:
                return SD_400;
            default:
                return defVal;
        }
    }
}
