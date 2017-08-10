package com.tenor.android.core.rvwidget;

/**
 * Interface for all recycler view items
 */
public interface IRVItem {
    int getType();

    String getId();

    /**
     * Get the position that is relative to the same type, instead of the adapter position
     *
     * @return the relative position to the same type
     */
    int getRelativePosition();
}
