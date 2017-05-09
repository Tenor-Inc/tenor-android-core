package com.tenor.android.core.model.impl;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

public class FeaturedInfo implements Serializable {
    private static final long serialVersionUID = -1486902705491791354L;

    @SerializedName("feature_text")
    private String featureText;

    @SerializedName("button_link")
    private String buttonLink;

    @SerializedName("button_text")
    private String buttonText;

    @NonNull
    public String getFeatureText() {
        return StringConstant.parse(featureText);
    }

    @NonNull
    public String getButtonLink() {
        return StringConstant.parse(buttonLink);
    }

    @NonNull
    public String getButtonText() {
        return StringConstant.parse(buttonText);
    }
}
