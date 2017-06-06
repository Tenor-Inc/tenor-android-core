package com.tenor.android.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tenor.android.core.model.impl.GlidePayload;
import com.tenor.android.core.model.impl.Media;

public abstract class AbstractGlideUtils {

    /**
     * Uses Glide to load a gif or image into an ImageView
     *
     * @param context the context
     * @param payload the {@link GlidePayload}
     */
    public static void loadGif(@Nullable final Context context,
                               @Nullable final GlidePayload payload) {

        if (context == null || payload == null) {
            return;
        }

        GifRequestBuilder<String> requestBuilder = Glide.with(context).load(payload.getPath()).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, payload), payload);
    }

    /**
     * Uses Glide to load image into an ImageView from {@link Context}
     *
     * @param context the context
     * @param payload the {@link GlidePayload}
     */
    public static void loadImage(@Nullable final Context context,
                                 @Nullable final GlidePayload payload) {

        if (context == null || payload == null) {
            return;
        }

        DrawableRequestBuilder<String> requestBuilder = Glide.with(context).load(payload.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, payload), payload);
    }

    protected static GenericRequestBuilder applyDimens(@NonNull final GenericRequestBuilder requestBuilder,
                                                       @NonNull final GlidePayload payload) {
        final Media media = payload.getMedia();
        if (media != null) {
            requestBuilder.override(media.getWidth(), media.getHeight());
        }
        return requestBuilder;
    }

    public static void load(@Nullable final GenericRequestBuilder requestBuilder,
                            @Nullable final GlidePayload payload) {

        if (requestBuilder == null || payload == null) {
            return;
        }

        if (payload.isThumbnail()) {
            requestBuilder.thumbnail(payload.getThumbnailMultiplier());
        }

        requestBuilder.placeholder(payload.getPlaceholder())
                .into(new GlideDrawableImageViewTarget(payload.getImageView()) {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (payload.getCurrentRetry() < payload.getMaxRetry()) {
                            load(requestBuilder, payload.incrementCurrentRetry());
                        } else {
                            payload.getListener().onLoadImageFailed(errorDrawable);
                        }
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        payload.getListener().onLoadImageSucceeded(resource);
                    }
                });
    }
}
