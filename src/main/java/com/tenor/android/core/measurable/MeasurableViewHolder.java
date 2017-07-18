package com.tenor.android.core.measurable;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.rvwidget.WeakRefViewHolder;
import com.tenor.android.core.view.IBaseView;

public abstract class MeasurableViewHolder<CTX extends IBaseView> extends WeakRefViewHolder<CTX>
        implements IMeasurableViewHolder {

    @NonNull
    private final MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> mMeasurableViewHolderData;
    private RecyclerView mRecyclerView;
    private boolean mMeasurable;

    public MeasurableViewHolder(View itemView, CTX context) {
        super(itemView, context);
        mMeasurableViewHolderData = new MeasurableViewHolderData<>(this, getThreshold());
    }

    @Nullable
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> getMeasurableViewHolderData() {
        return mMeasurableViewHolderData;
    }

    protected float getThreshold() {
        return 1.0f;
    }

    public synchronized void measure() {
        if (getRecyclerView() != null) {
            measure(getRecyclerView());
        }
    }

    @Override
    public final boolean isMeasurable() {
        return mMeasurable;
    }

    @Override
    public void setMeasurable(boolean measurable) {
        mMeasurable = measurable;
    }

    @Override
    public synchronized float measure(@NonNull RecyclerView recyclerView) {
        if (!isMeasurable()) {
            return 0f;
        }
        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(recyclerView, itemView, getThreshold());
        mMeasurableViewHolderData.setVisibleFraction(visibleFraction);
        return visibleFraction;
    }

    @CallSuper
    @Override
    public synchronized void attachMeasurer(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mMeasurable = false;
        mMeasurableViewHolderData.clear();
        measure(recyclerView);
    }

    @CallSuper
    @Override
    public synchronized void resumeMeasurer(@NonNull RecyclerView recyclerView) {
        if (isMeasurable()) {
            mMeasurableViewHolderData.resume();
        }
    }

    @CallSuper
    @Override
    public synchronized void pauseMeasurer(@NonNull RecyclerView recyclerView) {
        if (isMeasurable()) {
            mMeasurableViewHolderData.pause();
        }
    }

    @CallSuper
    @Override
    public synchronized void detachMeasurer() {
        mRecyclerView = null;
        mMeasurable = false;
        mMeasurableViewHolderData.destroy(getContext());
    }

    @CallSuper
    @Override
    public void flush() {
        if (isMeasurable()) {
            mMeasurableViewHolderData.flush(getContext());
        }
    }
}
