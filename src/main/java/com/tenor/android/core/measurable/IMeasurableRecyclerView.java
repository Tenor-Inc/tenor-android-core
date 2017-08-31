package com.tenor.android.core.measurable;

import android.support.v7.widget.RecyclerView;

/**
 *
 */
public interface IMeasurableRecyclerView extends IRecyclerView {

    /**
     * Method to be called on the referenced view, activity, or fragment is paused
     */
    void notifyMeasurableViewHoldersOnPause();

    /**
     * Method to be called on the referenced view, activity, or fragment is resumed
     */
    void notifyMeasurableViewHoldersOnResume();

    /**
     * Method to be called on the referenced view, activity, or fragment is refreshed
     */
    void notifyMeasurableViewHoldersOnRefresh();

    /**
     * Method to be called on the referenced view, activity, or fragment is destroyed
     */
    void flushMeasurableViewHolderDataSet();

    /**
     * Notify {@link RecyclerView.ViewHolder}, which has implemented {@link IMeasurableViewHolder}
     * in the given {@link RecyclerView} that a {@link IMeasurableViewHolder#measure(RecyclerView)}
     * needs to be performed
     * <p>
     * Method got called once a scroll is reached the idle state
     * <p>
     * This updates all the stored {@link MeasurableViewHolderData} data in case any of them is not
     * properly updated due to reason, such as fast scrolling
     */
    void notifyViewHolderDataSetChanged();

    /**
     * Notify {@link RecyclerView.ViewHolder}, which has implemented {@link IMeasurableViewHolder}
     * in the given {@link RecyclerView} located between {@code start} and {@code end} that a
     * {@link IMeasurableViewHolder#measure(RecyclerView)} needs to be performed
     * <p>
     * Method got called once a scroll is reached the idle state
     * <p>
     * This updates all the stored {@link MeasurableViewHolderData} data in case any of them is not
     * properly updated due to reason, such as fast scrolling
     *
     * @param start the start position
     * @param end   the end position
     */
    void notifyViewHolderDataRangeChanged(int start, int end);
}

