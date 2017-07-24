package com.tenor.android.core.measurable;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MeasurableRecyclerViewHelper {

    public static void onConstruct(@NonNull Context context, @NonNull final RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new MeasurableOnScrollListener<>(context));

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {

            @Override
            public void onChildViewAttachedToWindow(View view) {
                RecyclerView.ViewHolder vh = recyclerView.findContainingViewHolder(view);
                if (vh instanceof IMeasurableViewHolder) {
                    IMeasurableViewHolder holder = (IMeasurableViewHolder) vh;
                    holder.attachMeasurer(recyclerView);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                RecyclerView.ViewHolder vh = recyclerView.findContainingViewHolder(view);
                if (vh instanceof IMeasurableViewHolder) {
                    IMeasurableViewHolder holder = (IMeasurableViewHolder) vh;
                    holder.detachMeasurer();
                }
            }
        });
    }

    /**
     * Notify {@link RecyclerView.ViewHolder}, which has implemented {@link IMeasurableViewHolder}
     * in the given {@link RecyclerView} that a {@link IMeasurableViewHolder#measure(RecyclerView)}
     * needs to be performed
     * <p>
     * Method got called once a scroll is reached the idle state
     * <p>
     * This updates all the stored {@link MeasurableViewHolderData} data in case any of them is not
     * properly updated due to reason, such as fast scrolling
     *
     * @param recyclerView the given {@link RecyclerView}
     */
    public static void notifyMeasurableViewHolderDataSetChanged(@NonNull RecyclerView recyclerView) {
        final int end = recyclerView.getAdapter().getItemCount() - 1;
        notifyMeasurableViewHolderDataRangeChanged(recyclerView, 0, end);
    }

    /**
     * Notify {@link RecyclerView.ViewHolder}, which has implemented {@link IMeasurableViewHolder}
     * in the given {@link RecyclerView} located between {@code start} and {@code end} that a
     * {@link IMeasurableViewHolder#measure(RecyclerView)} needs to be performed
     * <p>
     * Method got called once a scroll is reached the idle state
     * <p>
     * This updates all the stored {@link MeasurableViewHolderData} data in case any of them is not
     * properly updated due to reason, such as fast scrolling
     *
     * @param recyclerView the given {@link RecyclerView}
     * @param start        the start position
     * @param end          the end position
     */
    public static void notifyMeasurableViewHolderDataRangeChanged(@NonNull RecyclerView recyclerView, int start, int end) {

        if (start == RecyclerView.NO_POSITION || end == RecyclerView.NO_POSITION) {
            return;
        }

        List<IMeasurableViewHolder> holders = getViewHolders(recyclerView, IMeasurableViewHolder.class, start, end);
        /*
         * [ANDROID-1830,1851]
         *
         * check if it is actually completely visible, only check the cases that we know
         * it is not completely visible, a.k.a visible fraction < 1.0f
         */
        for (IMeasurableViewHolder holder : holders) {
            holder.measure(recyclerView);
        }
    }

    public static void notifyViewHoldersOnPause(@NonNull RecyclerView recyclerView) {
        // handle pause
        List<IMeasurableViewHolder> holders =
                MeasurableRecyclerViewHelper.getViewHolders(recyclerView, IMeasurableViewHolder.class);
        for (IMeasurableViewHolder holder : holders) {
            holder.pauseMeasurer(recyclerView);
        }
    }

    public static void notifyViewHoldersOnResume(@NonNull RecyclerView recyclerView) {
        // handle resume
        List<IMeasurableViewHolder> holders =
                MeasurableRecyclerViewHelper.getViewHolders(recyclerView, IMeasurableViewHolder.class);
        for (IMeasurableViewHolder holder : holders) {
            holder.resumeMeasurer(recyclerView);
        }
    }

    public static void notifyMeasurableViewHoldersOnRefresh(@NonNull RecyclerView recyclerView) {
        flushMeasurableViewHolderDataSet(recyclerView);
    }

    public static void flushMeasurableViewHolderDataSet(@NonNull RecyclerView recyclerView) {
        /*
         * for all views those are still visible, mark them all as invisible, update the time,
         * and calculate the time accumulation
         */
        List<IMeasurableViewHolder> holders =
                MeasurableRecyclerViewHelper.getViewHolders(recyclerView, IMeasurableViewHolder.class);
        for (IMeasurableViewHolder holder : holders) {
            holder.flush();
        }
    }

    /**
     * Get all <b>attached</b> {@link RecyclerView.ViewHolder} in the given {@link RecyclerView}
     * that has implemented or extended the the given {@link T} class
     *
     * @param recyclerView the given {@link RecyclerView}
     * @param cls          the class of {@link android.support.v7.widget.RecyclerView.ViewHolder} of interest in the given {@link RecyclerView}
     */
    public static <T extends IViewHolder> List<T> getViewHolders(@Nullable RecyclerView recyclerView, Class<T> cls) {
        final List<T> list = new ArrayList<>();
        if (recyclerView == null) {
            return list;
        }

        final int end = recyclerView.getAdapter().getItemCount() - 1;
        return getViewHolders(recyclerView, cls, 0, end);
    }

    /**
     * Get all <b>attached</b> {@link RecyclerView.ViewHolder} in the given {@link RecyclerView}
     * that has implemented or extended the the given {@link T} class in the given range between
     * {@code start} and {@code end}
     *
     * @param recyclerView the given {@link RecyclerView}
     * @param cls          the class of {@link android.support.v7.widget.RecyclerView.ViewHolder} of interest in the given {@link RecyclerView}
     * @param start        the start position
     * @param end          the end position
     */
    public static <T extends IViewHolder> List<T> getViewHolders(@Nullable RecyclerView recyclerView, Class<T> cls, int start, int end) {
        final List<T> list = new ArrayList<>(end);
        if (recyclerView == null || recyclerView.getAdapter() == null) {
            return list;
        }

        final int count = recyclerView.getAdapter().getItemCount();
        if (start < 0 || end >= count) {
            return list;
        }

        RecyclerView.ViewHolder holder;
        for (int i = start; i <= end; i++) {
            holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (cls.isInstance(holder)) {
                //noinspection unchecked
                list.add((T) holder);
            }
        }
        return list;
    }
}
