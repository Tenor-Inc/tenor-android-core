package com.tenor.android.core.measurable;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Locale;

public class MeasurableViewHolderEvent extends ArrayMap<String, String> {

    public MeasurableViewHolderEvent(@NonNull MeasurableViewHolderData data,
                                     @NonNull String utcOffset) {
        this(data.getId(), "view", utcOffset, data.getVisualPosition());
        put("elapsed_ms", String.format(Locale.US, "%d", data.getAccumulatedVisibleDuration()));
        put("visible_fraction", String.format(Locale.US, "%f", data.getVisibleFraction()));

    }

    public MeasurableViewHolderEvent(@NonNull String sourceId,
                                     @NonNull String action,
                                     @NonNull String utcOffset,
                                     @NonNull String visualPosition) {
        super(7);
        put("source_id", sourceId);
        put("timestamp", String.format(Locale.US, "%d", System.currentTimeMillis()));
        put("action", action);
        put("timezone", utcOffset);
        put("visual_pos", visualPosition);
    }
}
