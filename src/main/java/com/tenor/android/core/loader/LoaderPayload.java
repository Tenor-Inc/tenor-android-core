package com.tenor.android.core.loader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tenor.android.core.listener.ILoadImageListener;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Configuration of loading a gif
 */
public class LoaderPayload implements Serializable {

    private static final long serialVersionUID = -3764658332353857684L;

    private static final String HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

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

    /**
     * Placeholder {@link ColorDrawable}, default is transparent
     */
    private Drawable mPlaceholder = new ColorDrawable(Color.parseColor("#00000000"));

    private float mThumbnailMultiplier = 1f;
    private int mCurrentRetry;
    private int mMaxRetry = 3;

    @NonNull
    private final Pattern mPattern;

    @NonNull
    private ILoadImageListener mListener = new ILoadImageListener() {
        @Override
        public void onLoadImageSucceeded(@Nullable Drawable drawable) {
        }

        @Override
        public void onLoadImageFailed(@Nullable Drawable errorDrawable) {
        }
    };

    public LoaderPayload(@NonNull ImageView imageView,
                         @NonNull String path) {
        mImageView = imageView;
        mPath = path;
        mPattern = Pattern.compile(HEX_COLOR_PATTERN);
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
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param context     the context
     * @param placeholder the color resource
     */
    public LoaderPayload setPlaceholder(@NonNull final Context context, @ColorRes final int placeholder) {
        mPlaceholder = ContextCompat.getDrawable(context, placeholder);
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param drawable the drawable
     */
    public LoaderPayload setPlaceholder(@NonNull final Drawable drawable) {
        mPlaceholder = drawable;
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param color hex color code in {@link String}, such as {@code #000000}
     */
    public LoaderPayload setPlaceholder(@Nullable final String color) {
        if (!isHexColor(color)) {
            throw new IllegalArgumentException("color must be in a valid hex color code");
        }
        mPlaceholder = new ColorDrawable(Color.parseColor(color));
        return this;
    }

    public boolean isThumbnail() {
        return mThumbnailMultiplier >= 0f && mThumbnailMultiplier < 1f;
    }

    /**
     * default is 1f
     */
    public LoaderPayload setThumbnailMultiplier(float multiplier) {
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
    public LoaderPayload setCurrentRetry(int retry) {
        if (retry >= 0) {
            mCurrentRetry = retry;
        }
        return this;
    }

    public LoaderPayload incrementCurrentRetry() {
        mCurrentRetry++;
        return this;
    }

    public int getMaxRetry() {
        return mMaxRetry;
    }

    /**
     * default is 3
     */
    public LoaderPayload setMaxRetry(int maxRetry) {
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
    public LoaderPayload setListener(@Nullable final ILoadImageListener listener) {
        if (listener != null) {
            mListener = listener;
        }
        return this;
    }

    private boolean isHexColor(@Nullable CharSequence text) {
        return !TextUtils.isEmpty(text) && mPattern.matcher(text).matches();
    }
}
