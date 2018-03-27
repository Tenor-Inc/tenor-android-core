package com.tenor.android.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;

/**
 * The session utility class
 */
public class CoreSessionUtils {
    
    private static final String DEVICE_PREF = "device_preferences";

    /**
     * Use {@link #KEY_ANON_ID} instead
     *
     * @deprecated
     */
    private static final String KEY_KEYBOARD_ID = "KEY_KEYBOARD_ID";
    private static final String KEY_ANON_ID = "KEY_ANON_ID";
    private static final String KEY_ANDROID_ADVERTISE_ID = "KEY_ANDROID_ADVERTISE_ID";

    protected static SharedPreferences getPreferences(@NonNull final Context context) {
        return context.getSharedPreferences(DEVICE_PREF, Context.MODE_PRIVATE);
    }

    protected static void remove(@NonNull final Context context, String... keys) {
        SharedPreferences.Editor edit = getPreferences(context).edit();
        for (String key : keys) {
            edit.remove(key);
        }
        edit.apply();
    }

    public static synchronized void setAnonId(@NonNull final Context context, @Nullable final String keyboardId) {
        if (TextUtils.isEmpty(keyboardId)) {
            return;
        }
        getPreferences(context).edit().putString(KEY_ANON_ID, keyboardId).apply();
    }

    public static synchronized String getAnonId(@NonNull final Context context) {

        final String id = getPreferences(context).getString(KEY_ANON_ID, StringConstant.EMPTY);
        if (!TextUtils.isEmpty(id)) {
            return id;
        }
        return migrateKeyboardId(context);
    }

    private static synchronized String migrateKeyboardId(@NonNull final Context context) {
        if (!getPreferences(context).contains(KEY_KEYBOARD_ID)) {
            return StringConstant.EMPTY;
        }

        // migrate id
        final String keyboardId = getPreferences(context).getString(KEY_KEYBOARD_ID, StringConstant.EMPTY);
        // migrate keyboard id to anon id
        setAnonId(context, keyboardId);
        // remove KEY_KEYBOARD_ID
        getPreferences(context).edit().remove(KEY_KEYBOARD_ID).apply();
        return keyboardId;
    }

    public static boolean hasAnonId(@NonNull final Context context) {
        return !TextUtils.isEmpty(getAnonId(context));
    }

    public static void setAndroidAdvertiseId(@NonNull final Context context,
                                             @NonNull final String aaid) {
        // empty string is used if aaid is not available
        final String id = StringConstant.getOrEmpty(aaid);
        getPreferences(context).edit().putString(KEY_ANDROID_ADVERTISE_ID, id).apply();
    }

    public static String getAndroidAdvertiseId(@NonNull final Context context) {
        return getPreferences(context).getString(KEY_ANDROID_ADVERTISE_ID, StringConstant.EMPTY);
    }
}