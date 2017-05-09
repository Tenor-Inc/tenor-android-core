package com.tenor.android.core.util;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Contains methods to control the status of a view
 */
public abstract class AbstractViewUtils {

    /**
     * Set visibility of given view to View.VISIBLE
     *
     * @param view the given view
     */
    public static boolean showView(@Nullable final View view) {
        if (view != null && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    /**
     * Set visibility of given view to View.GONE
     *
     * @param view the given view
     */
    public static boolean hideView(@Nullable final View view) {
        if (view != null && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    /**
     * Is the visibility of given view set to {@link View}.VISIBLE
     *
     * @param view the given view
     * @return true if view is visible
     */
    public static boolean isVisible(@Nullable final View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }
}
