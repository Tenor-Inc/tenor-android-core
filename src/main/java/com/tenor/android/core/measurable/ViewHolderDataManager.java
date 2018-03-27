package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tenor.android.core.concurrent.ConcurrentFifoQueue;
import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.util.CoreLocaleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Queue and send {@link MeasurableViewHolderData}
 */
public class ViewHolderDataManager extends ConcurrentFifoQueue<MeasurableViewHolderEvent>
        implements IViewHolderDataManager {

    private static final int BATCH_SIZE = 5;
    private static final int MAX_POOL_SIZE = 6 * BATCH_SIZE;

    public ViewHolderDataManager() {
        super();
    }

    /**
     * Push a {@link MeasurableViewHolderEvent} to queue
     *
     * @param context the context
     * @param event   the serialized {@link MeasurableViewHolderEvent}
     */
    public synchronized void push(@NonNull final Context context,
                                  @NonNull final MeasurableViewHolderEvent event) {
        try {
            add(event);
        } catch (Throwable ignored) {
        }

        if (size() >= MAX_POOL_SIZE) {
            // flush
            send(context, Integer.MAX_VALUE);
        }

        if (size() >= BATCH_SIZE) {
            send(context, BATCH_SIZE);
        }
    }

    /**
     * Send analytic events to server
     *
     * @param context   the context
     * @param batchSize the size of the analytic data batch
     */
    public synchronized void send(@NonNull final Context context, int batchSize) {

        List<MeasurableViewHolderEvent> list = new ArrayList<>();
        try {
            while (size() > 0 && list.size() < batchSize) {
                list.add(poll());
            }
            // TODO: make this a schedule task in the future
            ApiClient.registerActions(context, list);
        } catch (Throwable throwable) {
        }
    }

    /*
     * ==============
     * Static Methods
     * ==============
     */
    private static ViewHolderDataManager sManager;

    public static ViewHolderDataManager get() {
        if (sManager == null) {
            sManager = new ViewHolderDataManager();
        }
        return sManager;
    }

    /**
     * Queue up a {@link MeasurableViewHolderData}
     *
     * @param context the context
     * @param data    the serialized {@link MeasurableViewHolderData}
     */
    public synchronized static void push(@NonNull final Context context,
                                         @NonNull final MeasurableViewHolderData data) {
        final String utcOffset = CoreLocaleUtils.getUtcOffset(context);
        get().push(context, new MeasurableViewHolderEvent(data, utcOffset));
    }
}
