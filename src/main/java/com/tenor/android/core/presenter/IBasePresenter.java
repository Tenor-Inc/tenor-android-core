package com.tenor.android.core.presenter;

import android.support.annotation.Nullable;

import com.tenor.android.core.view.IBaseView;

/**
 * The base presenter interface that all presenters should implement
 */
public interface IBasePresenter<T extends IBaseView> {
    @Nullable
    T getView();
}