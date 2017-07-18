package com.tenor.android.core.measurable;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Interface of a {@link RecyclerView.ViewHolder}, measure {@link MeasurableViewHolderData} of a {@link RecyclerView.ViewHolder}
 * <p>
 * (1) Implementations of methods in this {@link IMeasurableViewHolder} are recommended to be synchronized
 * <p>
 * (2) {@link MeasurableViewHolderData} must be initialized on constructor
 */
public interface IMeasurableViewHolder extends IViewHolder {

    boolean isMeasurable();

    void setMeasurable(boolean measurable);

    @FloatRange(from = 0f, to = 1f)
    float measure(@NonNull RecyclerView recyclerView);

    /**
     * Enable timer
     */
    void attachTimer(@NonNull RecyclerView recyclerView);

    void resumeTimer(@NonNull RecyclerView recyclerView);

    void pauseTimer();

    /**
     * Destroy {@link MeasurableViewHolderData} on the itemView of a
     * {@link RecyclerView.ViewHolder} detached from window
     */
    void detachTimer();

    void flush();
}

