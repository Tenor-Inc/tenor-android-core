package com.tenor.android.core.loader.asset;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tenor.android.core.util.CoreWeakReferenceUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Uses the Glide library to load gifs into a specified ImageView
 */
public class AssetLoader {

    /**
     * Uses Glide to load a local gif asset into an ImageView
     *
     * @param ctx       the subclass of {@link Context}
     * @param imageView where the gif will load into
     * @param assetPath local path of the gif asset
     */
    public static <CTX extends Context> void loadAsset(@NonNull CTX ctx,
                                                       @NonNull ImageView imageView,
                                                       @NonNull String assetPath) {
        loadAsset(new WeakReference<>(ctx), imageView, assetPath);
    }

    /**
     * Uses Glide to load a local gif asset into an ImageView
     *
     * @param weakRef   the {@link WeakReference} of a given subclass of {@link Context}
     * @param imageView where the gif will load into
     * @param assetPath local path of the gif asset
     */
    public static <CTX extends Context> void loadAsset(@NonNull WeakReference<CTX> weakRef,
                                                       @NonNull ImageView imageView,
                                                       @NonNull String assetPath) {

        if (!CoreWeakReferenceUtils.isAlive(weakRef)) {
            return;
        }

        byte[] bytes = toByteArray(weakRef.get(), assetPath);
        if (bytes == null) {
            return;
        }

        Glide.with(weakRef.get()).load(bytes).asGif().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Nullable
    public static byte[] toByteArray(@NonNull Context context, @NonNull String assetPath) {
        byte[] data = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream is = context.getAssets().open(assetPath);

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[65536];
            while (is.read(buffer) != -1) {
                outputStream.write(buffer);
            }

            data = outputStream.toByteArray();
            outputStream.close();
            is.close();
        } catch (IOException ignored) {
        }
        return data;
    }
}
