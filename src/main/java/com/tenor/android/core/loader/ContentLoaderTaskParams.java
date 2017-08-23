package com.tenor.android.core.loader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.regex.Pattern;

public class ContentLoaderTaskParams<T extends ImageView> implements IContentLoaderTaskParams<T, Drawable> {

    private static final long serialVersionUID = -3764658332353857684L;

    private static final String HEX_COLOR_PATTERN = "^#([A-Fa-f0-9]{8}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    /**
     * Required
     */
    @NonNull
    private final T mImageView;

    /**
     * Required
     */
    @NonNull
    private final String mPath;

    /**
     * Placeholder {@link ColorDrawable}, default is {@link Color#TRANSPARENT}
     */
    private Drawable mPlaceholder;

    @NonNull
    private final Pattern mPattern;

    private IContentLoaderTaskListener<T, Drawable> mListener;

    public ContentLoaderTaskParams(@NonNull T imageView,
                                   @NonNull String path) {
        mImageView = imageView;
        mPath = path;
        mPattern = Pattern.compile(HEX_COLOR_PATTERN);
    }

    @NonNull
    @Override
    public String getId() {
        return getPath();
    }

    @NonNull
    public T getTarget() {
        return mImageView;
    }

    @NonNull
    public String getPath() {
        return mPath;
    }

    @NonNull
    public Drawable getPlaceholder() {
        return mPlaceholder != null ? mPlaceholder : new ColorDrawable(Color.TRANSPARENT);
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param context    the context
     * @param colorResId the color resource
     */
    public ContentLoaderTaskParams<T> setPlaceholder(@NonNull Context context, @ColorRes int colorResId) {
        setPlaceholder(ContextCompat.getDrawable(context, colorResId));
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param drawable the drawable
     */
    public ContentLoaderTaskParams<T> setPlaceholder(@NonNull Drawable drawable) {
        mPlaceholder = drawable;
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param colorHex hex color code in {@link String}, such as {@code #000000}
     */
    public ContentLoaderTaskParams<T> setPlaceholder(@Nullable String colorHex) {
        if (!isHexColor(colorHex)) {
            throw new IllegalArgumentException("color must be in a valid hex color code");
        }
        setPlaceholder(Color.parseColor(colorHex));
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param colorInt the color integer
     */
    public ContentLoaderTaskParams<T> setPlaceholder(@ColorInt int colorInt) {
        setPlaceholder(new ColorDrawable(colorInt));
        return this;
    }

    @NonNull
    public IContentLoaderTaskListener<T, Drawable> getListener() {
        return mListener != null ? mListener :
                new IContentLoaderTaskListener<T, Drawable>() {
                    @Override
                    public void success(@NonNull T target, @NonNull Drawable taskResult) {

                    }

                    @Override
                    public void failure(@NonNull T target, @NonNull Drawable errorResult) {

                    }
                };
    }

    /**
     * default is do nothing
     */
    public ContentLoaderTaskParams<T> setListener(@Nullable final IContentLoaderTaskListener<T, Drawable> listener) {
        mListener = listener;
        return this;
    }

    private boolean isHexColor(@Nullable CharSequence text) {
        return !TextUtils.isEmpty(text) && mPattern.matcher(text).matches();
    }
}
