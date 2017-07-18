package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The search suggestion response
 */
public class SearchSuggestionResponse extends AbstractResponse {

    private static final long serialVersionUID = 8046525237691607393L;
    private List<String> results;

    @NonNull
    public List<String> getResults() {
        return !AbstractListUtils.isEmpty(results) ? results : Collections.<String>emptyList();
    }
}
