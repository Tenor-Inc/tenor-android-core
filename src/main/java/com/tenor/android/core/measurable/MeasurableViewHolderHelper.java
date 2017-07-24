package com.tenor.android.core.measurable;


import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.util.AbstractUIUtils;

public class MeasurableViewHolderHelper {

    /**
     * Get the visible fraction of the given  {@link View} in the given {@link RecyclerView}
     *
     * @param recyclerView the given {@link RecyclerView}
     * @param itemView     the given {@link View}
     * @param threshold    the percentage of a view's area to be shown in order to consider it as being viewed
     * @return the visible fraction of the given  {@link View} in the given {@link RecyclerView}
     */
    @FloatRange(from = 0.01f, to = 1f)
    public static float calculateVisibleFraction(@NonNull RecyclerView recyclerView,
                                                 @NonNull View itemView,
                                                 float threshold) {
        /*
         * Special distributive measuring approach to handle the following issues:
         *
         * [ANDROID-1776]
         * view just went off visible region will report visible width equals to
         * the actual width of the width due to pre-caching
         *
         * [ANDROID-1786]
         * Incorrect reporting of in/visible status on some OEMs
         * issue caused by OEM custom implementation on how to define
         * the left and right of a off visible region
         *
         * Stock OS define off visible region left as negative number,
         * whereas some other OEM defines it as 0, so use #getLocationInWindow() instead
         */
        Rect ivRect = new Rect();
        itemView.getGlobalVisibleRect(ivRect);
        if (ivRect.isEmpty()) {
            return 0.01f;
        }

        Rect rvRect = new Rect();
        recyclerView.getGlobalVisibleRect(rvRect);

        int[] location = new int[2];
        itemView.getLocationInWindow(location);

        final boolean rtl = AbstractUIUtils.isRightToLeft(recyclerView.getContext());
        final int width = itemView.getMeasuredWidth();
        final int height = itemView.getMeasuredHeight();

        // ratio for left bound
        final float leftBoundRatio;
        if (!rtl) {
            // left to right languages
            leftBoundRatio = 1f - threshold;
        } else {
            // right to left languages
            leftBoundRatio = threshold;
        }

        final int left = (int) (rvRect.left - leftBoundRatio * width);
        final int right = (int) (rvRect.right - (1f - leftBoundRatio) * width);
        final int top = (int) (rvRect.top - (1f - threshold) * height);
        final int bottom = (int) (rvRect.bottom - threshold * height);

        // horizontally out of bound
        final boolean hoob = location[0] < left || location[0] > right;

        // vertically out of bound
        final boolean voob = location[1] < top || location[1] > bottom;
        if (hoob || voob) {
            return 0.01f;
        }

        float wRatio = (float) ivRect.width() / width;
        float hRatio = (float) ivRect.height() / height;

        /*
         * the non-moving direction will be always 1.0f,
         * so the direction of interest is the smaller one of the two,
         * and the final number is capped between 0.01f and 1.0f
         */
        float percentage = Math.min(wRatio, hRatio);
        percentage = Math.min(percentage, 1f);
        percentage = Math.max(percentage, 0.01f);
        return percentage;
    }
}
