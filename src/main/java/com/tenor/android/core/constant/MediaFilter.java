package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaFilter {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({MINIMAL, BASIC})
    public @interface Value {
    }

    public static final String MINIMAL = "minimal";
    public static final String BASIC = "basic";
}
