package com.tenor.android.core.rvwidget;

import android.support.annotation.NonNull;

/**
 * All recycler view items should inherit this abstract class
 */
public abstract class AbstractRVItem implements IRVItem {

    private final int mType;
    private final String mId;

    public AbstractRVItem(int type) {
        this(type, String.valueOf(type));
    }

    public AbstractRVItem(int type, @NonNull final String id) {

        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

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
}
