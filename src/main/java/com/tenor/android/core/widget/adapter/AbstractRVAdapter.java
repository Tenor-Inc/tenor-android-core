package com.tenor.android.core.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.tenor.android.core.util.CoreWeakReferenceUtils;
import com.tenor.android.core.view.IBaseView;
import com.tenor.android.core.weakref.IWeakRefObject;

import java.lang.ref.WeakReference;

/**
 * The abstract recycler view adapter class that all recycler view adapter classes should extend from
 *
 * @param <CTX> the referenced context
 * @param <VH>  the view holder type extending from {@link RecyclerView.ViewHolder}
 */
public abstract class AbstractRVAdapter<CTX extends IBaseView, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public AbstractRVAdapter(@Nullable final CTX ctx) {
        mWeakRef = new WeakReference<>(ctx);
    }

    public AbstractRVAdapter(@NonNull final WeakReference<CTX> weakRef) {
        mWeakRef = weakRef;
    }

    /**
     * Get context, use hasContext() to check its existence first
     */
    @Nullable
    protected Context getContext() {
        return hasRef() ? getWeakRef().get().getContext() : null;
    }

    protected boolean hasContext() {
        return hasRef() && getContext() != null;
    }

    @NonNull
    @Override
    public WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return CoreWeakReferenceUtils.isAlive(mWeakRef);
    }
}
