package com.tenor.android.core.model.impl;

import android.support.annotation.FloatRange;

import com.tenor.android.core.sponsorable.ItemBadgePosition;
import com.tenor.android.core.sponsorable.impl.ItemBadgePositions;

public class ItemBadge extends Image {

    private static final long serialVersionUID = 5769727680233855104L;

    private int position;
    private float threshold;

    @ItemBadgePosition
    public int getPosition() {
        return ItemBadgePositions.parse(position);
    }

    @FloatRange(from = 0.01f, to = 1f)
    public float getThreshold() {
        return threshold >= 0.01f && threshold <= 1f ? threshold : 1f;
    }
}
