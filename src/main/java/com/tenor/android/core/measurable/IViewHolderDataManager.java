package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.NonNull;

public interface IViewHolderDataManager {

    /**
     * Push a {@link MeasurableViewHolderEvent} to queue
     *
     * @param context the context
     * @param event   the serialized {@link MeasurableViewHolderEvent}
     */
    void push(@NonNull final Context context,
              @NonNull final MeasurableViewHolderEvent event);

    /**
     * Send feedback to improve future content delivery experience
     *
     * @param context   the context
     * @param batchSize the size of the analytic data batch
     */
    void send(@NonNull final Context context, int batchSize);
}
