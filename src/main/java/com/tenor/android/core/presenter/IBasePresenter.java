package com.tenor.android.core.presenter;

import com.tenor.android.core.view.IBaseView;

/**
 * The base presenter interface that all presenters should implement
 */
public interface IBasePresenter<T extends IBaseView> {
    T getView();
}