package com.tenor.android.core.listener;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.weakref.WeakRefObject;

import java.lang.ref.WeakReference;

public abstract class WeakRefLoadImageListener<CTX> extends WeakRefObject<CTX> implements ILoadImageListener {

    public WeakRefLoadImageListener(@NonNull CTX ctx) {
        super(ctx);
    }

    public WeakRefLoadImageListener(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef);
    }

    @Override
    public void onLoadImageSucceeded(@Nullable Drawable drawable) {
        if (hasRef()) {
            //noinspection ConstantConditions
            onLoadImageSucceeded(getRef(), drawable);
        }
    }

    @Override
    public void onLoadImageFailed(@Nullable Drawable errorDrawable) {
        if (hasRef()) {
            //noinspection ConstantConditions
            onLoadImageFailed(getRef(), errorDrawable);
        }
    }

    public abstract void onLoadImageSucceeded(@NonNull CTX ctx, @Nullable Drawable drawable);

    public abstract void onLoadImageFailed(@NonNull CTX ctx, @Nullable Drawable errorDrawable);
}
