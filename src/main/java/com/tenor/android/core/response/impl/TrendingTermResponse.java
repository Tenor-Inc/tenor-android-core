package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response of trending terms
 */
public class TrendingTermResponse extends AbstractResponse {

    private static final long serialVersionUID = -8215681889738059793L;
    private List<String> results;

    @NonNull
    public List<String> getTrendingTerms() {
        return !AbstractListUtils.isEmpty(results) ? results : Collections.<String>emptyList();
    }
}
