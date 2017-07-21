package com.tenor.android.core.measurable;

import android.support.annotation.CallSuper;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.constant.ItemVisualPositions;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.rvwidget.WeakRefViewHolder;
import com.tenor.android.core.util.AbstractLayoutManagerUtils;
import com.tenor.android.core.view.IBaseView;

public abstract class MeasurableViewHolder<CTX extends IBaseView> extends WeakRefViewHolder<CTX>
        implements IMeasurableViewHolder {

    @NonNull
    private final MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> mMeasurableViewHolderData;
    private RecyclerView mRecyclerView;

    @NonNull
    private String mId = StringConstant.EMPTY;

    @FloatRange(from = 0.01f, to = 1f)
    private float mViewAcceptanceThreshold = 1f;

    public MeasurableViewHolder(View itemView, CTX context) {
        super(itemView, context);
        mMeasurableViewHolderData = new MeasurableViewHolderData<>(this, mId, mViewAcceptanceThreshold);
    }

    @Nullable
    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    protected MeasurableViewHolderData<? extends MeasurableViewHolder<CTX>> getMeasurableData() {
        return mMeasurableViewHolderData;
    }

    public void setViewAcceptanceThreshold(@FloatRange(from = 0.01f, to = 1f) float threshold) {
        mViewAcceptanceThreshold = threshold;
    }

    /**
     * Set the unique identifier of this view holder, MUST OVERWRITE
     */
    public void setId(@NonNull String id) {
        mId = id;
    }

    public synchronized void measure() {
        if (getRecyclerView() != null) {
            measure(getRecyclerView());
        }
    }

    @Override
    public synchronized float measure(@NonNull RecyclerView recyclerView) {
        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(recyclerView, itemView, mViewAcceptanceThreshold);
        mMeasurableViewHolderData.setVisibleFraction(visibleFraction);
        return visibleFraction;
    }

    @Override
    public synchronized void onContentReady() {
        if (getRecyclerView() != null) {
            float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(getRecyclerView(), itemView, mViewAcceptanceThreshold);
            final int spanIndex = AbstractLayoutManagerUtils.getSpanIndex(itemView.getLayoutParams());
            mMeasurableViewHolderData.onContentReady(visibleFraction, ItemVisualPositions.parse(getContext(), spanIndex));
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
        mMeasurableViewHolderData.destroy(getContext());
    }

    @CallSuper
    @Override
    public void flush() {
        mMeasurableViewHolderData.flush(getContext());
    }
}
