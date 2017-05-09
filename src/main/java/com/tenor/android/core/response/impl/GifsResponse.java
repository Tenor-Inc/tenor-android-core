package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The gifs response
 */
public class GifsResponse extends AbstractResponse {
    private static final long serialVersionUID = 2805215887381282883L;
    private String next;
    private List<Result> results;

    public boolean hasNext() {
        return !TextUtils.isEmpty(next);
    }

    @Nullable
    public String getNext() {
        return next;
    }

    @NonNull
    public List<Result> getResults() {
        return !AbstractListUtils.isEmpty(results) ? results : Collections.<Result>emptyList();
    }
}
