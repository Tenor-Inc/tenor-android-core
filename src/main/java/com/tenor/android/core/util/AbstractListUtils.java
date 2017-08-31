package com.tenor.android.core.util;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.Set;

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
    public static <T> boolean isEmpty(@Nullable final List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * Check if the given set is null or with no item in it
     *
     * @param set the given {@link Set}
     * @param <T> the type of the list items
     */
    public static <T> boolean isEmpty(@Nullable final Set<T> set) {
        return set == null || set.isEmpty();
    }
}
