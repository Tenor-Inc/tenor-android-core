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
import android.widget.ImageView;

import com.tenor.android.core.util.AbstractColorUtils;

public class DrawableLoaderTaskParams<T extends ImageView> implements IDrawableLoaderTaskParams<T, Drawable> {

    private static final long serialVersionUID = -3764658332353857684L;

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

    private IDrawableLoaderTaskListener<T, Drawable> mListener;

    public DrawableLoaderTaskParams(@NonNull T imageView,
                                    @NonNull String path) {
        mImageView = imageView;
        mPath = path;
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
    public DrawableLoaderTaskParams<T> setPlaceholder(@NonNull Context context, @ColorRes int colorResId) {
        setPlaceholder(ContextCompat.getDrawable(context, colorResId));
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param drawable the drawable
     */
    public DrawableLoaderTaskParams<T> setPlaceholder(@NonNull Drawable drawable) {
        mPlaceholder = drawable;
        return this;
    }

    /**
     * Set placeholder color, the default is android.R.color.transparent
     *
     * @param colorHex hex color code in {@link String}, such as {@code #000000}
     */
    public DrawableLoaderTaskParams<T> setPlaceholder(@Nullable String colorHex) {
        if (!AbstractColorUtils.isColorHex(colorHex)) {
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
    public DrawableLoaderTaskParams<T> setPlaceholder(@ColorInt int colorInt) {
        setPlaceholder(new ColorDrawable(colorInt));
        return this;
    }

    @NonNull
    public IDrawableLoaderTaskListener<T, Drawable> getListener() {
        return mListener != null ? mListener :
                new IDrawableLoaderTaskListener<T, Drawable>() {
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
    public DrawableLoaderTaskParams<T> setListener(@Nullable final IDrawableLoaderTaskListener<T, Drawable> listener) {
        mListener = listener;
        return this;
    }
}
