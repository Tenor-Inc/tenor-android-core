package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ViewAction {

    @Retention(RetentionPolicy.CLASS)
    @StringDef({VIEW, SHARE, TAP})
    public @interface Value {
    }

    public static final String VIEW = "view";
    public static final String SHARE = "share";
    public static final String TAP = "tap";
}
