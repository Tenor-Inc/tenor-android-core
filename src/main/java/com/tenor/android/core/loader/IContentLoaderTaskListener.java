package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Listener for on content loader task completed
 */
public interface IContentLoaderTaskListener<T extends ImageView, R extends Drawable> {
    /**
     * Load task success case
     */
    void success(@NonNull T target, @NonNull R taskResult);

    /**
     * Load task fail case
     */
    void failure(@NonNull T target, @NonNull R errorResult);
}
