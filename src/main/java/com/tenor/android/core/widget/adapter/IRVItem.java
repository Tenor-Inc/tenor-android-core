package com.tenor.android.core.widget.adapter;

import android.support.annotation.NonNull;

/**
 * Interface for all recycler view items
 */
public interface IRVItem {
    int getType();

    @NonNull
    String getId();

    /**
     * Get the position that is relative to the same type, instead of the adapter position
     *
     * @return the relative position to the same type
     */
    int getRelativePosition();
}
