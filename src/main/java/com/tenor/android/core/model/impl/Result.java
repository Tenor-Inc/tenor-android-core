package com.tenor.android.core.model.impl;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.IGif;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response model of result
 */
public class Result implements IGif {
    private static final long serialVersionUID = -4037633614634142811L;
    private Media composite;
    private double created;
    private String id;

    @SerializedName("media")
    private List<MediaCollection> medias;

    private List<String> tags;

    private int shares;

    private GifPoster user;

    private String title;

    private String url;

    private String embed;

    @SerializedName("itemurl")
    private String itemUrl;

    @SerializedName("hasaudio")
    private boolean hasAudio;

    @SerializedName("bg_color")
    private String placeholderColor;

    @SerializedName("aspect_ratio")
    private String aspectRatio;

    @SerializedName("feature_info")
    private FeaturedInfo featuredInfo;

    @SerializedName("source_id")
    private String sourceId;

    /**
     * @return true if asset has sound
     */
    public boolean isHasAudio() {
        return hasAudio;
    }

    @Nullable
    public Media getComposite() {
        return composite;
    }

    /**
     * @return created at timestamp
     */
    public double getCreated() {
        return created;
    }

    /**
     * @return unique result id
     */
    @NonNull
    public String getId() {
        return StringConstant.getOrEmpty(id);
    }

    @Override
    @NonNull
    public String getName() {
        return StringConstant.getOrEmpty(title);
    }

    /**
     * @return collection of Media objects for the different asset formats
     */
    @NonNull
    public List<MediaCollection> getMedias() {
        return !AbstractListUtils.isEmpty(medias) ? medias : Collections.<MediaCollection>emptyList();
    }

    /**
     * @return list of related tags for a result
     */
    @NonNull
    public List<String> getTags() {
        return !AbstractListUtils.isEmpty(tags) ? tags : Collections.<String>emptyList();
    }

    /**
     * @return times shared by all users
     */
    public int getShares() {
        return shares;
    }

    @NonNull
    public GifPoster getUser() {
        return user != null ? user : new GifPoster();
    }

    /**
     * @return name of the gif
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return default web url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return web embed code
     */
    public String getEmbed() {
        return embed;
    }

    /**
     * @return alternative web url
     */
    public String getItemUrl() {
        return itemUrl;
    }

    @NonNull
    public String getPlaceholderColor() {
        return !TextUtils.isEmpty(placeholderColor) ? placeholderColor : "#000000";
    }

    @FloatRange(from = 0.01f, to = 5.01f)
    public float getAspectRatio() {
        return Float.parseFloat(aspectRatio != null ? aspectRatio : "1.778f");
    }

    @Nullable
    public FeaturedInfo getFeaturedInfo() {
        return featuredInfo;
    }

    public boolean hasFeaturedInfo() {
        return featuredInfo != null;
    }

    @NonNull
    public String getSourceId() {
        return hasSourceId() ? sourceId : StringConstant.EMPTY;
    }

    public boolean hasSourceId() {
        return !TextUtils.isEmpty(sourceId);
    }
}
