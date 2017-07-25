package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.tenor.android.core.concurrency.WeakRefOnScrollListener;
import com.tenor.android.core.util.AbstractLayoutManagerUtils;
import com.tenor.android.core.util.AbstractLogUtils;
import com.tenor.android.core.util.AbstractUIUtils;

import java.util.List;

public class MeasurableOnScrollListener<CTX extends Context> extends WeakRefOnScrollListener<CTX> {

    private int mDraggingStart = RecyclerView.NO_POSITION;
    private int mDraggingEnd = RecyclerView.NO_POSITION;
    private boolean mRtl = false;
    private boolean mDragging;

    private static final int TYPE_UNMEASURABLE = -1;
    private static final int TYPE_UNKNOWN = 0;
    private static final int TYPE_MEASURABLE = 1;
    private int mMeasurable = TYPE_UNKNOWN;

    public MeasurableOnScrollListener(@NonNull CTX ctx) {
        super(ctx);
        mRtl = AbstractUIUtils.isRightToLeft(ctx);
    }

    private boolean validateMeasurable(@Nullable RecyclerView recyclerView) {
        if (mMeasurable != TYPE_UNKNOWN) {
            return mMeasurable == TYPE_MEASURABLE;
        }

        if (recyclerView instanceof IMeasurableRecyclerView) {
            mMeasurable = TYPE_MEASURABLE;
        } else {
            mMeasurable = TYPE_UNMEASURABLE;
        }
        return mMeasurable == TYPE_MEASURABLE;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mDragging) {
            // Only for dragging case to improve the accuracy
            onDragged(recyclerView);
        } else {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                // distinct drag to improve accuracy, init mDraggingStart and mDraggingEnd here
                mDragging = true;
                final int[] range = AbstractLayoutManagerUtils.getVisibleRange(recyclerView);
                mDraggingStart = range[0];
                mDraggingEnd = range[1];
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                mDragging = false;
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                mDragging = false;

                // perform final update on the range we have ever visited
                updateVisibleRange(recyclerView);
                AbstractLogUtils.e(this, "==>  visible range: [" + mDraggingStart + ", " + mDraggingEnd + "]");
                MeasurableRecyclerViewHelper.notifyMeasurableViewHolderDataRangeChanged(recyclerView, mDraggingStart, mDraggingEnd);

                // reset mDraggingStart and mDraggingEnd here
                mDraggingStart = RecyclerView.NO_POSITION;
                mDraggingEnd = RecyclerView.NO_POSITION;

                final boolean rtl = AbstractUIUtils.isRightToLeft(getRef());
                if (mRtl ^ rtl) {
                    /*
                     * [ANDROID-1778]
                     *
                     * immediately flush the statistics if user change between a ltr and rtl language
                     * on the fly; do the check in here for better performance
                     */
                    final List<IMeasurableViewHolder> holders =
                            MeasurableRecyclerViewHelper.getViewHolders(recyclerView, IMeasurableViewHolder.class);
                    for (IMeasurableViewHolder holder : holders) {
                        holder.flush();
                    }

                    if (AbstractLayoutManagerUtils.getOrientation(recyclerView.getLayoutManager())
                            == OrientationHelper.HORIZONTAL) {
                        AbstractLayoutManagerUtils.setReverseLayout(recyclerView.getLayoutManager(), mRtl);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void onDragged(@NonNull RecyclerView recyclerView) {
        updateVisibleRange(recyclerView);

        /*
         * Get the view holders that are currently visiting or just visited.  Using the range from
         * `AbstractLayoutManagerUtils.getVisibleRange(recyclerView);` should result in better
         * performance, yet it can potentially reduce the measuring accuracy
         */
        final List<IMeasurableViewHolder> holders = MeasurableRecyclerViewHelper.getViewHolders(
                recyclerView, IMeasurableViewHolder.class, mDraggingStart, mDraggingEnd);
        for (IMeasurableViewHolder holder : holders) {
            holder.measure(recyclerView);
        }
    }

    private void updateVisibleRange(@NonNull RecyclerView recyclerView) {
        /*
         * [ANDROID-1830]
         *
         * Get the estimated completely visible item range of a multi-span layout manager,
         * this just a rough range, each start/end item on each span may not be completely
         * visible
         */
        final int[] range = AbstractLayoutManagerUtils.getVisibleRange(recyclerView);
        if (range[0] > RecyclerView.NO_POSITION && range[0] < mDraggingStart) {
            mDraggingStart = range[0];
        }
        if (range[1] > RecyclerView.NO_POSITION && range[1] > mDraggingEnd) {
            mDraggingEnd = range[1];
        }
    }
}

