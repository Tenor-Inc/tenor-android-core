package com.tenor.android.core.util;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Contains helper methods to manipulate {@link List}
 */
public abstract class AbstractListUtils {

    /**
     * Check if the given array is null or with no item in it
     *
     * @param list the given {@link List}
     * @param <T>  the type of the list items
     */
    public static <T> boolean isEmpty(@Nullable final T[] list) {
        return list == null || list.length <= 0;
    }

    /**
     * Check if the given list is null or with no item in it
     *
     * @param list the given {@link List}
     * @param <T>  the type of the list items
     */
    public static <T> boolean isEmpty(@Nullable final Collection<T> list) {
        return list == null || list.isEmpty();
    }
}
