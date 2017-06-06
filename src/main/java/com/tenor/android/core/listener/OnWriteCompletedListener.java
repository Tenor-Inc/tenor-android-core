package com.tenor.android.core.listener;

import android.support.annotation.NonNull;

/**
 * Handles actions after on write completed in local storage
 */
public interface OnWriteCompletedListener {
    void onWriteSucceeded(@NonNull String path);

    void onWriteFailed(@NonNull Throwable throwable);
}
