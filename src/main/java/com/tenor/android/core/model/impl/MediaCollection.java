package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.constant.MediaCollectionFormat;
import com.tenor.android.core.constant.MediaCollectionFormats;

import java.io.Serializable;

/**
 * The model of {@link MediaCollection}, which contains a collection of {@link Media} formats of {@link Result}
 */
public class MediaCollection implements Serializable {
    private static final long serialVersionUID = -8824214919408809561L;

    private Media gif;
    private Media tinygif;
    private Media mediumgif;
    private Media nanogif;

    private Media mp4;
    private Media loopedmp4;
    private Media tinymp4;
    private Media nanomp4;

    private Media webm;
    private Media tinywebm;
    private Media nanowebm;

    /**
     * @param type one of {@link MediaCollectionFormats}
     */
    @NonNull
    public Media get(@MediaCollectionFormat String type) {
        switch (type) {
            case MediaCollectionFormats.GIF:
                return getOrEmptyMedia(gif);
            case MediaCollectionFormats.GIF_TINY:
                return getOrEmptyMedia(tinygif);
            case MediaCollectionFormats.GIF_MEDIUM:
                return getOrEmptyMedia(mediumgif);
            case MediaCollectionFormats.GIF_NANO:
                return getOrEmptyMedia(nanogif);
            case MediaCollectionFormats.MP4:
                return getOrEmptyMedia(mp4);
            case MediaCollectionFormats.MP4_TINY:
                return getOrEmptyMedia(tinymp4);
            case MediaCollectionFormats.MP4_NANO:
                return getOrEmptyMedia(nanomp4);
            case MediaCollectionFormats.MP4_LOOPED:
                return getOrEmptyMedia(loopedmp4);
            case MediaCollectionFormats.WEBM:
                return getOrEmptyMedia(webm);
            case MediaCollectionFormats.WEBM_TINY:
                return getOrEmptyMedia(tinywebm);
            case MediaCollectionFormats.WEBM_NANO:
                return getOrEmptyMedia(nanowebm);
            default:
                return new Media();
        }
    }

    @NonNull
    private static Media getOrEmptyMedia(@Nullable Media media) {
        return media != null ? media : new Media();
    }
}
