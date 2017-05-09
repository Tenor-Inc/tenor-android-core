package com.tenor.android.core.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 * The base presenter class that all presenters should extend from
 */
public class BasePresenter<T extends IBaseView> implements IBasePresenter<T> {

    private final WeakReference<T> mWeakRef;

    public BasePresenter(T view) {
        mWeakRef = new WeakReference<>(view);
    }

    @Override
    @Nullable
    public T getView() {
        return hasView() ? mWeakRef.get() : null;
    }

    @NonNull
    protected WeakReference<T> getWeakRef() {
        return mWeakRef;
    }

    protected boolean hasView() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }
}
