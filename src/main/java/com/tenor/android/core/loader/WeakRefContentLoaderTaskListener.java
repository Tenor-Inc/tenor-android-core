package com.tenor.android.core.loader;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.tenor.android.core.weakref.WeakRefObject;

import java.lang.ref.WeakReference;

public abstract class WeakRefContentLoaderTaskListener<CTX, T extends ImageView>
        extends WeakRefObject<CTX>
        implements IContentLoaderTaskListener<T, Drawable> {

    public WeakRefContentLoaderTaskListener(@NonNull CTX ctx) {
        super(ctx);
    }

    public WeakRefContentLoaderTaskListener(@NonNull WeakReference<CTX> weakRef) {
        super(weakRef);
    }

    @Override
    public void success(@NonNull T target, @NonNull Drawable taskResult) {
        if (hasRef()) {
            //noinspection ConstantConditions
            success(getRef(), target, taskResult);
        }
    }

    @Override
    public void failure(@NonNull T target, @NonNull Drawable errorResult) {
        if (hasRef()) {
            //noinspection ConstantConditions
            failure(getRef(), target, errorResult);
        }
    }

    public abstract void success(@NonNull CTX ctx, @NonNull T target, @Nullable Drawable taskResult);

    public abstract void failure(@NonNull CTX ctx, @NonNull T target, @Nullable Drawable errorResult);
}
