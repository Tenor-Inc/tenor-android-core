package com.tenor.android.core.rvwidget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tenor.android.core.measurable.MeasurableViewHolder;
import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.view.IBaseView;

/**
 * The abstract recycler view view holder class that all recycler view view holder classes should extend from
 *
 * @param <CTX> the referenced context
 */
public class StaggeredGridLayoutItemViewHolder<CTX extends IBaseView> extends MeasurableViewHolder<CTX> {

    /**
     * Interface used to communicate info between adapter and view holder
     */
    public interface OnClickListener {
        /**
         * Send back the current select position to adapter
         */
        void onClick(int position);
    }

    public StaggeredGridLayoutItemViewHolder(@NonNull View itemView, @NonNull CTX context) {
        super(itemView, context);
    }

    /**
     * Get context, use hasWeakRef() to check its existence first
     */
    @Nullable
    public Context getContext() {
        if (getRef() instanceof Activity) {
            return (Activity) getRef();
        }

        if (getRef() instanceof Fragment) {
            return ((Fragment) getRef()).getActivity();
        }
        //noinspection ConstantConditions
        return hasRef() ? getRef().getContext() : null;
    }

    public boolean hasContext() {
        return hasRef() && getContext() != null;
    }

    /**
     * Set full span with {@link ViewGroup.LayoutParams#WRAP_CONTENT} height
     */
    public void setFullWidthWithHeight() {
        setFullWidthWithHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setFullHeightWithWidth() {
        setFullHeightWithWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * @param height height in px
     */
    public void setFullWidthWithHeightInPixel(int height) {
        StaggeredGridLayoutManager.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        params.setFullSpan(true);
        itemView.setLayoutParams(params);
    }

    /**
     * @param height height in dp
     */
    public void setFullWidthWithHeight(int height) {
        if (!hasContext()) {
            return;
        }
        final int h = height >= 0 ? AbstractUIUtils.dpToPx(getContext(), height) : height;
        setFullWidthWithHeightInPixel(h);
    }

    /**
     * @param width width in dp
     */
    public void setFullHeightWithWidth(int width) {
        if (!hasContext()) {
            return;
        }
        final int w = width >= 0 ? AbstractUIUtils.dpToPx(getContext(), width) : width;
        StaggeredGridLayoutManager.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setFullSpan(true);
        itemView.setLayoutParams(params);
    }

    /**
     * @param height height in dp
     */
    public void setHeight(int height) {
        if (!hasContext()) {
            return;
        }
        setHeightInPixel(AbstractUIUtils.dpToPx(getContext(), height));
    }

    /**
     * @param height height in px
     */
    public void setHeightInPixel(int height) {
        StaggeredGridLayoutManager.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        height);

        itemView.setLayoutParams(params);
    }

    /**
     * @param width width in px
     */
    public void setWidthInPixel(int width) {
        StaggeredGridLayoutManager.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(
                        width,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        itemView.setLayoutParams(params);
    }

    public void setFullSpan(boolean fullSpan) {
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        params.setFullSpan(fullSpan);
        itemView.setLayoutParams(params);
    }

    /**
     * @param width  width in px
     * @param height height in px
     */
    public void setParams(int width, int height) {
        StaggeredGridLayoutManager.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(
                        width,
                        height);

        itemView.setLayoutParams(params);
    }
}
