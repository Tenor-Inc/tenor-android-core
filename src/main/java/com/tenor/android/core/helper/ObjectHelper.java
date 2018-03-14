package com.tenor.android.core.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ObjectHelper {

    public static <T> T getOrDef(@Nullable T t, @NonNull T def) {
        return t != null ? t : def;
    }
}
