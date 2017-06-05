package com.tenor.android.core.model.impl;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.AdIconPosition;
import com.tenor.android.core.constant.AdIconPositions;
import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

public class AdBadgeInfo implements Serializable {

    private static final long serialVersionUID = 5769727680233855104L;

    @SerializedName("url")
    private String adIconUrl;

    @SerializedName("position")
    private int adIconPosition;

    @NonNull
    public String getAdIconUrl() {
        return StringConstant.getOrEmpty(adIconUrl);
    }

    @AdIconPosition
    public int getAdIconPosition() {
        return AdIconPositions.parse(adIconPosition);
    }
}
