package com.tenor.android.core.loader.asset;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Uses the Glide library to load gifs into a specified ImageView
 */
public class AssetLoader {

    /**
     * Uses Glide to load a local gif asset into an ImageView
     *
     * @param context   the context
     * @param imageView where the gif will load into
     * @param assetPath local path of the gif asset
     */
    public static void loadGif(@NonNull final Context context,
                               @NonNull final ImageView imageView,
                               @NonNull final String assetPath) {
        Glide.with(context)
                .load(toByteArray(context, assetPath))
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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
