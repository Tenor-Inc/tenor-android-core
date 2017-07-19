package com.tenor.android.core.model.impl;


import android.support.annotation.NonNull;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.sponsorable.ItemBadgePosition;
import com.tenor.android.core.sponsorable.impl.ItemBadgePositions;

import java.io.Serializable;

public class ItemBadge implements Serializable {

    private static final long serialVersionUID = 5769727680233855104L;

    private String url;
    private int position;

    @NonNull
    public String getUrl() {
        return StringConstant.getOrEmpty(url);
    }

    @ItemBadgePosition
    public int getPosition() {
        return ItemBadgePositions.parse(position);
    }
}
