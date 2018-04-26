package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.tenor.android.core.weakref.WeakRefObject;

import java.lang.ref.WeakReference;

public abstract class WeakRefContentLoaderTaskListener<CTX, T extends ImageView>
        extends WeakRefObject<CTX>
        implements IDrawableLoaderTaskListener<T, Drawable> {

    public WeakRefContentLoaderTaskListener(@NonNull CTX ctx) {
        super(ctx);
    }

    public WeakRefContentLoaderTaskListener(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef);
    }

    @Override
    public void success(@NonNull T target, @NonNull Drawable drawable) {
        if (hasRef()) {
            //noinspection ConstantConditions
            success(getWeakRef().get(), target, drawable);
        }
    }

    @Override
    public void failure(@NonNull T target, @Nullable Drawable errorDrawable) {
        if (hasRef()) {
            //noinspection ConstantConditions
            failure(getWeakRef().get(), target, errorDrawable);
        }
    }

    public abstract void success(@NonNull CTX ctx, @NonNull T target, @Nullable Drawable taskResult);

    public abstract void failure(@NonNull CTX ctx, @NonNull T target, @Nullable Drawable errorResult);
}
