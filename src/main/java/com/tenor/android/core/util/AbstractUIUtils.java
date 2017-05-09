package com.tenor.android.core.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tenor.android.core.R;
import com.tenor.android.core.model.impl.Result;

import java.lang.reflect.Method;

/**
 * The UI utility class
 */
public abstract class AbstractUIUtils {

    /**
     * Get screen density
     *
     * @param context the context
     * @return the screen density
     */
    public static float getScreenDensity(@Nullable final Context context) {
        if (context == null) {
            return 0;
        }
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static void hideInputMethod(@Nullable final Activity activity) {
        hideInputMethod(activity, 0);
    }

    public static void hideInputMethod(@Nullable final View view) {
        hideInputMethod(view, 0);
    }

    /**
     * @param flags Provides additional operating flags.  Set to 0 if there are no additional flags.
     */
    public static void hideInputMethod(@Nullable final View view, int flags) {
        if (view == null || view.getContext() == null) {
            return;
        }

        final InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), flags);
    }

    /**
     * @param flags Provides additional operating flags.  Set to 0 if there are no additional flags.
     */
    public static void hideInputMethod(@Nullable final Activity activity, int flags) {
        if (activity == null || activity.getCurrentFocus() == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        view.clearFocus();
        hideInputMethod(view, flags);
    }

    /**
     * @param flags Provides additional operating flags.  Set to 0 if there are no additional flags.
     */
    public static void showInputMethod(@Nullable final Activity activity, int flags) {
        if (activity == null || activity.getCurrentFocus() == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        view.clearFocus();
        showInputMethod(view, flags);
    }

    public static void showInputMethod(@Nullable final View view) {
        showInputMethod(view, 0);
    }

    /**
     * @param flags Provides additional operating flags.  Set to 0 if there are no additional flags.
     */
    public static void showInputMethod(@Nullable final View view, int flags) {
        if (view == null || view.getContext() == null) {
            return;
        }

        final InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, flags);
    }

    /**
     * @param activity the activity
     * @return true if destroyed.  False if still alive, or call performed on device of API 16 and below
     * <p>
     * @targetApi 17
     * Calls Activity method isDestroyed() on supplied activity.
     */
    public static boolean isActivityDestroyed(@NonNull final Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && activity != null && activity.isDestroyed();
    }

    /**
     * @param context the context
     * @param dp      the density-independent pixels
     * @return the given value in pixels
     */
    public static int dpToPx(@NonNull final Context context,
                             @IntRange(from = 0, to = Integer.MAX_VALUE) final int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    /**
     * @param context the context
     * @param dp      the density-independent pixels
     * @return the given value in pixels
     */
    public static int dpToPx(@NonNull final Context context,
                             @FloatRange(from = 0f, to = Float.MAX_VALUE) final float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    /**
     * @param context the context
     * @param px      the pixels
     * @return the given value in density-independent pixels
     */
    public static int pxToDp(@NonNull final Context context,
                             @IntRange(from = 0, to = Integer.MAX_VALUE) final int px) {
        return Math.round(px / getScreenDensity(context));
    }

    /**
     * @param context the context
     * @param px      the pixels
     * @return the given value in density-independent pixels
     */
    public static int pxToDp(@NonNull final Context context,
                             @FloatRange(from = 0f, to = Float.MAX_VALUE) final float px) {
        return Math.round(px / getScreenDensity(context));
    }

    /**
     * @param context the context
     * @param sp      scale-independent pixels
     * @return the given value in pixels
     */
    public static int spToPx(@NonNull final Context context,
                             @IntRange(from = 0, to = Integer.MAX_VALUE) int sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()));
    }

    /**
     * Takes value stored as a <b>"dimen"</b> resource, and converts it into pixel length
     *
     * @param context        the context
     * @param dimensionResId <b>"dimen"</b> resource id name
     * @return conversion of the resource's value into pixel length
     */
    public static int getDimension(@NonNull final Context context, @DimenRes final int dimensionResId) {
        return Math.round(context.getResources().getDimension(dimensionResId));
    }

    /**
     * Converts and returns the supplied colorResId in the form 0xAARRGGBB
     *
     * @param context    the context
     * @param colorResId color resource id of the desired color value
     * @return A single color value in the form 0xAARRGGBB
     */
    @SuppressWarnings("deprecation")
    public static int getColor(@NonNull final Context context, @ColorRes final int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorResId);
        }
        return context.getResources().getColor(colorResId);
    }

    /**
     * Get status bar height
     *
     * @param context the context
     * @return the height of status bar
     */
    public static int getStatusBarHeight(@Nullable final Context context) {
        if (context == null) {
            return 0;
        }

        if (!hasOnScreenSystemBar(context)) {
            return 0;
        }

        int result = 0;
        try {
            final int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * Get screen width in pixels
     *
     * @param context the context
     * @return the screen width in pixels
     */
    public static int getScreenWidth(@Nullable final Context context) {
        if (context == null) {
            return 0;
        }
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * Get screen height in pixels
     *
     * @param context the context
     * @return the screen height in pixels
     */
    public static int getScreenHeight(@Nullable final Context context) {
        if (context == null) {
            return 0;
        }
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * Get screen height in pixels
     *
     * @param context the context
     * @return true if there is system bar on screen
     */
    public static boolean hasOnScreenSystemBar(@NonNull final Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display display = wm.getDefaultDisplay();
        int displayHeight = 0;
        try {
            final Method getRawHeight = Display.class.getMethod("getRawHeight");
            displayHeight = (Integer) getRawHeight.invoke(display);
        } catch (Exception ignored) {
        }

        int uiHeight = getScreenHeight(context);

        return displayHeight - uiHeight > 0;
    }

    /**
     * Calculate the height of a item to be display on {@link android.support.v7.widget.StaggeredGridLayoutManager}
     *
     * @param context     the context
     * @param width       the raw width of an item, usually less than the width needed to be properly displayed on screen
     * @param height      the raw height of an item, usually less than the height needed to be properly displayed on screen
     * @param columnCount the number of columns of {@link android.support.v7.widget.StaggeredGridLayoutManager}
     * @param padding     the padding in pixel
     */
    public static int getAdjustedStaggeredGridItemHeight(@NonNull final Context context,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int width,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int height,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int columnCount,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int padding) {
        return getAdjustedStaggeredGridItemHeight(context, (float) width / height, columnCount, padding);
    }

    /**
     * Calculate the height of a item to be display on {@link android.support.v7.widget.StaggeredGridLayoutManager}
     *
     * @param context     the context
     * @param aspectRatio the aspectRatio of an item
     * @param columnCount the number of columns of {@link android.support.v7.widget.StaggeredGridLayoutManager}, either 1 or 2
     * @param padding     the padding in pixel
     */
    public static int getAdjustedStaggeredGridItemHeight(@NonNull final Context context,
                                                         @FloatRange(from = 0.01f, to = 5.01f) final float aspectRatio,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int columnCount,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) final int padding) {
        final int column = columnCount > 0 ? columnCount : 1;
        final int topBottomPadding = 2 * padding;
        final double screenWidthPerColumn = (getScreenWidth(context) / column) - 1.5 * padding;
        final double screenHeightPerColumn = screenWidthPerColumn / aspectRatio;
        return (int) Math.ceil(screenHeightPerColumn + topBottomPadding);
    }

    /**
     * Calculate the height of a item to be display on {@link android.support.v7.widget.StaggeredGridLayoutManager}
     *
     * @param context     the context
     * @param result      the {@link Result}
     * @param columnCount the number of columns of {@link android.support.v7.widget.StaggeredGridLayoutManager}
     * @param padding     the padding in pixel
     */
    public static int getAdjustedStaggeredGridItemHeight(@Nullable final Context context,
                                                         @Nullable final Result result,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) int columnCount,
                                                         @IntRange(from = 0, to = Integer.MAX_VALUE) int padding) {
        if (context == null || result == null) {
            return 0;
        }

        if (AbstractListUtils.isEmpty(result.getMedias())
                || result.getMedias().get(0) == null
                || result.getMedias().get(0).getGif() == null) {
            return 0;
        }

        return getAdjustedStaggeredGridItemHeight(context,
                result.getMedias().get(0).getGif().getWidth(),
                result.getMedias().get(0).getGif().getHeight(),
                columnCount, padding);
    }

    /**
     * Check if the device is on landscape mode
     *
     * @param context the context
     * @return true if the device is on landscape mode
     */
    public static boolean isLandscape(@Nullable final Context context) {
        return context != null && context.getResources().getBoolean(R.bool.landscape);
    }

    /**
     * Check if the device is on right to left mode
     *
     * @param context the context
     * @return true if the device is on right to left mode
     */
    public static boolean isRightToLeft(@Nullable final Context context) {
        return context != null && context.getResources().getBoolean(R.bool.right_to_left);
    }
}