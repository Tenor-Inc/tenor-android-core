package com.tenor.android.core.rvwidget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tenor.android.core.listener.IWeakRefObject;
import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 * The abstract recycler view view holder class that all recycler view view holder classes should extend from
 *
 * @param <CTX> the referenced context
 */
public abstract class WeakRefViewHolder<CTX> extends RecyclerView.ViewHolder
        implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefViewHolder(View itemView, CTX context) {
        super(itemView);
        mWeakRef = new WeakReference<>(context);
    }

    @Nullable
    @Override
    public CTX getRef() {
        return mWeakRef.get();
    }

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    public abstract Context getContext();

    public abstract boolean hasContext();
}
