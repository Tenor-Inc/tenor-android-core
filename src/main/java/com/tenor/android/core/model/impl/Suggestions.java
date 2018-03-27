package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.util.CoreListUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The model of {@link Suggestions}, which contains a collection of {@link String}
 */
public class Suggestions implements Serializable {
    private static final long serialVersionUID = 5805025178874996139L;

    @SerializedName("results")
    private List<String> suggestions;

    @NonNull
    public List<String> getSuggestions() {
        return !CoreListUtils.isEmpty(suggestions) ? suggestions : Collections.<String>emptyList();
    }
}
