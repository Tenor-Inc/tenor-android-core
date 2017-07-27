package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.util.AbstractLocaleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Queue and send {@link MeasurableViewHolderData}
 */
public class ViewHolderDataManager {

    private static final int BATCH_SIZE = 5;
    private static final int MAX_POOL_SIZE = 6 * BATCH_SIZE;
    /**
     * Always call sQueue through using {@link #getInstance()}
     */
    private static Queue<MeasurableViewHolderEvent> sQueue;

    /**
     * Use explicit size counter to compensate the non-constant-time size() operation in {@link ConcurrentLinkedQueue}
     */
    private static int sQueueSize;

    /**
     * initialize {@link ViewHolderDataManager}
     */
    public static void init() {
        getInstance();
    }

    private static Queue<MeasurableViewHolderEvent> getInstance() {
        if (sQueue == null) {
            sQueue = new ConcurrentLinkedQueue<>();
            sQueueSize = 0;
        }
        return sQueue;
    }

    /**
     * Push a view event to queue
     *
     * @param context the context
     * @param data    the serialized {@link MeasurableViewHolderData}
     */
    public synchronized static void push(@NonNull final Context context,
                                         @NonNull final MeasurableViewHolderData data) {
        final String utcOffset = AbstractLocaleUtils.getUtcOffset(context);
        push(context, new MeasurableViewHolderEvent(data, utcOffset));
    }

    /**
     * Push an action event to queue
     *
     * @param context the context
     * @param id      the unique identifier of the view holder
     * @param action  the action {share|tap}
     */
    public synchronized static void push(@NonNull final Context context,
                                         @NonNull final String id,
                                         @NonNull final String action,
                                         @NonNull String visualPosition) {
        final String utcOffset = AbstractLocaleUtils.getUtcOffset(context);
        push(context, new MeasurableViewHolderEvent(id, action, utcOffset, visualPosition));
    }

    /**
     * Push a {@link MeasurableViewHolderEvent} to queue
     *
     * @param context the context
     * @param event   the serialized {@link MeasurableViewHolderEvent}
     */
    private synchronized static void push(@NonNull final Context context,
                                          @NonNull final MeasurableViewHolderEvent event) {
        try {
            getInstance().add(event);
            sQueueSize++;
        } catch (Throwable ignored) {
        }

        if (sQueueSize >= MAX_POOL_SIZE) {
            flush(context, false);
        }

        if (sQueueSize >= BATCH_SIZE) {
            send(context);
        }
    }

    /**
     * Flush remaining analytic events to server even without a keyboard id
     *
     * @param context the context
     */
    public synchronized static void flush(@NonNull final Context context) {
        flush(context, true);
    }

    /**
     * Flush remaining analytic events to server
     *
     * @param context               the context
     * @param sendWithoutKeyboardId force sending the analytic data when there is no keyboard id
     */
    public synchronized static void flush(@NonNull final Context context, boolean sendWithoutKeyboardId) {
        send(context, Integer.MAX_VALUE);
    }

    /**
     * Send analytic events to server
     *
     * @param context the context
     */
    public synchronized static void send(@NonNull final Context context) {
        send(context, BATCH_SIZE);
    }

    /**
     * Send analytic events to server
     *
     * @param context               the context
     * @param batchSize             the size of the analytic data batch
     */
    private synchronized static void send(@NonNull final Context context, int batchSize) {

        List<MeasurableViewHolderEvent> list = new ArrayList<>();
        try {
            while (sQueueSize > 0 && list.size() < batchSize) {
                list.add(getInstance().poll());
                sQueueSize--;
            }
            // TODO: make this a schedule task
            ApiClient.registerActions(context, list);
        } catch (Throwable throwable) {
        }
    }
}
