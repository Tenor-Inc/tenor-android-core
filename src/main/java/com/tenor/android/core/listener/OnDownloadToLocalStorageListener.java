package com.tenor.android.core.listener;

import android.support.annotation.NonNull;

/**
 * Handles actions after on write completed in local storage
 */
public interface OnDownloadToLocalStorageListener {
    void success(@NonNull String path);

    void failure(@NonNull Throwable throwable);
}
