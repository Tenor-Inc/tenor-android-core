package com.tenor.android.core.model.impl;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

/**
 * The model of {@link Image}
 */
public class Image implements Serializable {
    private static final long serialVersionUID = -8616498739266612929L;
    private String url;

    @SerializedName("dims")
    private int[] dimensions;

    /**
     * @return url of the raw asset
     */
    @NonNull
    public String getUrl() {
        return StringConstant.getOrEmpty(url);
    }

    public int getWidth() {
        return dimensions != null && dimensions.length == 2 ? dimensions[0] : -1;
    }

    public int getHeight() {
        return dimensions != null && dimensions.length == 2 ? dimensions[1] : -1;
    }

    /**
     * @return aspect ratio of this {@link Image} or the default 1080p aspect ratio, 1.778f
     */
    @FloatRange(from = 0.01f, to = 5.01f)
    public float getAspectRatio() {
        final float aspectRatio = (float) getWidth() / getHeight();
        return aspectRatio >= 0.01f && aspectRatio <= 5.01f ? aspectRatio : 1.778f;
    }
}
