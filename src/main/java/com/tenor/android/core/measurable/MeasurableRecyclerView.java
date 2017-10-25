package com.tenor.android.core.measurable;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tenor.android.core.widget.WeakRefRecyclerView;

public class MeasurableRecyclerView extends WeakRefRecyclerView implements IMeasurableRecyclerView {

    public MeasurableRecyclerView(Context context) {
        this(context, null);
    }

    public MeasurableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasurableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        MeasurableRecyclerViewHelper.onConstruct(context, this);
    }

    @Override
    public void notifyMeasurableViewHoldersOnPause() {
        MeasurableRecyclerViewHelper.notifyViewHoldersOnPause(this);
    }

    @Override
    public void notifyMeasurableViewHoldersOnResume() {
        MeasurableRecyclerViewHelper.notifyViewHoldersOnResume(this);
    }

    @Override
    public void notifyMeasurableViewHoldersOnRefresh() {
        MeasurableRecyclerViewHelper.notifyMeasurableViewHoldersOnRefresh(this);
    }

    @Override
    public void flushMeasurableViewHolderDataSet() {
        MeasurableRecyclerViewHelper.flushMeasurableViewHolderDataSet(this);
    }

    @Override
    public void notifyViewHolderDataSetChanged() {
        MeasurableRecyclerViewHelper.notifyMeasurableViewHolderDataSetChanged(this);
    }

    @Override
    public void notifyViewHolderDataRangeChanged(int start, int end) {
        MeasurableRecyclerViewHelper.notifyMeasurableViewHolderDataRangeChanged(this, start, end);
    }
}
