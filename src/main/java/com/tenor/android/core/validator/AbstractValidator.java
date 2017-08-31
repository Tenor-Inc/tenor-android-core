package com.tenor.android.core.validator;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.regex.Pattern;

public abstract class AbstractValidator<T> implements IValidator<T> {

    private static final long serialVersionUID = 1605380452419139489L;
    private final Pattern mPattern;

    public AbstractValidator(@NonNull String regex) {
        if (TextUtils.isEmpty(regex)) {
            throw new IllegalArgumentException("regex cannot be empty");
        }
        mPattern = Pattern.compile(regex);
    }

    @NonNull
    @Override
    public Pattern get() {
        return mPattern;
    }
}
