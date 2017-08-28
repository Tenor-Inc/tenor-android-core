package com.tenor.android.core.validator;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class ColorHex extends AbstractValidator<CharSequence> {

    private static final long serialVersionUID = -5985057771273176145L;

    public ColorHex(@NonNull String regex) {
        super(regex);
    }

    @Override
    public boolean validate(CharSequence text) {
        return get().matcher(text).matches();
    }

    /*
     * ==============
     * Static Methods
     * ==============
     */
    private static final String COLOR_HEX_PATTERN = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private static final ColorHex COLOR_HEX = new ColorHex(COLOR_HEX_PATTERN);

    /**
     * Check if a color hex code is valid
     *
     * @param colorHex a color hex code in {@link String}
     * @return true if {@code colorHex} is a valid color hex code that in compliance with {@link #COLOR_HEX_PATTERN}
     */
    public static boolean isValid(@Nullable CharSequence colorHex) {
        return !TextUtils.isEmpty(colorHex) && COLOR_HEX.validate(colorHex);
    }

    /**
     * Parse the given {@link String} into color hex
     *
     * @param str    the string
     * @param defVal the default value
     */
    public static String parse(@Nullable String str, String defVal) {
        if (!isValid(str)) {
            if (!isValid(defVal)) {
                throw new IllegalArgumentException("default value must be a valid hex color code");
            }
            return defVal;
        }
        return str;
    }

    /**
     * Parse the given {@link String} into {@link ColorInt}
     *
     * @param str the string
     */
    @ColorInt
    public static int parse(@Nullable String str) {
        return parse(str, Color.TRANSPARENT);
    }

    /**
     * Parse the given {@link String} into {@link ColorInt}
     *
     * @param str    the string
     * @param defVal the default value
     */
    @ColorInt
    public static int parse(@Nullable String str, @ColorInt int defVal) {
        return isValid(str) ? Color.parseColor(str) : defVal;
    }
}
