package com.tenor.android.core.loader.gif;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlidePayload;

public abstract class GifLoader extends GlideLoader {

    /**
     * Uses Glide to load a gif or image into an ImageView
     *
     * @param context the context
     * @param payload the {@link GlidePayload}
     */
    public static void loadGif(@NonNull Context context,
                               @NonNull GlidePayload payload) {

        GifRequestBuilder<String> requestBuilder = Glide.with(context).load(payload.getPath()).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, payload), payload);
    }
}
