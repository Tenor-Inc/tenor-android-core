package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.util.AbstractListUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The response model of similar results, used for auto-suggestion and auto-correction features
 */
public class SimilarResults implements Serializable {
    private static final long serialVersionUID = -2732692251223305342L;

    private List<Pivot> similar;
    private List<Pivot> corrections;

    @NonNull
    public List<Pivot> getSimilar() {
        return !AbstractListUtils.isEmpty(similar) ? similar : Collections.<Pivot>emptyList();
    }

    @NonNull
    public List<Pivot> getCorrections() {
        return !AbstractListUtils.isEmpty(corrections) ? corrections : Collections.<Pivot>emptyList();
    }
}
