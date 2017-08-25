package com.tenor.android.core.network.constant;

import android.support.annotation.Nullable;

/**
 * The supported protocols formats
 */
public class Protocols {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    @Protocol
    public static String getOrHttps(@Nullable String protocol) {
        switch (protocol) {
            case HTTP:
                return HTTP;
            case HTTPS:
            default:
                return HTTPS;

        }
    }
}
