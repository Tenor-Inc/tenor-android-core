package com.tenor.android.core.util;

import android.os.Build;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Get the better perform or backward compatible {@link Random} base on API version
 */
public class RandomCompat {

    public static Random get() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ThreadLocalRandom.current();
        } else {
            return new ThreadLocal<Random>() {
                @Override
                protected Random initialValue() {
                    return new Random();
                }
            }.get();
        }
    }
}
