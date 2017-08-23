package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tenor.android.core.model.impl.Media;

public class GlideLoader {

    public static GenericRequestBuilder applyDimens(@NonNull GenericRequestBuilder requestBuilder,
                                                    @NonNull GlideTaskParams payload) {
        final Media media = payload.getMedia();
        if (media != null) {
            requestBuilder.override(media.getWidth(), media.getHeight());
        }
        return requestBuilder;
    }

    public static <T extends ImageView> void load(@NonNull final GenericRequestBuilder requestBuilder,
                                                  @NonNull final GlideTaskParams<T> payload) {

        if (payload.isThumbnail()) {
            requestBuilder.thumbnail(payload.getThumbnailMultiplier());
        }

        requestBuilder.placeholder(payload.getPlaceholder())
                .into(new GlideDrawableImageViewTarget(payload.getTarget()) {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (payload.getCurrentRetry() < payload.getMaxRetry()) {
                            payload.incrementCurrentRetry();
                            load(requestBuilder, payload);
                        } else {
                            super.onLoadFailed(e, errorDrawable);
                            payload.getListener().failure(payload.getTarget(), errorDrawable);
                        }
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        payload.getListener().success(payload.getTarget(), resource);
                    }
                });
    }
}
