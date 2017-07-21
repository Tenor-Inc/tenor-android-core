package com.tenor.android.core.constant;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

import com.tenor.android.core.util.AbstractUIUtils;

public class ItemVisualPositions {

    public static final String UNKNOWN = StringConstant.UNKNOWN;
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";


    @ItemVisualPosition
    public static String parse(@Nullable Context context,
                               @IntRange(from = -1, to = Integer.MAX_VALUE) final int spanIndex) {
        if (context == null) {
            return UNKNOWN;
        }
        return parse(spanIndex, AbstractUIUtils.isRightToLeft(context));
    }

    @ItemVisualPosition
    public static String parse(@IntRange(from = -1, to = Integer.MAX_VALUE) final int spanIndex,
                               final boolean rtl) {
        switch (spanIndex) {
            case 0:
                // item on left
                return !rtl ? LEFT : RIGHT;
            case 1:
                // item on right
                return !rtl ? RIGHT : LEFT;
            case -1:
                // GridLayoutManager.LayoutParams.INVALID_SPAN_ID
                // StaggeredGridLayoutManager.LayoutParams.INVALID_SPAN_ID
            default:
                return UNKNOWN;
        }
    }
}
