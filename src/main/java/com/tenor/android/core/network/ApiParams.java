package com.tenor.android.core.network;

import java.io.Serializable;


public class ApiParams implements Serializable {

    private static final long serialVersionUID = 7827279022709337862L;

    public static final String API_ENDPOINT_FORMATTER = "%1$s://%2$s.tenor.com/v1/";

    /**
     * Connection timeout in seconds
     */
    public static final int TIMEOUT_SLOW_CONNECTION = 30;

    /**
     * Connection timeout in seconds
     */
    public static final int TIMEOUT_FAST_CONNECTION = 15;

    public static final String HTTP = "http";
    public static final String HTTPS = "https";

    public static final String SERVER_API = "api";

//    @NonNull
//    private static String sApiKey = StringConstant.EMPTY;
//
//    public static void setApiKey(@NonNull String apiKey) {
//        if (TextUtils.isEmpty(apiKey)) {
//            throw new IllegalStateException("API key cannot be null or empty");
//        }
//        sApiKey = apiKey;
//    }
//
//    /**
//     * Get API key.  Must be called after {@link #setApiKey(String)}
//     */
//    @NonNull
//    public static String getApiKey() {
//        if (TextUtils.isEmpty(sApiKey)) {
//            throw new IllegalStateException("API key cannot be null or empty.");
//        }
//        return sApiKey;
//    }
}
