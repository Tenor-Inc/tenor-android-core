package com.tenor.android.core.network;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Callback for asynchronous anon id API request is done
 */
public interface IAnonIdListener {

    /**
     * @param anonId the keyboard id
     */
    void onReceiveAnonIdSucceeded(@NonNull String anonId);

    /**
     * @param throwable the {@link Throwable}
     */
    void onReceiveAnonIdFailed(@Nullable Throwable throwable);
}
