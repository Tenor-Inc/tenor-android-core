package com.tenor.android.core.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tenor.android.core.model.impl.GlidePayload;

public abstract class AbstractGlideUtils {

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
                            return;
                        }
                        if (payload.hasListener()) {
                            payload.getListener().onLoadImageFailed();
                        }
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        if (payload.hasListener()) {
                            payload.getListener().onLoadImageSucceeded();
                        }
                    }
                });
    }
}
