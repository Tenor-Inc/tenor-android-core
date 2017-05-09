package com.tenor.android.core.model.impl;

import com.google.gson.annotations.SerializedName;

/**
 * The response model of emoji tag
 */
public class Media extends Image {
    private static final long serialVersionUID = -8616498739266612929L;
    @SerializedName("preview")
    private String previewUrl;
    private double duration;

    /**
     * @return url of the static image preview
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * @return duration of each loop, in seconds
     */
    public double getDuration() {
        return duration;
    }
}
