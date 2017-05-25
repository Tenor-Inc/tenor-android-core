package com.tenor.android.core.listener;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Listener for checking when image loading has completed
 */
public interface ILoadImageListener {
    /**
     * Image load success case
     */
    void onLoadImageSucceeded(@Nullable Drawable drawable);

    /**
     * Image load fail case
     */
    void onLoadImageFailed(@Nullable Drawable errorDrawable);
}
