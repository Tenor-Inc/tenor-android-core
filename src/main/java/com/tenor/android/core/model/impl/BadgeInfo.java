package com.tenor.android.core.model.impl;


import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.util.AbstractListUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class BadgeInfo implements Serializable {

    private static final long serialVersionUID = 2519123239149906317L;

    @SerializedName("view_acceptance_fraction")
    private float threshold;
    private List<ItemBadge> badges;

    @FloatRange(from = 0.01f, to = 1f)
    public float getThreshold() {
        return threshold >= 0.01f && threshold <= 1f ? threshold : 1f;
    }

    public boolean hasBadges() {
        return !AbstractListUtils.isEmpty(badges);
    }

    @NonNull
    public List<ItemBadge> getBadges() {
        return hasBadges() ? badges : Collections.<ItemBadge>emptyList();
    }
}
