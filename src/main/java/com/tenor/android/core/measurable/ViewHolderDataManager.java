package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tenor.android.core.util.AbstractGsonUtils;

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
    private static Queue<String> sQueue;

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

    private static Queue<String> getInstance() {
        if (sQueue == null) {
            sQueue = new ConcurrentLinkedQueue<>();
            sQueueSize = 0;
        }
        return sQueue;
    }

    /**
     * Push a analytic event to queue
     *
     * @param context the context
     * @param data    the serialized {@link MeasurableViewHolderData}
     */
    public synchronized static void push(@NonNull final Context context, String data) {
        try {
            getInstance().add(data);
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
     * Push a analytic event to queue
     *
     * @param context the context
     * @param data    the {@link MeasurableViewHolderData}
     */
    public synchronized static void push(@NonNull final Context context, MeasurableViewHolderData data) {
        push(context, AbstractGsonUtils.getInstance().toJson(data));

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
        send(context, Integer.MAX_VALUE, sendWithoutKeyboardId);
    }

    /**
     * Send analytic events to server
     *
     * @param context the context
     */
    public synchronized static void send(@NonNull final Context context) {
        send(context, BATCH_SIZE, false);
    }

    /**
     * Send analytic events to server
     *
     * @param context               the context
     * @param batchSize             the size of the analytic data batch
     * @param sendWithoutKeyboardId force sending the analytic data when there is no keyboard id
     */
    private synchronized static void send(@NonNull final Context context, int batchSize,
                                          boolean sendWithoutKeyboardId) {
        // TODO: to be implemented
    }
}
