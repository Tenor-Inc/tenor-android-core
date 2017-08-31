package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.IGif;

/**
 * The model of {@link Tag}, which contains a collection of {@link Result}
 */
public class Tag implements IGif {
    private static final long serialVersionUID = 2978652640985303628L;
    private String image;
    private String name;

    @SerializedName(value = "path", alternate = {"url"})
    private String path;
    @SerializedName("searchterm")
    String searchTerm;

    @SerializedName("dims")
    private int[] dimensions;

    public int getWidth() {
        return dimensions != null && dimensions.length == 2 ? dimensions[0] : -1;
    }

    public int getHeight() {
        return dimensions != null && dimensions.length == 2 ? dimensions[1] : -1;
    }

    public float getAspectRatio() {
        return (float) getWidth() / getHeight();
    }

    public boolean hasAspectRatio() {
        return getWidth() > 0 && getHeight() > 0;
    }

    /**
     * @return the media url of a GIF that describes this {@link Tag}{@link Tag}
     */
    @NonNull
    public String getImage() {
        return StringConstant.getOrEmpty(image);
    }

    /**
     * @return the name of the {@link Tag}
     */
    @NonNull
    public String getName() {
        return StringConstant.getOrEmpty(name);
    }

    @NonNull
    public String getPath() {
        return StringConstant.getOrEmpty(path);
    }

    /**
     * @return the {@link String} to be used to search the GIFs related to this {@link Tag}
     */
    public String getSearchTerm() {
        return StringConstant.getOrEmpty(searchTerm);
    }

    @NonNull
    @Override
    public String getId() {
        return StringConstant.getOrEmpty(name);
    }
}
