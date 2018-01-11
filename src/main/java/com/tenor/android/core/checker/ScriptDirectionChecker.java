package com.tenor.android.core.checker;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.tenor.android.core.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class ScriptDirectionChecker {

    @Retention(RetentionPolicy.CLASS)
    @IntDef({UNSPECIFIED, LEFT_TO_RIGHT, RIGHT_TO_LEFT})
    public @interface Value {
    }

    public static final int UNSPECIFIED = -1;
    public static final int LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;

    @ScriptDirectionChecker.Value
    public static int checkSelfScriptDirection(@Nullable Context context) {
        if (context == null || context.getResources() == null) {
            return UNSPECIFIED;
        }

        if (context.getResources().getBoolean(R.bool.right_to_left)) {
            return RIGHT_TO_LEFT;
        } else {
            return LEFT_TO_RIGHT;
        }
    }
}
