package com.tenor.android.core.constant.ad;

import android.support.annotation.IntDef;

import com.tenor.android.core.constant.ad.impl.AdIconPositions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.CLASS)
@IntDef({AdIconPositions.NONE,
        AdIconPositions.INTERIOR_TOP_LEFT, AdIconPositions.INTERIOR_TOP_RIGHT,
        AdIconPositions.INTERIOR_BOTTOM_RIGHT, AdIconPositions.INTERIOR_BOTTOM_LEFT,
        AdIconPositions.EXTERIOR_TOP_LEFT, AdIconPositions.EXTERIOR_TOP_RIGHT,
        AdIconPositions.EXTERIOR_BOTTOM_RIGHT, AdIconPositions.EXTERIOR_BOTTOM_LEFT})
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface AdIconPosition {
}
