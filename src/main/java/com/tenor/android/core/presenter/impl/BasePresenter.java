package com.tenor.android.core.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.response.WeakRefCallback;
import com.tenor.android.core.view.IBaseView;
import com.tenor.android.core.weakref.WeakRefObject;

import java.lang.ref.WeakReference;

/**
 * The base presenter class that all presenters should extend from
 */
public class BasePresenter<CTX extends IBaseView> extends WeakRefObject<CTX> implements IBasePresenter<CTX> {

    public BasePresenter(CTX ctx) {
        super(ctx);
    }

    @Nullable
    @Override
    public CTX getView() {
        return getWeakRef().get();
    }

    public boolean hasView() {
        return hasRef();
    }

    protected abstract class BaseWeakRefCallback<T> extends WeakRefCallback<CTX, T> {

        protected BaseWeakRefCallback(@NonNull CTX ctx) {
            this(new WeakReference<>(ctx));
        }

        protected BaseWeakRefCallback(@NonNull WeakReference<CTX> weakRef) {
            super(weakRef);
        }
    }
}
