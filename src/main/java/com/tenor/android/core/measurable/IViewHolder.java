package com.tenor.android.core.measurable;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Interface of a {@link android.support.v7.widget.RecyclerView.ViewHolder}
 */
public interface IViewHolder {

    /**
     * Returns the position of the ViewHolder in terms of the latest layout pass.
     * <p>
     * This position is mostly used by RecyclerView components to be consistent while
     * RecyclerView lazily processes adapter updates.
     * <p>
     * For performance and animation reasons, RecyclerView batches all adapter updates until the
     * next layout pass. This may cause mismatches between the Adapter position of the item and
     * the position it had in the latest layout calculations.
     * <p>
     * LayoutManagers should always call this method while doing calculations based on item
     * positions. All methods in {@link RecyclerView.LayoutManager}, {@link RecyclerView.State},
     * {@link RecyclerView.Recycler} that receive a position expect it to be the layout position
     * of the item.
     * <p>
     * If LayoutManager needs to call an external method that requires the adapter position of
     * the item, it can use {@link #getAdapterPosition()} or
     * {@link RecyclerView.Recycler#convertPreLayoutPositionToPostLayout(int)}.
     *
     * @return Returns the adapter position of the ViewHolder in the latest layout pass.
     * @see #getAdapterPosition()
     */
    int getLayoutPosition();

    /**
     * Returns the Adapter position of the item represented by this ViewHolder.
     * <p>
     * Note that this might be different than the {@link #getLayoutPosition()} if there are
     * pending adapter updates but a new layout pass has not happened yet.
     * <p>
     * RecyclerView does not handle any adapter updates until the next layout traversal. This
     * may create temporary inconsistencies between what user sees on the screen and what
     * adapter contents have. This inconsistency is not important since it will be less than
     * 16ms but it might be a problem if you want to use ViewHolder position to access the
     * adapter. Sometimes, you may need to get the exact adapter position to do
     * some actions in response to user events. In that case, you should use this method which
     * will calculate the Adapter position of the ViewHolder.
     * <p>
     * Note that if you've called {@link RecyclerView.Adapter#notifyDataSetChanged()}, until the
     * next layout pass, the return value of this method will be {@link RecyclerView#NO_POSITION}.
     *
     * @return The adapter position of the item if it still exists in the adapter.
     * {@link RecyclerView#NO_POSITION} if item has been removed from the adapter,
     * {@link RecyclerView.Adapter#notifyDataSetChanged()} has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    int getAdapterPosition();

    /**
     * When LayoutManager supports animations, RecyclerView tracks 3 positions for ViewHolders
     * to perform animations.
     * <p>
     * If a ViewHolder was laid out in the previous onLayout call, old position will keep its
     * adapter index in the previous layout.
     *
     * @return The previous adapter index of the Item represented by this ViewHolder or
     * {@link RecyclerView#NO_POSITION} if old position does not exists or cleared (pre-layout is
     * complete).
     */
    int getOldPosition();

    /**
     * Returns The itemId represented by this ViewHolder.
     *
     * @return The item's id if adapter has stable ids, {@link RecyclerView#NO_ID}
     * otherwise
     */
    long getItemId();

    /**
     * @return The view type of this ViewHolder.
     */
    int getItemViewType();

    @NonNull
    String toString();
}
