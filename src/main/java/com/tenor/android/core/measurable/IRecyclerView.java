package com.tenor.android.core.measurable;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;

import java.util.ArrayList;


/**
 * Interface of {@link RecyclerView} on support library
 *
 * @version v26.0.1
 * @since v22.0.0
 */
public interface IRecyclerView {

    void addFocusables(ArrayList<View> views, int direction, int focusableMode);

    void addItemDecoration(RecyclerView.ItemDecoration decor);

    void addItemDecoration(RecyclerView.ItemDecoration decor, int index);

    void addOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener);

    void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener);

    /**
     * @since v22.1.0
     */
    void addOnScrollListener(RecyclerView.OnScrollListener listener);

    /**
     * @since v22.2.0
     */
    void clearOnChildAttachStateChangeListeners();

    /**
     * @since v22.1.0
     */
    void clearOnScrollListeners();

    /**
     * @since v22.1.0
     */
    int computeHorizontalScrollExtent();

    /**
     * @since v22.1.0
     */
    int computeHorizontalScrollOffset();

    /**
     * @since v22.1.0
     */
    int computeHorizontalScrollRange();

    /**
     * @since v22.1.0
     */
    int computeVerticalScrollExtent();

    /**
     * @since v22.1.0
     */
    int computeVerticalScrollOffset();

    /**
     * @since v22.1.0
     */
    int computeVerticalScrollRange();

    boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);

    boolean dispatchNestedPreFling(float velocityX, float velocityY);

    boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow);

    /**
     * @since v26.0.0
     */
    boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type);

    /**
     * @since v26.0.0
     */
    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type);

    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

    void draw(Canvas c);

    /**
     * @since v22.2.0
     */
    boolean drawChild(Canvas canvas, View child, long drawingTime);

    View findChildViewUnder(float x, float y);

    /**
     * @since v23.2.0
     */
    View findContainingItemView(View view);

    @Nullable
    RecyclerView.ViewHolder findContainingViewHolder(View view);

    @Nullable
    RecyclerView.ViewHolder findViewHolderForAdapterPosition(int position);

    @Nullable
    RecyclerView.ViewHolder findViewHolderForItemId(long id);

    @Nullable
    RecyclerView.ViewHolder findViewHolderForLayoutPosition(int position);

    @Nullable
    RecyclerView.ViewHolder findViewHolderForPosition(int position);

    boolean fling(int velocityX, int velocityY);

    @Nullable
    View focusSearch(View focused, int direction);

    @NonNull
    ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs);

    RecyclerView.Adapter getAdapter();

    int getBaseline();

    int getChildAdapterPosition(View child);

    long getChildItemId(View child);

    int getChildLayoutPosition(View child);

    int getChildPosition(View child);

    RecyclerView.ViewHolder getChildViewHolder(View child);

    boolean getClipToPadding();

    RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate();

    /**
     * @since v25.0.0
     */
    void getDecoratedBoundsWithMargins(View view, Rect outBounds);

    RecyclerView.ItemAnimator getItemAnimator();

    /**
     * @since v26.0.0
     */
    RecyclerView.ItemDecoration getItemDecorationAt(int index);

    RecyclerView.LayoutManager getLayoutManager();

    /**
     * @since v22.2.0
     */
    int getMaxFlingVelocity();

    /**
     * @since v22.2.0
     */
    int getMinFlingVelocity();

    /**
     * @since v24.2.0
     */
    RecyclerView.OnFlingListener getOnFlingListener();

    /**
     * @since v24.0.0
     */
    boolean getPreserveFocusAfterLayout();

    RecyclerView.RecycledViewPool getRecycledViewPool();

    int getScrollState();

    boolean hasFixedSize();

    boolean hasNestedScrollingParent();

    /**
     * @since v26.0.0
     */
    boolean hasNestedScrollingParent(int type);

    /**
     * @since v22.1.0
     */
    boolean hasPendingAdapterUpdates();

    void invalidateItemDecorations();

    /**
     * @since v22.2.0
     */
    boolean isAnimating();

    boolean isAttachedToWindow();

    /**
     * @since v22.2.1
     */
    boolean isComputingLayout();

    /**
     * @since v23.0.0
     */
    boolean isLayoutFrozen();

    boolean isNestedScrollingEnabled();

    void offsetChildrenHorizontal(int dx);

    void offsetChildrenVertical(int dy);

    void onChildAttachedToWindow(View child);

    void onChildDetachedFromWindow(View child);

    void onDraw(Canvas c);

    boolean onGenericMotionEvent(MotionEvent event);

    boolean onInterceptTouchEvent(MotionEvent e);

    /**
     * @since v22.1.0
     */
    void onScrollStateChanged(int state);

    /**
     * @since v22.1.0
     */
    void onScrolled(int dx, int dy);

    boolean onTouchEvent(MotionEvent e);

    void removeItemDecoration(RecyclerView.ItemDecoration decor);

    /**
     * @since v22.2.0
     */
    void removeOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener);

    void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener);

    /**
     * @since v22.1.0
     */
    void removeOnScrollListener(RecyclerView.OnScrollListener listener);

    void requestChildFocus(View child, View focused);

    boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate);

    void requestDisallowInterceptTouchEvent(boolean disallowIntercept);

    void requestLayout();

    void scrollBy(int x, int y);

    void scrollTo(int x, int y);

    void scrollToPosition(int position);

    void sendAccessibilityEventUnchecked(AccessibilityEvent event);

    void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate);

    void setAdapter(RecyclerView.Adapter adapter);

    /**
     * @since v22.2.0
     */
    void setChildDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback childDrawingOrderCallback);

    void setClipToPadding(boolean clipToPadding);

    void setHasFixedSize(boolean hasFixedSize);

    void setItemAnimator(RecyclerView.ItemAnimator animator);

    void setItemViewCacheSize(int size);

    /**
     * @since v23.0.0
     */
    void setLayoutFrozen(boolean frozen);

    void setLayoutManager(RecyclerView.LayoutManager layout);

    void setNestedScrollingEnabled(boolean enabled);

    /**
     * @since v24.2.0
     */
    void setOnFlingListener(RecyclerView.OnFlingListener onFlingListener);

    void setOnScrollListener(RecyclerView.OnScrollListener listener);

    /**
     * @since v24.0.0
     */
    void setPreserveFocusAfterLayout(boolean preserveFocusAfterLayout);

    void setRecycledViewPool(RecyclerView.RecycledViewPool pool);

    void setRecyclerListener(RecyclerView.RecyclerListener listener);

    void setScrollingTouchSlop(int slopConstant);

    void setViewCacheExtension(RecyclerView.ViewCacheExtension extension);

    void smoothScrollBy(int dx, int dy);

    /**
     * @since v25.1.0
     */
    void smoothScrollBy(int dx, int dy, Interpolator interpolator);

    void smoothScrollToPosition(int position);

    boolean startNestedScroll(int axes);

    /**
     * @since v26.0.0
     */
    boolean startNestedScroll(int axes, int type);

    void stopNestedScroll();

    /**
     * @since v26.0.0
     */
    void stopNestedScroll(int type);

    void stopScroll();

    void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews);
}

