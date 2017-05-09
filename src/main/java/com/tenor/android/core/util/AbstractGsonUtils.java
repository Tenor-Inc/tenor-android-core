package com.tenor.android.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The Gson utility class
 */
public abstract class AbstractGsonUtils {

    private static Gson sGson;

    public static Gson getInstance() {
        if (sGson == null) {
            sGson = new GsonBuilder().create();
        }
        return sGson;
    }
}
