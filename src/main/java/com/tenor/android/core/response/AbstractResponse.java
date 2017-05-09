package com.tenor.android.core.response;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

/**
 * The abstract response that all other responses should extend from
 */
public abstract class AbstractResponse implements Serializable {

    private static final long serialVersionUID = 2769940807942589161L;
    private String error;

    @NonNull
    public String getError() {
        return hasError() ? error : StringConstant.EMPTY;
    }

    public boolean hasError() {
        return !TextUtils.isEmpty(error);
    }
}
