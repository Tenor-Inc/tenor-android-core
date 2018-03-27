package com.tenor.android.core.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {

    private static Gson sGson;

    public static Gson get() {
        if (sGson == null) {
            sGson = new GsonBuilder().create();
        }
        return sGson;
    }
}
