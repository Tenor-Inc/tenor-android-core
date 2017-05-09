package com.tenor.android.core.rvwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.view.IBaseView;

import java.lang.ref.WeakReference;

/**
 * The abstract recycler view adapter class that all recycler view adapter classes should extend from
 *
 * @param <CTX> the referenced context
 * @param <VH>  the view holder type extending from {@link RecyclerView.ViewHolder}
 */
public abstract class AbstractRVAdapter<CTX extends IBaseView, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

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
        return getCTX().getContext();
    }

    protected boolean hasContext() {
        return hasCTX() && getContext() != null;
    }

    /**
     * Get {@link <CTX>}, the the generic type of  referenced context;
     * use hasWeakRef() to check its existence first
     */
    @Nullable
    protected CTX getCTX() {
        return mWeakRef.get();
    }

    protected boolean hasCTX() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    @NonNull
    protected WeakReference<CTX> getWeakRef() {
        return mWeakRef;
    }
}
