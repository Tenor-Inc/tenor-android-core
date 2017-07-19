package com.tenor.android.core.sponsorable.impl;

import com.tenor.android.core.sponsorable.ItemBadgePosition;

public class ItemBadgePositions {

    public static final int NONE = 0;
    public static final int INTERIOR_TOP_LEFT = 1;
    public static final int INTERIOR_TOP_RIGHT = 2;
    public static final int INTERIOR_BOTTOM_RIGHT = 3;
    public static final int INTERIOR_BOTTOM_LEFT = 4;
    public static final int EXTERIOR_TOP_LEFT = 5;
    public static final int EXTERIOR_TOP_RIGHT = 6;
    public static final int EXTERIOR_BOTTOM_RIGHT = 7;
    public static final int EXTERIOR_BOTTOM_LEFT = 8;

    @ItemBadgePosition
    public static int parse(int position) {
        switch (position) {
            case INTERIOR_TOP_LEFT:
                return INTERIOR_TOP_LEFT;
            case INTERIOR_TOP_RIGHT:
                return INTERIOR_TOP_RIGHT;
            case INTERIOR_BOTTOM_RIGHT:
                return INTERIOR_BOTTOM_RIGHT;
            case INTERIOR_BOTTOM_LEFT:
                return INTERIOR_BOTTOM_LEFT;
            case EXTERIOR_TOP_LEFT:
                return EXTERIOR_TOP_LEFT;
            case EXTERIOR_TOP_RIGHT:
                return EXTERIOR_TOP_RIGHT;
            case EXTERIOR_BOTTOM_RIGHT:
                return EXTERIOR_BOTTOM_RIGHT;
            case EXTERIOR_BOTTOM_LEFT:
                return EXTERIOR_BOTTOM_LEFT;
            case NONE:
            default:
                return NONE;
        }
    }

    public static boolean isExterior(@ItemBadgePosition int position) {
        switch (position) {
            case EXTERIOR_TOP_LEFT:
            case EXTERIOR_TOP_RIGHT:
            case EXTERIOR_BOTTOM_RIGHT:
            case EXTERIOR_BOTTOM_LEFT:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInterior(@ItemBadgePosition int position) {
        switch (position) {
            case ItemBadgePositions.INTERIOR_TOP_LEFT:
            case ItemBadgePositions.INTERIOR_TOP_RIGHT:
            case ItemBadgePositions.INTERIOR_BOTTOM_RIGHT:
            case ItemBadgePositions.INTERIOR_BOTTOM_LEFT:
                return true;
            default:
                return false;
        }
    }
}
