package com.tenor.android.core.listener;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.response.BaseError;

/**
 * Callback for asynchronous keyboard id API request is done
 */
public interface IKeyboardIdListener {

    /**
     * @param keyboardId the keyboard id
     */
    void onReceiveKeyboardIdSucceeded(@NonNull String keyboardId);

    /**
     * @param error the error
     */
    void onReceiveKeyboardIdFailed(@Nullable BaseError error);
}
