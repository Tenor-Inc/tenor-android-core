package com.tenor.android.core.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.CLASS)
@IntDef({ItemBadgePositions.NONE,
        ItemBadgePositions.INTERIOR_TOP_LEFT, ItemBadgePositions.INTERIOR_TOP_RIGHT,
        ItemBadgePositions.INTERIOR_BOTTOM_RIGHT, ItemBadgePositions.INTERIOR_BOTTOM_LEFT,
        ItemBadgePositions.EXTERIOR_TOP_LEFT, ItemBadgePositions.EXTERIOR_TOP_RIGHT,
        ItemBadgePositions.EXTERIOR_BOTTOM_RIGHT, ItemBadgePositions.EXTERIOR_BOTTOM_LEFT})
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface ItemBadgePosition {
}
