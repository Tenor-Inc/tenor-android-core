package com.tenor.android.core.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public class CoreIOUtils {

    public static void close(final Closeable... closeables) {
        if (closeables == null || closeables.length <= 0) {
            return;
        }

        for (Closeable closeable : closeables) {
            if (closeable == null) {
                continue;
            }

            try {
                closeable.close();
            } catch (IOException ignored) {
                // do nothing
            }
        }
    }

    public static void flush(final Flushable... flushables) {
        if (flushables == null || flushables.length <= 0) {
            return;
        }

        for (Flushable flushable : flushables) {
            if (flushable == null) {
                continue;
            }

            try {
                flushable.flush();
            } catch (IOException ignored) {
                // do nothing
            }
        }
    }
}
