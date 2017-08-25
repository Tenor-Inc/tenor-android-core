package com.tenor.android.core.loader.gif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlideTaskParams;

public abstract class GifLoader extends GlideLoader {

    /**
     * Uses Glide to load a gif or image into an ImageView
     *
     * @param context the context
     * @param payload the {@link GlideTaskParams}
     */
    public static <T extends ImageView> void loadGif(@NonNull Context context,
                                                     @NonNull GlideTaskParams<T> payload) {

        GifRequestBuilder<String> requestBuilder = Glide.with(context).load(payload.getPath()).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, payload), payload);
    }
}
