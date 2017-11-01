package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The supported media formats
 */
public class MediaCollectionFormat {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({GIF, GIF_TINY, GIF_MEDIUM, GIF_NANO,
            MP4, MP4_TINY, MP4_NANO, MP4_LOOPED,
            WEBM, WEBM_TINY, WEBM_NANO
    })
    public @interface Value {
    }
    
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
}
