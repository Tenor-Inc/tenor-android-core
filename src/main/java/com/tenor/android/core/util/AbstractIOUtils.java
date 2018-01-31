package com.tenor.android.core.util;

import java.io.Closeable;
import java.io.IOException;

public class AbstractIOUtils {

    public static void close(final Closeable... closeables) {
        if (closeables == null || closeables.length <= 0) {
            return;
        }

        try {
            for (Closeable closeable : closeables) {
                if (closeable == null) {
                    continue;
                }
                closeable.close();
            }
        } catch (final IOException ignored) {
            // do nothing
        }
    }
}
