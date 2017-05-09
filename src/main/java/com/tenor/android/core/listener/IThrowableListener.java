package com.tenor.android.core.listener;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * The interface to handle unexpected {@link Throwable}
 *
 * @param <T> the type of item that may cause the {@link Throwable}
 */
public interface IThrowableListener<T> {

    /**
     * Handle {@link Throwable}
     *
     * @param throwable the throwable
     */
    void onReceiveThrowable(@Nullable Throwable throwable);

    /**
     * Handle {@link Throwable}
     *
     * @param throwable the throwable
     * @param event     the {@link T} that cause the {@link Throwable}
     */
    void onReceiveThrowable(@Nullable Throwable throwable, @Nullable T event);

    /**
     * Handle {@link Throwable}
     *
     * @param throwable the throwable
     * @param event     the {@link T} that cause the {@link Throwable}
     * @param events    other instances of {@link T} that may also be affected, a.k.a the collateral damage
     */
    void onReceiveThrowable(@Nullable Throwable throwable, @Nullable T event, @Nullable List<T> events);
}