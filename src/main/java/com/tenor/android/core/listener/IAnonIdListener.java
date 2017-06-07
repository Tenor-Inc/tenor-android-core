package com.tenor.android.core.listener;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.response.BaseError;

/**
 * Callback for asynchronous anon id API request is done
 */
public interface IAnonIdListener {

    /**
     * @param anonId the keyboard id
     */
    void onReceiveAnonIdSucceeded(@NonNull String anonId);

    /**
     * @param error the error
     */
    void onReceiveAnonIdFailed(@Nullable BaseError error);
}
