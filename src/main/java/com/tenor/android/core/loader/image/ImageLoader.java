package com.tenor.android.core.loader.image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.loader.GlideLoader;
import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;

import java.lang.ref.WeakReference;

public abstract class ImageLoader extends GlideLoader {

    /**
     * Uses Glide to load image into an ImageView
     *
     * @param ctx    the subclass of {@link Context}
     * @param params the {@link GlideTaskParams}
     */
    public static <CTX extends Context, T extends ImageView> void loadImage(@NonNull CTX ctx,
                                                                            @NonNull GlideTaskParams<T> params) {
        loadImage(new WeakReference<>(ctx), params);
    }

    /**
     * Uses Glide to load image into an ImageView
     *
     * @param weakRef the {@link WeakReference} of a given subclass of {@link Context}
     * @param params  the {@link GlideTaskParams}
     */
    public static <CTX extends Context, T extends ImageView> void loadImage(@NonNull WeakReference<CTX> weakRef,
                                                                            @NonNull GlideTaskParams<T> params) {

        if (!AbstractWeakReferenceUtils.isAlive(weakRef)) {
            return;
        }

        DrawableRequestBuilder<String> requestBuilder = Glide.with(weakRef.get()).load(params.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        load(applyDimens(requestBuilder, params), params);
    }
}
