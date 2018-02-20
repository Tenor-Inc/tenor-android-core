package com.tenor.android.core.measurable;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.ItemVisualPosition;
import com.tenor.android.core.constant.ViewAction;

import java.io.Serializable;
import java.util.Locale;

public class MeasurableViewHolderEvent implements Serializable {

    private static final long serialVersionUID = 2288577105348192434L;

    @SerializedName("source_id")
    private final String mSourceId;

    @SerializedName("timestamp")
    private final String mTimestamp;

    @SerializedName("action")
    private final String mAction;

    @SerializedName("timezone")
    private final String mUtcOffset;

    @ItemVisualPosition.Value
    @SerializedName("visual_pos")
    private final String mVisualPosition;

    @SerializedName("elapsed_ms")
    private String mDuration;

    @SerializedName("visible_fraction")
    private String mVisibleFraction;

    public MeasurableViewHolderEvent(@NonNull MeasurableViewHolderData data,
                                     @NonNull String utcOffset) {
        this(data.getId(), ViewAction.VIEW, utcOffset, data.getVisualPosition());
        mDuration = String.format(Locale.US, "%d", data.getAccumulatedVisibleDuration());
        mVisibleFraction = String.format(Locale.US, "%f", data.getVisibleFraction());
    }

    public MeasurableViewHolderEvent(@NonNull String sourceId,
                                     @ViewAction.Value String action,
                                     @NonNull String utcOffset,
                                     @ItemVisualPosition.Value String visualPosition) {
        mSourceId = sourceId;
        mTimestamp = String.format(Locale.US, "%d", System.currentTimeMillis());
        mAction = action;
        mUtcOffset = utcOffset;
        mVisualPosition = visualPosition;
    }
}
