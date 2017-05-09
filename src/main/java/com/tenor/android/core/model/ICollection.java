package com.tenor.android.core.model;

/**
 * The interface of Collection
 */
public interface ICollection {
    /**
     * Check if this is a collection based on preference tag
     */
    boolean isCollection();

    /**
     * Check if this is a collection based on preference tag
     *
     * @deprecated
     */
    boolean isCollectionTag();

    /**
     * Get the name of this collection
     */
    String getName();
}
