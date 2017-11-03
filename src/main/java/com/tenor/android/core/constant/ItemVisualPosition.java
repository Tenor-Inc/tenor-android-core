package com.tenor.android.core.constant;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.tenor.android.core.util.AbstractUIUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ItemVisualPosition {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({UNKNOWN, LEFT, RIGHT})
    public @interface Value {
    }

    public static final String UNKNOWN = StringConstant.UNKNOWN;
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    @Value
    public static String parse(@Nullable Context context,
                               @IntRange(from = -1, to = Integer.MAX_VALUE) final int spanIndex) {
        if (context == null) {
            return UNKNOWN;
        }
        return parse(spanIndex, AbstractUIUtils.isRightToLeft(context));
    }

    @Value
    public static String parse(@IntRange(from = -1, to = Integer.MAX_VALUE) final int spanIndex,
                               final boolean rtl) {
        switch (spanIndex) {
            case -1:
                // GridLayoutManager.LayoutParams.INVALID_SPAN_ID
                // StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID
                return UNKNOWN;
            case 0:
                // item on left
                return !rtl ? LEFT : RIGHT;
            default:
                // item on right
                return !rtl ? RIGHT : LEFT;
        }
    }
}
