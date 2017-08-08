package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.CLASS)
@StringDef({MediaCollectionFormats.GIF, MediaCollectionFormats.GIF_TINY,
        MediaCollectionFormats.GIF_MEDIUM, MediaCollectionFormats.GIF_NANO,
        MediaCollectionFormats.MP4, MediaCollectionFormats.MP4_TINY,
        MediaCollectionFormats.MP4_NANO, MediaCollectionFormats.MP4_LOOPED,
        MediaCollectionFormats.WEBM, MediaCollectionFormats.WEBM_TINY,
        MediaCollectionFormats.WEBM_NANO
})
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface MediaCollectionFormat {
}
