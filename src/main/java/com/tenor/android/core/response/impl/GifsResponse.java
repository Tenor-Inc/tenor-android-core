package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.CoreListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response of GIFs
 */
public class GifsResponse extends AbstractResponse {
    private static final long serialVersionUID = 2805215887381282883L;
    private String next;
    private List<Result> results;

    public boolean hasNext() {
        return !TextUtils.isEmpty(next);
    }

    @NonNull
    public String getNext() {
        return StringConstant.getOrEmpty(next);
    }

    @NonNull
    public List<Result> getResults() {
        return !CoreListUtils.isEmpty(results) ? results : Collections.<Result>emptyList();
    }
}
