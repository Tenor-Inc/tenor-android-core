package com.tenor.android.core.util;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.regex.Pattern;

public abstract class AbstractColorUtils {

    private static final String COLOR_HEX_PATTERN = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private static Pattern sPattern;

    private static Pattern getHexPattern() {
        if (sPattern == null) {
            sPattern = Pattern.compile(COLOR_HEX_PATTERN);
        }
        return sPattern;
    }

    /**
     * Check if a color hex code is valid
     *
     * @param colorHex a color hex code in {@link String}
     * @return true if {@code colorHex} is a valid color hex code that in compliance with {@link #COLOR_HEX_PATTERN}
     */
    public static boolean isColorHex(@Nullable CharSequence colorHex) {
        return !TextUtils.isEmpty(colorHex) && getHexPattern().matcher(colorHex).matches();
    }

    /**
     * Retrieve color from res id
     *
     * @param context    the context
     * @param colorResId res id of the color value
     * @return A single color value in the form 0xAARRGGBB, or, the colorResId if context is null
     */
    @ColorInt
    public static int getColor(@NonNull Context context, @ColorRes int colorResId) {
        return ContextCompat.getColor(context, colorResId);
    }
}
