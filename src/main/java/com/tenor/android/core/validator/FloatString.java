package com.tenor.android.core.validator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class FloatString extends AbstractValidator<CharSequence> {

    private static final long serialVersionUID = -5985057771273176145L;

    public FloatString(@NonNull String regex) {
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
    private static final String FLOAT_STRING_PATTERN = "^[0-9]+\\.[0-9]+$";
    private static final FloatString FLOAT_STRING = new FloatString(FLOAT_STRING_PATTERN);

    /**
     * Check if a color hex code is valid
     *
     * @param floatString a color hex code in {@link String}
     * @return true if {@code colorHex} is a valid color hex code that in compliance with {@link #FLOAT_STRING_PATTERN}
     */
    public static boolean isValid(@Nullable CharSequence floatString) {
        return !TextUtils.isEmpty(floatString) && FLOAT_STRING.validate(floatString);
    }

    /**
     * Parse the given {@link String} into float
     *
     * @param str    the string
     * @param defVal the default value
     */
    public static float parse(@Nullable String str, float defVal) {
        if (!isValid(str)) {
            return defVal;
        }

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException ignored) {
            return defVal;
        }
    }
}
