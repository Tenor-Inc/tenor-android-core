package com.tenor.android.core.loader.image;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlidePayload;
import com.tenor.android.core.util.AbstractUIUtils;

public abstract class ImageLoader extends GlideLoader {

    /**
     * Uses Glide to load image into an ImageView from {@link Context}
     *
     * @param context the context
     * @param payload the {@link GlidePayload}
     */
    public static void loadImage(@NonNull Context context,
                                 @NonNull GlidePayload payload) {

        if (context instanceof Activity && AbstractUIUtils.isActivityDestroyed((Activity) context)) {
            return;
        }

        DrawableRequestBuilder<String> requestBuilder = Glide.with(context).load(payload.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, payload), payload);
    }
}
