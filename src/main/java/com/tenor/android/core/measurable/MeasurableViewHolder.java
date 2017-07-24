package com.tenor.android.core.measurable;

import android.support.annotation.CallSuper;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.rvwidget.WeakRefViewHolder;
import com.tenor.android.core.util.AbstractLayoutManagerUtils;
import com.tenor.android.core.view.IBaseView;

public abstract class MeasurableViewHolder<CTX extends IBaseView> extends WeakRefViewHolder<CTX>
        implements IMeasurableViewHolder {

    @NonNull
    private final MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> mMeasurableViewHolderData;
    private RecyclerView mRecyclerView;
    private boolean mAttached;
    private boolean mDetached;

    /**
     * Lifecycle fo MeasurableViewHolder:
     * <p>
     * attachMeasurer() -> onViewHolderFullyReady() -> measure() -> detachMeasurer()
     */
    public MeasurableViewHolder(View itemView, CTX context) {
        super(itemView, context);
        mMeasurableViewHolderData = new MeasurableViewHolderData<>(this);
    }

    @Nullable
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> getMeasurableData() {
        return mMeasurableViewHolderData;
    }

    @Override
    public boolean isAttached() {
        return mAttached;
    }

    @Override
    public boolean isDetached() {
        return mDetached;
    }

    public synchronized void measure() {
        if (getRecyclerView() == null) {
            throw new IllegalStateException("measure() cannot be called before attachMeasurer() or after detachMeasurer() is called");
        }
        measure(getRecyclerView());
    }

    @Override
    public synchronized float measure(@NonNull RecyclerView recyclerView) {
        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(recyclerView, itemView, mMeasurableViewHolderData.getThreshold());
        mMeasurableViewHolderData.setVisibleFraction(visibleFraction);
        return visibleFraction;
    }

    @Override
    public synchronized void onContentReady(@NonNull String id, @FloatRange(from = 0.01f, to = 1f) float threshold) {
        if (!isAttached() || isDetached()) {
            return;
        }

        if (getRecyclerView() == null) {
            throw new IllegalStateException("ViewHolder must be attached to a non-null RecyclerView");
        }

        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(getRecyclerView(), itemView, threshold);
        final String visualPosition = AbstractLayoutManagerUtils.getVisualPosition(getContext(), itemView);
        mMeasurableViewHolderData.onViewHolderFullyReady(id, threshold, visibleFraction, visualPosition);
    }

    @CallSuper
    @Override
    public synchronized void attachMeasurer(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mAttached = true;
        mDetached = false;
        mMeasurableViewHolderData.clear();
        measure(recyclerView);
    }

    @CallSuper
    @Override
    public synchronized void resumeMeasurer(@NonNull RecyclerView recyclerView) {
        mMeasurableViewHolderData.resume();
    }

    @CallSuper
    @Override
    public synchronized void pauseMeasurer(@NonNull RecyclerView recyclerView) {
        mMeasurableViewHolderData.pause();
    }

    @CallSuper
    @Override
    public synchronized void detachMeasurer() {
        mAttached = false;
        mDetached = true;
        mMeasurableViewHolderData.destroy(getContext());
    }

    @CallSuper
    @Override
    public void flush() {
        mMeasurableViewHolderData.flush(getContext());
    }
}
