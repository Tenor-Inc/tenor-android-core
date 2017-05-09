package com.tenor.android.core.model.impl;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.model.IGif;

/**
 * The response model of pivot
 */
public class Pivot implements IGif {
    private static final long serialVersionUID = -4562054991401683841L;

    @SerializedName("image")
    private String image;

    // naming 'tag' as 'name' is intended in here
    @SerializedName("tag")
    private String name;

    public Pivot(String name, String image) {
        this.image = image;
        this.name = name;
    }

    /**
     * @return url of image or gif asset to play in pivot
     */
    public String getImage() {
        return image;
    }

    /**
     * @return pivot search tag
     */
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return name;
    }
}
