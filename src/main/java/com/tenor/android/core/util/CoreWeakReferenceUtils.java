package com.tenor.android.core.util;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * The weak reference utility class
 */
public class CoreWeakReferenceUtils {

    public static <T> boolean isAlive(@Nullable final WeakReference<T> weakReference) {
        return weakReference != null && weakReference.get() != null;
    }
}
