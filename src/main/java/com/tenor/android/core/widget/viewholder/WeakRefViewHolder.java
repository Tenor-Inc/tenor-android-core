package com.tenor.android.core.widget.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.util.CoreWeakReferenceUtils;
import com.tenor.android.core.weakref.IWeakRefObject;

import java.lang.ref.WeakReference;

/**
 * The abstract recycler view view holder class that all recycler view view holder classes should extend from
 *
 * @param <CTX> the referenced context
 */
public abstract class WeakRefViewHolder<CTX> extends RecyclerView.ViewHolder
        implements IWeakRefObject<CTX> {

    private final WeakReference<CTX> mWeakRef;

    public WeakRefViewHolder(@NonNull View itemView, @NonNull CTX context) {
        super(itemView);
        mWeakRef = new WeakReference<>(context);
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

    public abstract Context getContext();

    public abstract boolean hasContext();
}
