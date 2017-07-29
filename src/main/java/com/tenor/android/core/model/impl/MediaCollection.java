package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * The response model of the media collection object, which contains different supported formats of a gif
 */
public class MediaCollection implements Serializable {
    private static final long serialVersionUID = -8824214919408809561L;

    /**
     * Standard GIF format
     */
    public static final String GIF = "GIF";
    /**
     * Compressed gif.  Good for quick loading and sending
     */
    public static final String GIF_TINY = "GIF_TINY";
    public static final String GIF_MEDIUM = "GIF_MEDIUM";
    public static final String GIF_NANO = "GIF_NANO";


    /**
     * Standard mp4 format
     */
    public static final String MP4 = "MP4";
    /**
     * Compressed mp4 format.  Good for quick loading and sending
     */
    public static final String MP4_TINY = "MP4_TINY";
    public static final String MP4_NANO = "MP4_NANO";
    /**
     * Looped mp4 format that contains 3 loops built in
     */
    public static final String MP4_LOOPED = "MP4_LOOPED";


    /**
     * Web player url
     */
    public static final String WEBM = "WEBM";
    public static final String WEBM_TINY = "WEBM_TINY";
    public static final String WEBM_NANO = "WEBM_NANO";

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

    @NonNull
    public Media get(@Nullable String type) {
        if (type == null) {
            return new Media();
        }

        switch (type) {
            case GIF:
                return getOrEmptyMedia(gif);
            case GIF_TINY:
                return getOrEmptyMedia(tinygif);
            case GIF_MEDIUM:
                return getOrEmptyMedia(mediumgif);
            case GIF_NANO:
                return getOrEmptyMedia(nanogif);
            case MP4:
                return getOrEmptyMedia(mp4);
            case MP4_TINY:
                return getOrEmptyMedia(tinymp4);
            case MP4_NANO:
                return getOrEmptyMedia(nanomp4);
            case MP4_LOOPED:
                return getOrEmptyMedia(loopedmp4);
            case WEBM:
                return getOrEmptyMedia(webm);
            case WEBM_TINY:
                return getOrEmptyMedia(tinywebm);
            case WEBM_NANO:
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
