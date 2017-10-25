package com.tenor.android.core.view;

import android.content.Context;

/**
 * The base view that all activities should implement
 */
public interface IBaseView {
    Context getContext();

    boolean isAlive();
}
