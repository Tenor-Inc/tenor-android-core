package com.tenor.android.core.concurrent;

import android.support.annotation.NonNull;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Queue and send {@link T}
 */
public class ConcurrentFifoQueue<T> implements IConcurrentFifoQueue<T> {

    @NonNull
    private final ConcurrentLinkedQueue<T> mQueue;

    /**
     * Use explicit size counter to compensate the non-constant-time size() operation
     * in {@link ConcurrentLinkedQueue}
     */
    private final AtomicInteger mQueueSize;

    public ConcurrentFifoQueue() {
        mQueue = new ConcurrentLinkedQueue<>();
        mQueueSize = new AtomicInteger();
    }

    /**
     * @return the size of the {@link ConcurrentLinkedQueue} of {@link T}
     */
    public int add(@NonNull T event) {
        mQueue.add(event);
        return mQueueSize.incrementAndGet();
    }

    /**
     * Retrieves and removes the head of {@link ConcurrentLinkedQueue},
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public T poll() {
        // decrement the index only after retrieving the content
        final T t = mQueue.poll();
        mQueueSize.decrementAndGet();
        return t;
    }

    public int size() {
        return mQueueSize.get();
    }
}
