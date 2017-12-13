package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tenor.android.core.constant.ItemVisualPosition;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.util.AbstractLogUtils;
import com.tenor.android.core.weakref.WeakRefObject;

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

    @ItemVisualPosition.Value
    private String mVisualPosition = ItemVisualPosition.UNKNOWN;

    private String mId = StringConstant.EMPTY;

    private boolean mEnhancedContent = false;

    @FloatRange(from = 0.01f, to = 1f)
    private float mThreshold = 1f;

    @IntRange(from = -1, to = Integer.MAX_VALUE)
    private int mAdapterPosition = RecyclerView.NO_POSITION;

    public MeasurableViewHolderData(@NonNull VH viewHolder) {
        super(viewHolder);
        resetTimestamp();
        resetCounts();
    }

    @NonNull
    public String getId() {
        return mId;
    }

    /**
     * Set identifier to uniquely identify the referenced {@link IMeasurableViewHolder}
     *
     * @param id the id, such as {@link Result#getSourceId()}
     */
    public void setId(@NonNull String id) {
        mId = StringConstant.getOrEmpty(id);
    }

    /**
     * Set to true if this data should be used to improve future content delivery experience
     *
     * @param enhancedContent the boolean to determine if this data should be used to improve
     *                        future content delivery experience
     */
    public void setEnhancedContent(boolean enhancedContent) {
        mEnhancedContent = enhancedContent;
    }

    public void setThreshold(@FloatRange(from = 0.01f, to = 1f) float threshold) {
        mThreshold = threshold;
    }

    @FloatRange(from = 0.01f, to = 1f)
    public float getThreshold() {
        return mThreshold;
    }

    public boolean isVisualPositionUnknown() {
        return ItemVisualPosition.UNKNOWN.equals(getVisualPosition());
    }

    @ItemVisualPosition.Value
    public String getVisualPosition() {
        return mVisualPosition;
    }

    public void setVisualPosition(@ItemVisualPosition.Value String visualPosition) {
        mVisualPosition = visualPosition;
    }

    public synchronized void clear() {
        mVisibility = View.INVISIBLE;
        resetTimestamp();
        resetCounts();
        mVisibleFraction = 0f;
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
        // reference GCed
        if (getRef() == null) {
            return mAdapterPosition;
        }

        // adapter position is not initialized
        if (mAdapterPosition == RecyclerView.NO_POSITION) {
            mAdapterPosition = getRef().getAdapterPosition();
        }

        // adapter position changed to a non NO_POSITION position
        if (getRef().getAdapterPosition() != RecyclerView.NO_POSITION
                && mAdapterPosition != getRef().getAdapterPosition()) {
            mAdapterPosition = getRef().getAdapterPosition();
        }
        return mAdapterPosition;
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
        updateTimestamp();
    }

    public synchronized void flush(@NonNull Context context) {
        setVisibleFraction(0f);

        final boolean useful = mEnhancedContent
                && getAccumulatedVisibleDuration() > 0 && getAccumulatedVisibleCount() > 0;
        AbstractLogUtils.e(this, "==> flushed\n" + (useful ? toString() : StringConstant.EMPTY));
        if (useful) {
            ViewHolderDataManager.push(context, this);
        }
        clear();
    }

    public synchronized void setVisibleFraction(@FloatRange(from = 0f, to = 1f) float visibleFraction) {

        final boolean wasVisible = mVisibleFraction >= mThreshold;
        final boolean isVisible = visibleFraction >= mThreshold;
        mVisibleFraction = visibleFraction;

        final boolean visibilityChanged = wasVisible ^ isVisible;
        if (!visibilityChanged) {
            // visibility hasn't changed, do nothing
            return;
        }

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
        AbstractLogUtils.e(this, "==> item[" + getAdapterPosition() + "] becomes Visible");
    }

    private void becomesInvisible() {
        if (mTimestampOnVisible < 0) {
            return;
        }
        final long duration = System.currentTimeMillis() - mTimestampOnVisible;
        mAccumulatedVisibleDuration += duration;
        resetTimestamp();
        AbstractLogUtils.e(this, "==> item[" + getAdapterPosition() + "] becomes Invisible");
    }

    public String toString() {
        return "adapter_pos: " + getAdapterPosition() + "\n"
                + "source_id: " + mId + "\n"
                + "visual_pos: " + mVisualPosition + "\n"
                + "viewed: " + getAccumulatedVisibleDuration() + " ms\n"
                + "counted: " + getAccumulatedVisibleCount() + "\n"
                + "enhanced_content: " + mEnhancedContent + "\n";
    }
}
