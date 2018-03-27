package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.CoreListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response of search suggestions
 */
public class SearchSuggestionResponse extends AbstractResponse {

    private static final long serialVersionUID = 8046525237691607393L;
    private List<String> results;

    @NonNull
    public List<String> getResults() {
        return !CoreListUtils.isEmpty(results) ? results : Collections.<String>emptyList();
    }
}
