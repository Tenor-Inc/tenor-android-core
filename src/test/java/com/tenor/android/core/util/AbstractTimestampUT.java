package com.tenor.android.core.util;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractTimestampUT {
    @Test
    public void isTimeDiffWithinRange() throws Exception {
        assertTrue(AbstractTimestampUtils.isTimeDiffWithinRange(
                1000, 5999,
                TimeUnit.SECONDS.toMillis(1), false, TimeUnit.SECONDS.toMillis(5), false));

        assertFalse(AbstractTimestampUtils.isTimeDiffWithinRange(
                1000, 6000,
                TimeUnit.SECONDS.toMillis(1), false, TimeUnit.SECONDS.toMillis(5), false));

        assertFalse(AbstractTimestampUtils.isTimeDiffWithinRange(
                1000, 6001,
                TimeUnit.SECONDS.toMillis(1), false, TimeUnit.SECONDS.toMillis(5), false));

        assertFalse(AbstractTimestampUtils.isTimeDiffWithinRange(
                5001, 6000,
                TimeUnit.SECONDS.toMillis(1), false, TimeUnit.SECONDS.toMillis(5), false));
    }
}