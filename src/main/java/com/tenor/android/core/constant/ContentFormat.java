package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The supported content formats
 */
public class ContentFormat {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({IMAGE_GIF, IMAGE_MP4, IMAGE_JPEG, IMAGE_PNG})
    public @interface Value {
    }

    /**
     * "image/gif"
     */
    public static final String IMAGE_GIF = "image/" + MediaFormat.GIF;

    /**
     * "image/jpeg"
     */
    public static final String IMAGE_JPEG = "image/" + MediaFormat.JPEG;

    /**
     * "image/png"
     */
    public static final String IMAGE_PNG = "image/" + MediaFormat.PNG;

    /**
     * "image/mp4"
     */
    public static final String IMAGE_MP4 = "video/" + MediaFormat.MP4;
}
