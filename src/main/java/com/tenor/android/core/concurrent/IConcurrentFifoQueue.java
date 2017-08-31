package com.tenor.android.core.concurrent;

import android.support.annotation.NonNull;

import java.util.Queue;

public interface IConcurrentFifoQueue<T> {

    /**
     * @return the size of the {@link Queue} of {@link T}
     */
    int add(@NonNull T event);

    /**
     * Retrieves and removes the head of {@link Queue},
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    T poll();

    /**
     * Returns the number of elements in this queue.  If this queue
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this queue
     */
    int size();
}
