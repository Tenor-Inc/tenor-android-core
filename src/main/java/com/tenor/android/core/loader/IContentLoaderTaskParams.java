package com.tenor.android.core.loader;


import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * @param <T> the type of the target to load resource into
 */
public interface IContentLoaderTaskParams<T extends ImageView, R extends Drawable> extends ITaskParams {

    /**
     * @return the target to load the resource into
     */
    @NonNull
    T getTarget();

    /**
     * @return the drawable to display as a placeholder
     */
    @NonNull
    R getPlaceholder();

    /**
     * @return a file path, or a uri or url
     */
    @NonNull
    String getPath();

    @NonNull
    IContentLoaderTaskListener<T, Drawable> getListener();
}
