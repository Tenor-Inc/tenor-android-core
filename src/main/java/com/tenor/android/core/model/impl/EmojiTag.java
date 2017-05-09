package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;

import java.io.Serializable;

/**
 * The response model for emoji tag
 */
public class EmojiTag implements Serializable {
    private static final long serialVersionUID = -2207206185861282031L;
    @SerializedName("character")
    private String unicodeChars;
    @SerializedName("name")
    private String searchName;
    @SerializedName("path")
    private String imgUrl;
    @SerializedName("searchterm")
    private String searchTerm;

    /**
     * @return String of characters that make up the emoji in unicode form
     */
    @NonNull
    public String getUnicodeChars() {
        return StringConstant.parse(unicodeChars);
    }

    /**
     * @return full text of corresponding search word, for display purposes
     */
    @NonNull
    public String getSearchName() {
        return StringConstant.parse(searchName);
    }

    /**
     * @return url of the api search endpoint corresponding to the emoji
     */
    @NonNull
    public String getImgUrl() {
        return StringConstant.parse(imgUrl);
    }

    /**
     * @return full text of corresponding search word
     */
    @NonNull
    public String getSearchTerm() {
        return StringConstant.parse(searchTerm);
    }
}