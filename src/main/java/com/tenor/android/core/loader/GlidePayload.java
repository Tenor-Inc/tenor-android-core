package com.tenor.android.core.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.tenor.android.core.model.impl.Media;

/**
 * Configuration of loading a gif
 */
public class GlidePayload extends LoaderPayload {

    private static final long serialVersionUID = -5195385185012871394L;

    @Nullable
    private Media mMedia;

    private int mWidth = Target.SIZE_ORIGINAL;
    private int mHeight = Target.SIZE_ORIGINAL;

    public GlidePayload(@NonNull ImageView imageView,
                        @NonNull String path) {
        super(imageView, path);
    }

    @Nullable
    public Media getMedia() {
        return mMedia;
    }

    public GlidePayload setMedia(@Nullable final Media media) {
        if (media != null) {
            mMedia = media;
            setWidth(media.getWidth());
            setHeight(media.getHeight());
        }
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public GlidePayload setWidth(int width) {
        if (width > 0) {
            this.mWidth = width;
        }
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public GlidePayload setHeight(int height) {
        if (height > 0) {
            this.mHeight = height;
        }
        return this;
    }
}
