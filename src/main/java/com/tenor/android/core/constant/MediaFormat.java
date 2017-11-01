package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The supported media formats
 */
public class MediaFormat {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({ALL, GIF, JPEG, JPG, MP4, PNG})
    public @interface Value {
    }

    public static final String ALL = "all";
    public static final String GIF = "gif";
    public static final String MP4 = "mp4";
    public static final String PNG = "png";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
}
