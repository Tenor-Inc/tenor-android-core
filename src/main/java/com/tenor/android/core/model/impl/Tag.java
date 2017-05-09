package com.tenor.android.core.model.impl;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.model.IGif;

/**
 * The response model of tag, which represents a collection of {@link Result}; it also with a name and a serch term
 */
public class Tag implements IGif {
    private static final long serialVersionUID = 2978652640985303628L;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
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
     * @return The image describing this Tag.
     */
    public String getImage() {
        return image;
    }

    /**
     * @return The name of the Tag.
     */
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    /**
     * @return String that when used with <b>ApiClient.getApi(Context).search</b> will return the gifs associated with this Tag.
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    @Override
    public String getId() {
        return name;
    }
}
