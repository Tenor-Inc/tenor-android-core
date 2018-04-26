package com.tenor.android.core.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
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
import com.tenor.android.core.checker.ScriptDirectionChecker;

import java.lang.reflect.Method;

/**
 * The UI utility class
 */
public abstract class AbstractUIUtils {

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
    public static boolean isActivityDestroyed(@Nullable Activity activity) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && activity != null && activity.isDestroyed();
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
     * @param sp      scale-independent pixels
     * @return the given value in pixels
     */
    public static int spToPx(@NonNull final Context context,
                             @FloatRange(from = 0f, to = Float.MAX_VALUE) float sp) {
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
     * Get status bar height
     *
     * @param context the context
     * @return the height of status bar
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        return getStatusBarHeight(context, 0);
    }

    /**
     * Get status bar height
     *
     * @param context the context
     * @param defVal  the default value if the height is not found
     * @return the height of status bar
     */
    public static int getStatusBarHeight(@NonNull Context context,
                                         @IntRange(from = 0) int defVal) {
        try {
            final int resourceId = context.getResources()
                    .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Throwable ignored) {
        }
        return defVal;
    }

    /**
     * Get status bar height
     *
     * @param view the view
     * @return the height of status bar
     */
    public static int getStatusBarHeight(@NonNull View view) {
        if (Build.VERSION.SDK_INT < 21) {
            return getStatusBarHeight(view.getContext(), dpToPx(view.getContext(), 24));
        }

        // this approach only works for API 21+
        final View v = view.getRootView().findViewById(android.R.id.statusBarBackground);
        if (v != null) {
            return v.getHeight();
        } else {
            // default status bar height in most cases is 24dp
            return dpToPx(view.getContext(), 24);
        }
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
        if (wm == null) {
            return true;
        }

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
        return ScriptDirectionChecker.checkSelfScriptDirection(context) == ScriptDirectionChecker.RIGHT_TO_LEFT;
    }
}