package com.tenor.android.core.util;

public abstract class AbstractTimestampUtils {

    /**
     * @param then       previous timestamp in milliseconds
     * @param now        previous timestamp in milliseconds
     * @param gt         time difference greater than this time in milliseconds
     * @param gInclusive use greater or equal to
     * @param lt         time difference less than this time in milliseconds
     * @param lInclusive use less or equal to
     */
    public static boolean isTimeDiffWithinRange(long then, long now,
                                                long gt, boolean gInclusive,
                                                long lt, boolean lInclusive) {
        if (then < 0 || now < 0 || gt < 0 || lt < 0) {
            throw new IllegalArgumentException("inputs cannot be less than 0");
        }

        if (then > now) {
            throw new IllegalArgumentException("then cannot be larger than now");
        }

        final long diff = now - then;

        boolean result = true;
        if (gInclusive) {
            result &= diff >= gt;
        } else {
            result &= diff > gt;
        }

        if (lInclusive) {
            result &= diff <= lt;
        } else {
            result &= diff < lt;
        }
        return result;
    }
}
