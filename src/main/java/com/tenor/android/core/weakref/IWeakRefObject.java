package com.tenor.android.core.weakref;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Interface of {@link WeakRefObject}
 */
public interface IWeakRefObject<CTX> {

    @Nullable
    CTX getRef();

    @NonNull
    WeakReference<CTX> getWeakRef();

    boolean hasRef();
}
