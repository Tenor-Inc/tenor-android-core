package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideLoader {

    public static <T extends ImageView> void load(@NonNull final RequestBuilder requestBuilder,
                                                  @NonNull final GlideTaskParams<T> params) {
        requestBuilder.into(new DrawableImageViewTarget(params.getTarget()) {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (params.getCurrentRetry() < params.getMaxRetry()) {
                    params.incrementCurrentRetry();
                    load(requestBuilder, params);
                } else {
                    super.onLoadFailed(errorDrawable);
                    params.getListener().failure(params.getTarget(), errorDrawable);
                }
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                params.getListener().success(params.getTarget(), resource);
            }
        });
    }
}
