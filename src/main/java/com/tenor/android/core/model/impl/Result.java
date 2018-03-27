package com.tenor.android.core.model.impl;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.IGif;
import com.tenor.android.core.util.CoreListUtils;
import com.tenor.android.core.validator.ColorHex;
import com.tenor.android.core.validator.FloatString;

import java.util.Collections;
import java.util.List;

/**
 * The model of {@link Result}
 */
public class Result implements IGif {
    private static final long serialVersionUID = -4037633614634142811L;

    private String url;

    @SerializedName("media")
    private List<MediaCollection> medias;

    private double created;
    private int shares;
    private String itemurl;
    private Media composite;

    @SerializedName("hasaudio")
    private boolean hasAudio;

    private String title;
    private String id;
    private List<String> tags;

    @SerializedName("bg_color")
    private String placeholderColor;

    @SerializedName("aspect_ratio")
    private String aspectRatio;

    @SerializedName("feature_info")
    private FeaturedInfo featuredInfo;

    @SerializedName("badge_info")
    private BadgeInfo badgeInfo;

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
     * @return collection of {@link Media} formats of this {@link Result}
     */
    @NonNull
    public List<MediaCollection> getMedias() {
        return !CoreListUtils.isEmpty(medias) ? medias : Collections.<MediaCollection>emptyList();
    }

    /**
     * @return collection of related {@link Tag}s of this {@link Result}
     */
    @NonNull
    public List<String> getTags() {
        return !CoreListUtils.isEmpty(tags) ? tags : Collections.<String>emptyList();
    }

    /**
     * @return share count of this {@link Result}
     */
    public int getShares() {
        return shares;
    }

    /**
     * @return name of this {@link Result}
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return web url of this {@link Result}
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return alternative web url of this {@link Result}
     */
    public String getItemUrl() {
        return itemurl;
    }

    /**
     * @return placeholder color hex of this {@link Result}
     */
    @NonNull
    public String getPlaceholderColorHex() {
        return ColorHex.parse(placeholderColor, "#000000");
    }

    /**
     * @return aspect ratio of this {@link Result} or the default 1080p aspect ratio, 1.7778f
     */
    @FloatRange(from = 0.01f, to = 5.01f)
    public float getAspectRatio() {
        float ratio = FloatString.parse(aspectRatio, 1.7778f);
        return ratio >= 0.01f && ratio <= 5.01f ? ratio : 1.7778f;
    }

    @Nullable
    public FeaturedInfo getFeaturedInfo() {
        return featuredInfo;
    }

    public boolean hasFeaturedInfo() {
        return featuredInfo != null;
    }

    @Nullable
    public BadgeInfo getBadgeInfo() {
        return badgeInfo;
    }

    public boolean hasBadgeInfo() {
        return badgeInfo != null;
    }

    @NonNull
    public String getSourceId() {
        return StringConstant.getOrEmpty(sourceId);
    }

    public boolean hasSourceId() {
        return !TextUtils.isEmpty(sourceId);
    }
}
