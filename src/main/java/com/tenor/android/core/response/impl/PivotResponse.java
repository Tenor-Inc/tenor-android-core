package com.tenor.android.core.response.impl;

import com.tenor.android.core.model.impl.SimilarResults;
import com.tenor.android.core.response.AbstractResponse;

/**
 * The pivot response
 */
public class PivotResponse extends AbstractResponse {

    private static final long serialVersionUID = -962133554919000759L;
    private SimilarResults results;

    public SimilarResults getResults() {
        return results;
    }
}
