package com.tenor.android.core.listener;

/**
 * Listener for checking when image loading has completed
 */
public interface ILoadImageListener {
    /**
     * Image load success case
     */
    void onLoadImageSucceeded();

    /**
     * Image load fail case
     */
    void onLoadImageFailed();
}
