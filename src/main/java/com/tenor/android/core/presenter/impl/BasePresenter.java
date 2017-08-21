package com.tenor.android.core.presenter.impl;

import android.support.annotation.Nullable;

import com.tenor.android.core.weakref.WeakRefObject;
import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.view.IBaseView;

/**
 * The base presenter class that all presenters should extend from
 */
public class BasePresenter<T extends IBaseView> extends WeakRefObject<T> implements IBasePresenter<T> {

    public BasePresenter(T view) {
        super(view);
    }

    @Nullable
    @Override
    public T getView() {
        return getRef();
    }

    public boolean hasView() {
        return hasRef();
    }
}
