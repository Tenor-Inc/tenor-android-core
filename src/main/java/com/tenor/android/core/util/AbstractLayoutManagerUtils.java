package com.tenor.android.core.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tenor.android.core.constant.ItemVisualPosition;
import com.tenor.android.core.constant.ItemVisualPositions;

public abstract class AbstractLayoutManagerUtils {

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @return {@link OrientationHelper#HORIZONTAL} or {@link OrientationHelper#VERTICAL}
     */
    public static <T extends RecyclerView.LayoutManager> int getOrientation(@NonNull final T layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else {
            return ((LinearLayoutManager) layoutManager).getOrientation();
        }
    }

    /**
     * @return the span count of {@link StaggeredGridLayoutManager} and {@link GridLayoutManager},
     * otherwise 1 for all other {@link RecyclerView.LayoutManager}
     */
    public static <T extends RecyclerView.LayoutManager> int getSpanCount(@Nullable final T layoutManager) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else {
            return 1;
        }
    }

    /**
     * @return the span index of {@link StaggeredGridLayoutManager.LayoutParams} and {@link GridLayoutManager.LayoutParams},
     * otherwise 1 for all other {@link ViewGroup.LayoutParams}
     */
    public static <T extends ViewGroup.LayoutParams> int getSpanIndex(@Nullable final T layoutParams) {
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return ((StaggeredGridLayoutManager.LayoutParams) layoutParams).getSpanIndex();
        } else if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            return ((GridLayoutManager.LayoutParams) layoutParams).getSpanIndex();
        } else {
            return -1;
        }
    }

    @ItemVisualPosition
    public static String getVisualPosition(@NonNull Context context, @Nullable final View view) {
        if (view == null) {
            return ItemVisualPositions.UNKNOWN;
        }
        final int spanIndex = getSpanIndex(view.getLayoutParams());
        return ItemVisualPositions.parse(context, spanIndex);
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @param span          the span of interest
     * @return the last visible item position
     */
    public static <T extends RecyclerView.LayoutManager> int findLastVisibleItemPosition(@NonNull final T layoutManager,
                                                                                         final int span) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null)[span];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @return the last visible item position among such positions in all spans
     */
    public static <T extends RecyclerView.LayoutManager> int findLastVisibleItemPosition(@NonNull final T layoutManager) {
        final int count = getSpanCount(layoutManager);
        int position;
        int end = RecyclerView.NO_POSITION;
        for (int i = count - 1; i >= 0; i--) {
            position = findLastVisibleItemPosition(layoutManager, i);
            if (end == RecyclerView.NO_POSITION || end < position) {
                end = position;
            }
        }
        return end;
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @param span          the span of interest
     * @return the last visible item position on a specific span
     */
    public static <T extends RecyclerView.LayoutManager> int findLastCompletelyVisibleItemPosition(@NonNull final T layoutManager,
                                                                                                   final int span) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[span];
        } else {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
    }

    /**
     * Find the last completely visible item position across all spans
     *
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @return the largest item position among such positions in all spans
     */
    public static <T extends RecyclerView.LayoutManager> int findLastCompletelyVisibleItemPosition(@NonNull final T layoutManager) {
        final int count = getSpanCount(layoutManager);
        int position;
        int end = RecyclerView.NO_POSITION;
        for (int i = count - 1; i >= 0; i--) {
            position = findLastCompletelyVisibleItemPosition(layoutManager, i);
            if (end == RecyclerView.NO_POSITION || end < position) {
                end = position;
            }
        }
        return end;
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @param span          the span of interest
     * @return the first visible item position on a specific span
     */
    public static <T extends RecyclerView.LayoutManager> int findFirstVisibleItemPosition(@NonNull final T layoutManager,
                                                                                          final int span) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null)[span];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
    }

    /**
     * Find the first visible item position across all spans
     *
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @return the smallest item position among the positions in all spans
     */
    public static <T extends RecyclerView.LayoutManager> int findFirstVisibleItemPosition(@NonNull final T layoutManager) {
        final int count = getSpanCount(layoutManager);
        int position;
        int start = RecyclerView.NO_POSITION;
        for (int i = 0; i < count; i++) {
            position = findFirstVisibleItemPosition(layoutManager, i);
            if (start == RecyclerView.NO_POSITION || start > position) {
                start = position;
            }
        }
        return start;
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @param span          the span of interest
     * @return the first visible item position
     */
    public static <T extends RecyclerView.LayoutManager> int findFirstCompletelyVisibleItemPosition(@NonNull final T layoutManager,
                                                                                                    final int span) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[span];
        } else {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        }
    }

    /**
     * Find the first completely visible item position across all spans
     * <p>
     * Starting on finding the first completely visible item on the first span,
     * iterate through the spans to see if other span has smaller index for
     * first completely visible item.
     *
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @return the smallest item position among the positions in all spans
     */
    public static <T extends RecyclerView.LayoutManager> int findFirstCompletelyVisibleItemPosition(@NonNull final T layoutManager) {
        final int count = getSpanCount(layoutManager);
        int position;
        int start = RecyclerView.NO_POSITION;
        for (int i = 0; i < count; i++) {
            position = findFirstCompletelyVisibleItemPosition(layoutManager, i);
            if (start == RecyclerView.NO_POSITION || start > position) {
                start = position;
            }
        }
        return start;
    }

    /**
     * @param layoutManager the given subclass of {@link RecyclerView.LayoutManager}
     * @param rtl           whether the layout is right to left
     */
    public static <T extends RecyclerView.LayoutManager> void setReverseLayout(@Nullable final T layoutManager,
                                                                               final boolean rtl) {
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) layoutManager).setReverseLayout(rtl);
        } else if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).setReverseLayout(rtl);
        } else {
            // do nothing
        }
    }

    /**
     * Get the visible range of a given {@link RecyclerView}
     *
     * @param recyclerView the {@link RecyclerView}
     * @return the int[] contains the range of interest, [0] contains the start and [1] contains the end
     */
    public static int[] getVisibleRange(@NonNull RecyclerView recyclerView) {
        int[] range = new int[]{RecyclerView.NO_POSITION, RecyclerView.NO_POSITION};
        int start, end;
        final int spanCount = getSpanCount(recyclerView.getLayoutManager());
        for (int span = 0; span < spanCount; span++) {
            start = findFirstVisibleItemPosition(recyclerView.getLayoutManager(), span);
            end = findLastVisibleItemPosition(recyclerView.getLayoutManager(), span);

            // update mDraggingStart and mDraggingEnd if the new bounds are wider than the old one
            if (range[0] == RecyclerView.NO_POSITION) {
                range[0] = start;
            }

            if (start > RecyclerView.NO_POSITION && start < range[0]) {
                range[0] = start;
            }

            if (range[1] == RecyclerView.NO_POSITION) {
                range[1] = end;
            }

            if (end > RecyclerView.NO_POSITION && end > range[1]) {
                range[1] = end;
            }
        }
        return range;
    }
}
