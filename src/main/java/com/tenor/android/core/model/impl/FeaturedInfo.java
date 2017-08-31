package com.tenor.android.core.model.impl;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

public class FeaturedInfo implements Serializable {
    private static final long serialVersionUID = -1486902705491791354L;

    @SerializedName("feature_text")
    private String mFeatureText;

    @SerializedName("button_link")
    private String mButtonLink;

    @SerializedName("button_text")
    private String mButtonText;

    @NonNull
    public String getFeatureText() {
        return StringConstant.getOrEmpty(mFeatureText);
    }

    @NonNull
    public String getButtonLink() {
        return StringConstant.getOrEmpty(mButtonLink);
    }

    @NonNull
    public String getButtonText() {
        return StringConstant.getOrEmpty(mButtonText);
    }
}
