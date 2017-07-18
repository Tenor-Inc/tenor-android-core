package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tenor.android.core.concurrency.WeakRefObject;

import java.io.Serializable;

public class MeasurableViewHolderData<VH extends IMeasurableViewHolder> extends WeakRefObject<VH> implements Serializable {
    private static final long serialVersionUID = 675815364606601681L;

    private int mVisibility = View.INVISIBLE;

    @IntRange(from = 0)
    private int mAccumulatedVisibleTime = 0;

    @IntRange(from = 0)
    private int mAccumulatedVisibleCount = 0;

    @IntRange(from = -1)
    private long mLastVisibleStartTime = -1;

    @FloatRange(from = 0f, to = 1f)
    private float mVisibleFraction = 0f;

    @FloatRange(from = 0f, to = 1f)
    private final float mThreshold;

    public MeasurableViewHolderData(@NonNull final VH viewHolder, float threshold) {
        super(viewHolder);
        mLastVisibleStartTime = -1;
        mAccumulatedVisibleTime = 0;
        mAccumulatedVisibleCount = 0;
        mThreshold = threshold;
    }

    public void clear() {
        mVisibility = View.INVISIBLE;
        mLastVisibleStartTime = -1;
        mAccumulatedVisibleTime = 0;
        mAccumulatedVisibleCount = 0;
        mVisibleFraction = 0f;
    }

    public int getAdapterPosition() {
        //noinspection ConstantConditions
        return hasRef() ? getRef().getAdapterPosition() : RecyclerView.NO_POSITION;
    }

    public boolean isVisible() {
        return mVisibility == View.VISIBLE;
    }

    public int getAccumulatedVisibleTime() {
        return mAccumulatedVisibleTime;
    }

    public int getAccumulatedVisibleCount() {
        return mAccumulatedVisibleCount;
    }

    @FloatRange(from = 0f, to = 1f)
    public float getVisibleFraction() {
        return mVisibleFraction;
    }

    public void pause() {
        // becomes invisible
        if (mLastVisibleStartTime < 0) {
            return;
        }
        final long duration = System.currentTimeMillis() - mLastVisibleStartTime;
        mAccumulatedVisibleTime += duration;
        mLastVisibleStartTime = -1;
    }

    public void resume() {
        // update the timestamp
        mLastVisibleStartTime = System.currentTimeMillis();
    }

    public void destroy(@NonNull Context context) {
        Log.e("===>", "======> item[" + getAdapterPosition() + "], destroy !!!");
        flush(context);
    }

    public void flush(@NonNull Context context) {
        setVisibleFraction(0f);
        // TODO: to be implemented
        // ViewHolderDataManager.push(context, AbstractGsonUtils.getInstance().toJson(this));
        if (getAccumulatedVisibleTime() > 0 || getAccumulatedVisibleCount() > 0) {
            Log.e("===>", "======> item[" + getAdapterPosition()
                    + "], flushed, viewed for: " + getAccumulatedVisibleTime()
                    + "], counted for: " + getAccumulatedVisibleCount());
        }
        clear();
    }

    public void setVisibleFraction(float visibleFraction) {
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
        mLastVisibleStartTime = System.currentTimeMillis();
        Log.e("===>", "======> item[" + getAdapterPosition() + "] becomes Visible !!!");
    }

    private void becomesInvisible() {
        if (mLastVisibleStartTime < 0) {
            return;
        }
        final long duration = System.currentTimeMillis() - mLastVisibleStartTime;
        mAccumulatedVisibleTime += duration;
        mAccumulatedVisibleCount++;
        mLastVisibleStartTime = -1;
        Log.e("===>", "======> item[" + getAdapterPosition() + "] becomes Invisible !!!");
    }
}
