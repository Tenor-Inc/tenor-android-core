package com.tenor.android.core.loader.gif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.util.CoreWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class GifLoader extends GlideLoader {

    /**
     * Uses Glide to load a gif or image into an ImageView
     *
     * @param ctx    the subclass of {@link Context}
     * @param params the {@link GlideTaskParams}
     */
    public static <CTX extends Context, T extends ImageView> void loadGif(@NonNull CTX ctx,
                                                                          @NonNull GlideTaskParams<T> params) {
        loadGif(new WeakReference<>(ctx), params);
    }

    /**
     * Uses Glide to load a gif or image into an ImageView
     *
     * @param weakRef the {@link WeakReference} of a given subclass of {@link Context}
     * @param params  the {@link GlideTaskParams}
     */
    public static <CTX extends Context, T extends ImageView> void loadGif(@NonNull WeakReference<CTX> weakRef,
                                                                          @NonNull GlideTaskParams<T> params) {

        if (!CoreWeakReferenceUtils.isAlive(weakRef)) {
            return;
        }

        GifRequestBuilder<String> requestBuilder = Glide.with(weakRef.get()).load(params.getPath()).asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, params), params);
    }
}
