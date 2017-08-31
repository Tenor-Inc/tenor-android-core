package com.tenor.android.core.response;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;

/**
 * The base error response
 */
public class BaseError extends RuntimeException {
    private static final long serialVersionUID = 7706148212222544124L;
    private int code;
    private String error = StringConstant.EMPTY;

    public BaseError() {
        this(StringConstant.EMPTY);
    }

    public BaseError(@Nullable final String error) {
        this.error = !TextUtils.isEmpty(error) ? error : StringConstant.EMPTY;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return this.error;
    }

    @Override
    public String getMessage() {
        return this.error;
    }
}
