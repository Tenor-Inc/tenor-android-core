package com.tenor.android.core.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.tenor.android.core.constant.StringConstant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Contains methods to determine the status of a given package
 * <p/>
 * Most of the methods in this class are heavy duties, avoid running them on UI thread
 */
public abstract class AbstractPackageManagerUtils {

    /**
     * Checks if two String package names are the same set of characters, and are equal
     *
     * @param packageNameLeft  String of the first package name
     * @param packageNameRight String of the second package name
     * @return true if both parameters are equal and non-null
     */
    public static boolean isPackageEquals(@NonNull String packageNameLeft, @NonNull String packageNameRight) {
        return !TextUtils.isEmpty(packageNameLeft) && !TextUtils.isEmpty(packageNameRight) && packageNameLeft.equals(packageNameRight);
    }

    /**
     * Checks if the app of the given package name is installed on the device
     *
     * @param context     the context
     * @param packageName package name of the <b>app</b>
     * @return true if app is installed on the device
     */
    public static boolean isPackageInstalled(@NonNull final Context context, @NonNull final String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return isAllPackageInstalled(context, packageName);
        }
    }

    /**
     * Takes one or more Strings of app package names, and checks if <i>all</i> of them are installed on the device
     *
     * @param context      the context
     * @param packageNames One or more Strings of package names of the <b>apps</b>
     * @return true if every app is installed on the device
     */
    public static boolean isAllPackageInstalled(@NonNull Context context, @NonNull String... packageNames) {
        //noinspection ConstantConditions
        if (context == null || packageNames.length == 0) {
            return false;
        }

        Set<String> installedPackageNames = AbstractSessionUtils.getInstalledPackages(context);

        // check if there is exact match on package name
        for (String packageName : packageNames) {
            if (!installedPackageNames.contains(packageName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Takes one or more Strings of app package names, and checks if <i>any</i> of them are installed on the device
     *
     * @param context      the context
     * @param packageNames One or more Strings of package names of the <b>apps</b>
     * @return true if any app is installed on the device
     */
    public static boolean isAnyPackageInstalled(@NonNull Context context, @NonNull String... packageNames) {
        //noinspection ConstantConditions
        if (context == null || packageNames.length == 0) {
            return false;
        }

        Set<String> installedPackageNames = AbstractSessionUtils.getInstalledPackages(context);

        // check if there is exact match on package name
        for (String packageName : packageNames) {
            if (installedPackageNames.contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all packages that are installed into the device
     * <p>
     * This method needs to be called before any other methods in the class
     */
    @WorkerThread
    public static Set<String> getInstalledPackages() {
//        final PackageManager packageManager = context.getPackageManager();
//        try {
//            return packageManager.getInstalledPackages(flags);
//        } catch (Exception ignored) {
//        }

        /*
         * Alternative to handle non-fatal TransactionTooLargeException
         */
        Process process;
        final Set<String> installedPackageNames = new HashSet<>();
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec("pm list packages");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String packageName = line.substring(line.indexOf(':') + 1);
                installedPackageNames.add(packageName);
            }
            process.waitFor();
        } catch (Exception ignored) {
        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {
                }
        }
        return installedPackageNames;
    }

    /**
     * Get all installed package names that contains the given package prefix
     *
     * @param context  the context
     * @param prefixes package prefix of the <b>apps</b>
     * @return true if any app is installed on the device
     */
    public static List<String> getInstalledPackageContains(@NonNull Context context, @NonNull String... prefixes) {
        //noinspection ConstantConditions
        if (context == null || prefixes.length == 0) {
            return Collections.emptyList();
        }

        ArrayList<String> result = new ArrayList<>();

        Set<String> installedPackageNames = AbstractSessionUtils.getInstalledPackages(context);
        /*
         * the reason to use double for loop here is to take care cases,
         * such as package name "com.abc.def" and prefix "com.abc"
         */
        for (String installedPackageName : installedPackageNames) {
            for (String prefix : prefixes) {
                if (installedPackageName.contains(prefix)) {
                    result.add(installedPackageName);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Looks up the version code of a given String package name.
     * App must be installed on the phone
     *
     * @param context     the context
     * @param packageName String of the package name of the app being checked
     * @return the version of the app, or -1 if not found
     */
    public static int getVersionCode(@Nullable final Context context, @Nullable final String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return -1;
        }

        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException ignored) {
            return -1;
        } catch (RuntimeException ignored) {
            /*
             * For ANDROID-713
             *
             * This is for catching the DeadObjectException, which is likely caused by the
             * android.app.ApplicationPackageManager.getPackageInfo() OR client supplying an
             * inappropriate android.content.Context, which got terminated before the task is completed
             */
            return -1;
        }
        return packageInfo != null ? packageInfo.versionCode : -1;
    }

    /**
     * Get the version name of a given package name, which the package must be already installed
     * on the phone
     *
     * @param context     the context
     * @param packageName String of the package name of the app being checked
     * @return the version of the app, or -1 if not found
     */
    public static String getVersionName(@Nullable final Context context, @Nullable final String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return StringConstant.EMPTY;
        }

        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException ignored) {
            return StringConstant.EMPTY;
        }
        return packageInfo != null ? packageInfo.versionName : StringConstant.EMPTY;
    }


    /**
     * Find the "Just once" button between API 16 and 23 of a Activity/Action Resolver
     *
     * @param context given context
     * @param buttons possible buttons
     * @return the "Just once" button
     */
    @NonNull
    protected static AccessibilityNodeInfoCompat findJustOnceButton(@NonNull final Context context,
                                                                    @NonNull final List<AccessibilityNodeInfoCompat> buttons) {
        /*
         * It utilizes the "activity_resolver_use_once" field of the string resources in android
         * system, and therefore, taking care of localization as well.
         *
         * It perform string comparison between the string of "activity_resolver_use_once" and the
         * text of the accessibility node.
         *
         * "activity_resolver_use_once" was added to android system on May 06, 2012 through
         * commit c587861, so it should support API 16 and up.
         *
         * You can review the commit in here:
         * https://android.googlesource.com/platform/frameworks/base/+/c587861%5E%21/#F4
         */
        String justOnceButtonText = StringConstant.getString(context, "android", "activity_resolver_use_once");

        boolean isStrongStationaryConditionSatisfied;
        boolean isWeakStationaryConditionSatisfied;
        final Locale locale = AbstractLocaleUtils.getEnUsLocale();
        for (AccessibilityNodeInfoCompat button : buttons) {

            isStrongStationaryConditionSatisfied = !TextUtils.isEmpty(button.getText())
                    && !TextUtils.isEmpty(justOnceButtonText)
                    && justOnceButtonText.toLowerCase(locale).equals(button.getText().toString().toLowerCase(locale));

            isWeakStationaryConditionSatisfied = !TextUtils.isEmpty(button.getViewIdResourceName())
                    && button.getViewIdResourceName().toLowerCase(locale).contains("once");

            if (isStrongStationaryConditionSatisfied || isWeakStationaryConditionSatisfied) {
                return button;
            }
        }

        /*
         * Set default chosen button to the second button because that is how API 16 & 17 preferred
         *
         * Note that the order of the buttons has been changed since API 21, but such change has been
         * taken care by the more reliable "isStrongStationaryConditionSatisfied" boolean.
         */
        return buttons.get(1);
    }

    /**
     * Check if the keyboard is enabled.
     *
     * @return true if this keyboard is enabled.
     */
    public static boolean isKeyboardEnabled(@NonNull final Context context,
                                            @NonNull final String packageName) {
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return isKeyboardEnabled(inputMethodManager, packageName);
    }

    /**
     * Check if the keyboard is enabled.
     *
     * @return true if this keyboard is enabled.
     */
    public static boolean isKeyboardEnabled(@NonNull final InputMethodManager inputMethodManager,
                                            @NonNull final String packageName) {
        for (final InputMethodInfo imi : inputMethodManager.getEnabledInputMethodList()) {
            if (packageName.equals(imi.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if Google Play is installed in this device
     *
     * @param context given context
     */
    public static boolean isGooglePlayInstalled(@NonNull final Context context) {
        PackageManager pm = context.getPackageManager();
        boolean hasGooglePlay = false;
        try {
            PackageInfo info = pm.getPackageInfo("com.android.vending", PackageManager.GET_ACTIVITIES);
            String label = (String) info.applicationInfo.loadLabel(pm);
            hasGooglePlay = !"Market".equals(label);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return hasGooglePlay;
    }
}
