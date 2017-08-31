package com.tenor.android.core.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;

@Retention(RetentionPolicy.SOURCE)
@StringDef({ScreenDensities.UNKNOWN,
        ScreenDensities.SD_075, ScreenDensities.SD_100, ScreenDensities.SD_150,
        ScreenDensities.SD_200, ScreenDensities.SD_300, ScreenDensities.SD_400,
})
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE})
public @interface ScreenDensity {
}
