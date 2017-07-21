package com.tenor.android.core.measurable;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.rvwidget.WeakRefViewHolder;
import com.tenor.android.core.view.IBaseView;

public abstract class MeasurableViewHolder<CTX extends IBaseView> extends WeakRefViewHolder<CTX>
        implements IMeasurableViewHolder {

    @NonNull
    private final MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> mMeasurableViewHolderData;
    private RecyclerView mRecyclerView;

    public MeasurableViewHolder(View itemView, CTX context) {
        super(itemView, context);
        mMeasurableViewHolderData = new MeasurableViewHolderData<>(this, getViewAcceptanceThreshold());
    }

    @Nullable
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> getMeasurableData() {
        return mMeasurableViewHolderData;
    }

    protected float getViewAcceptanceThreshold() {
        return 1.0f;
    }

    /**
     * Get the unique identifier of this view holder
     * */
    @NonNull
    @Override
    public String getId() {
        return StringConstant.EMPTY;
    }

    public synchronized void measure() {
        if (getRecyclerView() != null) {
            measure(getRecyclerView());
        }
    }

    @Override
    public synchronized float measure(@NonNull RecyclerView recyclerView) {
        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(recyclerView, itemView, getViewAcceptanceThreshold());
        mMeasurableViewHolderData.setVisibleFraction(visibleFraction);
        return visibleFraction;
    }

    @Override
    public synchronized void onContentReady() {
        if (getRecyclerView() != null) {
            float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(getRecyclerView(), itemView, getViewAcceptanceThreshold());
            mMeasurableViewHolderData.onContentReady(visibleFraction);
        }
    }

    @CallSuper
    @Override
    public synchronized void attachMeasurer(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
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
        mRecyclerView = null;
        mMeasurableViewHolderData.destroy(getContext(), getId());
    }

    @CallSuper
    @Override
    public void flush() {
        mMeasurableViewHolderData.flush(getContext(), getId());
    }
}
