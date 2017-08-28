package com.tenor.android.core.validator;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.regex.Pattern;

public interface IValidator<T> extends Serializable {
    @NonNull
    Pattern get();

    /**
     * @return true if validation result is positive, else false
     */
    boolean validate(T t);
}
