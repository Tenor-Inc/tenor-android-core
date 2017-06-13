package com.tenor.android.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.model.ICollection;
import com.tenor.android.core.model.IGif;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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

        List<T> items = new ArrayList<>();
        T item;
        Random random = new Random();
        while (!isEmpty(list)) {
            item = list.remove(random.nextInt(list.size()));
            items.add(item);
        }
        return items;
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
        Random random = getRandomCompat();
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
     * Get the better perform or backward compatible {@link Random} base on API version
     */
    public static Random getRandomCompat() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            return ThreadLocalRandom.current();
        } else {
            return new Random();
        }
    }

    /**
     * Merge lists while ignoring duplicates
     *
     * @param base  the base list
     * @param items the list that is going to merge into the base list
     * @return the joined list
     */
    @NonNull
    public static List<String> orJoinString(List<String> base, List<String> items) {

        if (base == null && items == null) {
            return new ArrayList<>();
        }

        if (base == null) {
            return items;
        }

        if (items == null) {
            return base;
        }

        final Set<String> set = new HashSet<>();
        for (String str : base) {
            set.add(str);
        }

        for (String str : items) {
            if (!set.contains(str)) {
                set.add(str);
                base.add(str);
            }
        }
        return base;
    }

    /**
     * Merge lists while ignoring duplicates
     *
     * @param base  the base list
     * @param items the list that is going to merge into the base list
     * @param <T>   the type of list item, which should be a type extending {@link IGif}
     * @return the joined list
     */
    @NonNull
    public static <T extends IGif> List<T> orJoinGif(List<T> base, List<T> items) {

        if (base == null && items == null) {
            return new ArrayList<>();
        }

        if (base == null) {
            return items;
        }

        if (items == null) {
            return base;
        }

        final Set<String> set = new HashSet<>();
        for (T t : base) {
            set.add(t.getId());
        }

        for (T t : items) {
            if (!set.contains(t.getId())) {
                set.add(t.getId());
                base.add(t);
            }
        }
        return base;
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

    /**
     * Split a list into several sub-lists; each sub-lists has length equal the given limit
     *
     * @param list  the given list
     * @param limit the given limit of each sub-list
     */
    public static <T> List<List<T>> splits(@Nullable final List<T> list, int limit) {
        List<List<T>> result = new ArrayList<>();
        if (isEmpty(list)) {
            return result;
        }

        if (limit < 2) {
            result.add(list);
            return result;
        }

        List<T> row = new ArrayList<>();
        for (T item : list) {
            row.add(item);
            if (row.size() == limit) {
                result.add(row);
                row = new ArrayList<>();
            }
        }

        if (!isEmpty(row)) {
            result.add(row);
        }
        return result;
    }

    /**
     * Merge lists while ignoring duplicates
     *
     * @param base  the base list
     * @param items the list that is going to merge into the base list
     * @param <T>   the type of list item, which should be a type extending {@link ICollection}
     * @return the joined list
     */
    @NonNull
    public static <T extends ICollection> List<T> orJoinCollection(List<T> base, List<T> items) {

        if (base == null && items == null) {
            return new ArrayList<>();
        }

        if (base == null) {
            return items;
        }

        if (items == null) {
            return base;
        }

        final Set<String> set = new HashSet<>();
        for (T t : base) {
            set.add(t.getName());
        }

        for (T t : items) {
            if (!set.contains(t.getName())) {
                set.add(t.getName());
                base.add(t);
            }
        }
        return base;
    }
}
