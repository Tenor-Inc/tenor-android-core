package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * Listener of loading {@link Drawable} task is completed
 */
public interface IDrawableLoaderTaskListener<T extends ImageView, R extends Drawable> {
    /**
     * Load task success case
     */
    void success(@NonNull T target, @NonNull R taskResult);

    /**
     * Load task fail case
     */
    void failure(@NonNull T target, @Nullable R errorResult);
}
