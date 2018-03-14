package com.tenor.android.core.network.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * The supported protocols formats
 */
public class Protocol {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({HTTP, HTTPS})
    @Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
    public @interface Value {
    }

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
}
