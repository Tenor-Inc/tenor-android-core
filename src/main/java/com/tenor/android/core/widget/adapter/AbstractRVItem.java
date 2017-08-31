package com.tenor.android.core.widget.adapter;

import android.support.annotation.NonNull;

/**
 * All recycler view items should inherit this abstract class
 */
public abstract class AbstractRVItem implements IRVItem {

    private final int mType;
    private final String mId;
    private int mRelativePosition = -1;

    public AbstractRVItem(int type) {
        this(type, String.valueOf(type));
    }

    public AbstractRVItem(int type, @NonNull final String id) {
        mType = type;
        mId = id;
    }

    /**
     * Get the type for this {@link AbstractRVItem}
     *
     * @return the type
     */
    @Override
    public int getType() {
        return mType;
    }

    /**
     * Get the unique identifier for this {@link AbstractRVItem}
     *
     * @return the id
     */
    @Override
    @NonNull
    public String getId() {
        return mId;
    }

    @Override
    public int getRelativePosition() {
        return mRelativePosition;
    }

    /**
     * Set the position relative to the same {@link #getType()},
     * this may not may not be the same as the adapter position
     *
     * @param relativePosition the relative position
     */
    public AbstractRVItem setRelativePosition(int relativePosition) {
        mRelativePosition = relativePosition;
        return this;
    }
}
