package com.tenor.android.core.measurable;

import android.support.annotation.CallSuper;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tenor.android.core.model.impl.BadgeInfo;
import com.tenor.android.core.model.impl.Result;
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
    private boolean mInitialized;

    /**
     * Order of operation:
     * <p>
     * 1. {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)} calls {@link MeasurableViewHolder} constructor
     * <p>
     * 2. {@link RecyclerView.OnChildAttachStateChangeListener#onChildViewAttachedToWindow(View)} calls {@link MeasurableViewHolder#attachMeasurer(RecyclerView)}
     * <p>
     * 3. {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)} calls {@link MeasurableViewHolder#onBindMeasurableViewHolderData(String, float, boolean)}
     * <p>
     * 4. {@link RecyclerView.OnChildAttachStateChangeListener#onChildViewDetachedFromWindow(View)} calls {@link MeasurableViewHolder#detachMeasurer()}
     * <p><p>
     * Lifecycle fo MeasurableViewHolder:
     * <p>
     * attachMeasurer() -> measure() -> detachMeasurer()
     */
    public MeasurableViewHolder(@NonNull View itemView, @NonNull CTX context) {
        super(itemView, context);
        mMeasurableViewHolderData = new MeasurableViewHolderData<>(this);
    }

    /**
     * Call on the {@link RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * to initialize {@link MeasurableViewHolderData}
     *
     * @param id              the unique identifier of the content, such as {@link Result#getSourceId()}
     * @param threshold       the percentage of content seen by users in order to be considered as viewed,
     *                        such as {@link BadgeInfo#getThreshold()}, which {@link BadgeInfo} is available
     *                        through {@link Result#getBadgeInfo()}
     * @param enhancedContent true if {@link MeasurableViewHolderData} should be used to improve
     *                        future content delivery experience
     */
    public synchronized void onBindMeasurableViewHolderData(@NonNull String id,
                                                            @FloatRange(from = 0.01f, to = 1f) float threshold,
                                                            boolean enhancedContent) {
        mMeasurableViewHolderData.setId(id);
        mMeasurableViewHolderData.setThreshold(threshold);
        mMeasurableViewHolderData.setEnhancedContent(enhancedContent);
        mMeasurableViewHolderData.updateTimestamp();
        mMeasurableViewHolderData.getAdapterPosition();
        mInitialized = true;
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

    public synchronized float measure() {
        return measure(getRecyclerView());
    }

    @Override
    public synchronized float measure(@Nullable RecyclerView recyclerView) {
        if (!isAttached() || isDetached() || !mInitialized) {
            return 0f;
        }

        if (recyclerView == null) {
            throw new IllegalStateException("ViewHolder must be attached to a non-null RecyclerView");
        }

        float visibleFraction = MeasurableViewHolderHelper.calculateVisibleFraction(recyclerView, itemView, mMeasurableViewHolderData.getThreshold());
        mMeasurableViewHolderData.setVisibleFraction(visibleFraction);

        if (mMeasurableViewHolderData.isVisualPositionUnknown()) {
            final String visualPosition = AbstractLayoutManagerUtils.getVisualPosition(getContext(), itemView);
            mMeasurableViewHolderData.setVisualPosition(visualPosition);
        }
        return visibleFraction;
    }

    @CallSuper
    @Override
    public synchronized void attachMeasurer(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mAttached = true;
        mDetached = false;
        mMeasurableViewHolderData.clear();
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
        mMeasurableViewHolderData.flush(getContext());
    }

    @CallSuper
    @Override
    public void flush() {
        mMeasurableViewHolderData.flush(getContext());
    }
}
