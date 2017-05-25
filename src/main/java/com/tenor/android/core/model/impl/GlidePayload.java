package com.tenor.android.core.model.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.request.target.Target;
import com.tenor.android.core.listener.ILoadImageListener;

import java.io.Serializable;

/**
 * Configuration of loading a gif
 */
public class GlidePayload implements Serializable {

    private static final long serialVersionUID = -3764658332353857684L;

    /**
     * Required
     */
    @NonNull
    private final ImageView mImageView;

    /**
     * Required
     */
    @NonNull
    private final String mPath;

    @Nullable
    private Media mMedia;

    /**
     * Placeholder {@link ColorDrawable}, default is transparent
     */
    private Drawable mPlaceholder = new ColorDrawable(Color.parseColor("#00000000"));

    private float mThumbnailMultiplier = 1f;
    private int mCurrentRetry;
    private int mMaxRetry = 3;
    private int mWidth = Target.SIZE_ORIGINAL;
    private int mHeight = Target.SIZE_ORIGINAL;

    @NonNull
    private ILoadImageListener mListener = new ILoadImageListener() {
        @Override
        public void onLoadImageSucceeded(@Nullable Drawable drawable) {
        }

        @Override
        public void onLoadImageFailed(@Nullable Drawable errorDrawable) {
        }
    };

    public GlidePayload(@NonNull final ImageView imageView,
                        @NonNull final String path) {
        mImageView = imageView;
        mPath = path;
    }

    @NonNull
    public ImageView getImageView() {
        return mImageView;
    }

    @NonNull
    public String getPath() {
        return mPath;
    }

    public Drawable getPlaceholder() {
        return mPlaceholder;
    }

    /**
     * default is android.R.color.transparent
     */
    public GlidePayload setPlaceholder(@NonNull final Context context, @ColorRes final int placeholder) {
        mPlaceholder = ContextCompat.getDrawable(context, placeholder);
        return this;
    }

    /**
     * default is android.R.color.transparent
     */
    public GlidePayload setPlaceholder(@NonNull final Drawable drawable) {
        mPlaceholder = drawable;
        return this;
    }

    /**
     * default is android.R.color.transparent
     */
    public GlidePayload setPlaceholder(@Nullable final String color) {
        mPlaceholder = new ColorDrawable(Color.parseColor(color));
        return this;
    }

    public boolean isThumbnail() {
        return mThumbnailMultiplier >= 0f && mThumbnailMultiplier < 1f;
    }

    /**
     * default is false
     */
    public GlidePayload setThumbnailMultiplier(float multiplier) {
        if (multiplier >= 0f && multiplier <= 1f) {
            mThumbnailMultiplier = multiplier;
        }
        return this;
    }

    public float getThumbnailMultiplier() {
        return mThumbnailMultiplier;
    }

    public int getCurrentRetry() {
        return mCurrentRetry;
    }

    /**
     * default is 0
     */
    public GlidePayload setCurrentRetry(int retry) {
        if (retry >= 0) {
            mCurrentRetry = retry;
        }
        return this;
    }

    public GlidePayload incrementCurrentRetry() {
        mCurrentRetry++;
        return this;
    }

    public int getMaxRetry() {
        return mMaxRetry;
    }

    /**
     * default is 3
     */
    public GlidePayload setMaxRetry(int maxRetry) {
        mMaxRetry = maxRetry;
        return this;
    }

    @NonNull
    public ILoadImageListener getListener() {
        return mListener;
    }

    /**
     * default is do nothing
     */
    public GlidePayload setListener(@Nullable final ILoadImageListener listener) {
        if (listener != null) {
            mListener = listener;
        }
        return this;
    }

    @Nullable
    public Media getMedia() {
        return mMedia;
    }

    public GlidePayload setMedia(@Nullable final Media media) {
        if (media != null) {
            mMedia = media;
            setWidth(media.getWidth());
            setHeight(media.getHeight());
        }
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public GlidePayload setWidth(int width) {
        if (width > 0) {
            this.mWidth = width;
        }
        return this;
    }

    public int getHeight() {
        return mHeight;
    }

    public GlidePayload setHeight(int height) {
        if (height > 0) {
            this.mHeight = height;
        }
        return this;
    }
}
