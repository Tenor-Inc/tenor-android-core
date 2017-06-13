package com.tenor.android.core.response.impl;

import com.tenor.android.core.model.impl.PivotResult;
import com.tenor.android.core.response.AbstractResponse;

/**
 * The pivot response
 *
 * @deprecated
 */
public class PivotResponse extends AbstractResponse {

    private static final long serialVersionUID = -962133554919000759L;
    private PivotResult results;

    public PivotResult getResults() {
        return results;
    }
}
