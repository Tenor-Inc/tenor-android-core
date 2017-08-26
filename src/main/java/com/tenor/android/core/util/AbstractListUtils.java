package com.tenor.android.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Contains helper methods to manipulate {@link List}
 */
public abstract class AbstractListUtils {

    /**
     * Takes a given list with items, randomizes its items and returns it
     *
     * @param list given list
     * @return randomized list
     */
    @NonNull
    public static <T> List<T> shuffle(@Nullable final List<T> list) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }
        Collections.shuffle(list);
        return list;
    }

    /**
     * Shuffle a given {@link int}[]
     * <p>
     * A implementation of Fisher–Yates shuffle
     *
     * @param array given {@link int}[] to be shuffled
     * @return a shuffled {@link int}[]
     */
    @NonNull
    public static int[] shuffle(@NonNull int[] array) {
        if (array.length <= 0) {
            return array;
        }
        Random random = RandomCompat.get();
        int randInt;
        int temp;
        for (int i = array.length - 1; i > 0; i--) {
            randInt = random.nextInt(i + 1);
            temp = array[randInt];
            array[randInt] = array[i];
            array[i] = temp;
        }
        return array;
    }

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

    /**
     * Check if the given {@link List} has one and only one item
     *
     * @param list the given {@link List}
     * @return true if the given list has one and only one item
     */
    public static <T> boolean hasOnlyOneItem(@Nullable final List<T> list) {
        return !isEmpty(list) && list.size() == 1;
    }
}
