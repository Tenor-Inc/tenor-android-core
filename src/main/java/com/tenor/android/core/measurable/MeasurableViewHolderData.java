package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tenor.android.core.concurrency.WeakRefObject;
import com.tenor.android.core.constant.ItemVisualPosition;
import com.tenor.android.core.constant.ItemVisualPositions;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractLocaleUtils;

import java.io.Serializable;

public class MeasurableViewHolderData<VH extends IMeasurableViewHolder> extends WeakRefObject<VH> implements Serializable {
    private static final long serialVersionUID = 675815364606601681L;

    private int mVisibility = View.INVISIBLE;

    @IntRange(from = 0)
    private int mAccumulatedVisibleDuration = 0;

    @IntRange(from = 0)
    private int mAccumulatedVisibleCount = 0;

    @IntRange(from = -1)
    private long mTimestampOnVisible = -1;

    @FloatRange(from = 0f, to = 1f)
    private float mVisibleFraction = 0f;

    @ItemVisualPosition
    private String mVisualPosition = ItemVisualPositions.UNKNOWN;

    private String mId = StringConstant.EMPTY;

    @FloatRange(from = 0.01f, to = 1f)
    private float mThreshold = 1f;

    public MeasurableViewHolderData(@NonNull VH viewHolder) {
        super(viewHolder);
        resetTimestamp();
        resetCounts();
    }

    @NonNull
    public String getId() {
        return mId;
    }

    public void setId(@NonNull String id) {
        mId = StringConstant.getOrEmpty(id);
    }

    public void setThreshold(@FloatRange(from = 0.01f, to = 1f) float threshold) {
        mThreshold = threshold;
    }

    @FloatRange(from = 0.01f, to = 1f)
    public float getThreshold() {
        return mThreshold;
    }

    @ItemVisualPosition
    public String getVisualPosition() {
        return mVisualPosition;
    }

    public void setVisualPosition(@ItemVisualPosition String visualPosition) {
        mVisualPosition = visualPosition;
    }

    public synchronized void clear() {
        mVisibility = View.INVISIBLE;
        resetTimestamp();
        resetCounts();
        mVisibleFraction = 0f;
    }

    /**
     * Notify {@link MeasurableViewHolderData} that the view and content of this view holder has been
     * finalized and all facts should be gathered and stored on {@link MeasurableViewHolderData}
     * <p>
     * This method should be called on the {@link IMeasurableViewHolder#onContentReady(String, float)},
     * or the similar corresponding method on the subclass of {@link IMeasurableViewHolder}.
     */
    public synchronized void onViewHolderFullyReady(@NonNull String id,
                                                    @FloatRange(from = 0.01f, to = 1f) float threshold,
                                                    @FloatRange(from = 0f, to = 1f) float visibleFraction,
                                                    @ItemVisualPosition String visualPosition) {
        updateTimestamp();
        setId(id);
        setThreshold(threshold);
        setVisibleFraction(visibleFraction);
        setVisualPosition(visualPosition);
    }

    private synchronized void resetCounts() {
        mAccumulatedVisibleDuration = 0;
        mAccumulatedVisibleCount = 0;
    }

    private synchronized void resetTimestamp() {
        mTimestampOnVisible = -1;
    }

    public synchronized void updateTimestamp() {
        mTimestampOnVisible = System.currentTimeMillis();
    }

    public int getAdapterPosition() {
        //noinspection ConstantConditions
        return hasRef() ? getRef().getAdapterPosition() : RecyclerView.NO_POSITION;
    }

    public boolean isVisible() {
        return mVisibility == View.VISIBLE;
    }

    public synchronized int getAccumulatedVisibleDuration() {
        return mAccumulatedVisibleDuration;
    }

    public synchronized int getAccumulatedVisibleCount() {
        return mAccumulatedVisibleCount;
    }

    @FloatRange(from = 0f, to = 1f)
    public float getVisibleFraction() {
        return mVisibleFraction;
    }

    public synchronized void pause() {
        // becomes invisible
        if (mTimestampOnVisible < 0) {
            return;
        }
        final long duration = System.currentTimeMillis() - mTimestampOnVisible;
        mAccumulatedVisibleDuration += duration;
        resetTimestamp();
    }

    public synchronized void resume() {
        // update the timestamp
        updateTimestamp();
    }

    public synchronized void destroy(@NonNull Context context) {
        Log.e("===>", "======> item[" + getAdapterPosition() + "], destroy !!!");
        flush(context);
    }

    public synchronized void flush(@NonNull Context context) {
        setVisibleFraction(0f);
        if (getAccumulatedVisibleDuration() > 0 || getAccumulatedVisibleCount() > 0) {
            Log.e("===>", "======> item[" + getAdapterPosition()
                    + "], flushed, viewed for: " + getAccumulatedVisibleDuration()
                    + ", counted for: " + getAccumulatedVisibleCount());
        }
        ViewHolderDataManager.push(context, this, AbstractLocaleUtils.getUtcOffset(context));
        clear();
    }

    public synchronized void setVisibleFraction(@FloatRange(from = 0f, to = 1f) float visibleFraction) {
        mVisibleFraction = visibleFraction;

        final boolean wasVisible = isVisible();
        final int nextVisibility = visibleFraction >= mThreshold ? View.VISIBLE : View.INVISIBLE;
        final boolean isVisible = nextVisibility == View.VISIBLE;

        final boolean visibilityChanged = wasVisible ^ isVisible;
        if (!visibilityChanged) {
            // visibility hasn't changed, do nothing
            return;
        }

        // update visibility
        mVisibility = nextVisibility;

        // visibility has changed
        if (isVisible) {
            becomesVisible();
        } else {
            becomesInvisible();
        }
    }

    private void becomesVisible() {
        updateTimestamp();
        mAccumulatedVisibleCount++;
        Log.e("===>", "======> item[" + getAdapterPosition() + "] becomes Visible !!!");
    }

    private void becomesInvisible() {
        if (mTimestampOnVisible < 0) {
            return;
        }
        final long duration = System.currentTimeMillis() - mTimestampOnVisible;
        mAccumulatedVisibleDuration += duration;
        resetTimestamp();
        Log.e("===>", "======> item[" + getAdapterPosition() + "] becomes Invisible !!!");
    }
}
