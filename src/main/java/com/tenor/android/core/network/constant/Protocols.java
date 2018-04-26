package com.tenor.android.core.network.constant;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * The supported protocols formats
 */
public class Protocols {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    @Protocol
    public static String getOrHttps(@Nullable String protocol) {
        if (TextUtils.isEmpty(protocol)) {
            return HTTPS;
        }

        switch (protocol) {
            case HTTP:
                return HTTP;
            case HTTPS:
            default:
                return HTTPS;

        }
    }
}
