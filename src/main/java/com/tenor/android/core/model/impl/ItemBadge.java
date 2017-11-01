package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.constant.ItemBadgePosition;
import com.tenor.android.core.constant.StringConstant;

public class ItemBadge extends Image {

    private static final long serialVersionUID = 5769727680233855104L;

    private int position;
    private String provider;

    @ItemBadgePosition.Value
    public int getPosition() {
        return ItemBadgePosition.parse(position);
    }

    @NonNull
    public String getProvider() {
        return StringConstant.getOrEmpty(provider);
    }
}
