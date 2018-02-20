package com.tenor.android.core.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class AaidService {

    public static final int JOB_ID_GET_AAID = 0;
    public static final String ACTION_GET_AAID = "ACTION_GET_AAID";

    public static void requestAaid(@NonNull Context context) {
        requestAaid(context, null);
    }

    public static void requestAaid(@NonNull Context context, @Nullable IAaidListener listener) {

        try {
            AaidServiceApi26.requestAaid(context, listener);
        } catch (NoClassDefFoundError ignored) {
            /*
             * [ANDROID-2581]
             *
             * This is to catch a fatal runtime `NoClassDefFoundError` due to compiling a apk file
             * with `compileSdkVersion` and `support-compat` less than 26; the `JobIntentService`
             * required by the `AaidServiceApi26` is only available since 26.
             */
            AaidServiceCompat.requestAaid(context, listener);
        }
    }
}