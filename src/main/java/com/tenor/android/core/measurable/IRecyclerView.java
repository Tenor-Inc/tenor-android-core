package com.tenor.android.core.measurable;


import android.graphics.Canvas;
import android.graphics.Rect;
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
 * Interface of {@link RecyclerView} on support library v25.3.1
 */
public interface IRecyclerView {
    
    void addFocusables(ArrayList<View> views, int direction, int focusableMode);

    void addItemDecoration(RecyclerView.ItemDecoration decor);

    void addItemDecoration(RecyclerView.ItemDecoration decor, int index);

    void addOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener);

    void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener);

    void addOnScrollListener(RecyclerView.OnScrollListener listener);

    void clearOnChildAttachStateChangeListeners();

    void clearOnScrollListeners();

    int computeHorizontalScrollExtent();

    int computeHorizontalScrollOffset();

    int computeHorizontalScrollRange();

    int computeVerticalScrollExtent();

    int computeVerticalScrollOffset();

    int computeVerticalScrollRange();

    boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed);

    boolean dispatchNestedPreFling(float velocityX, float velocityY);

    boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow);

    boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow);

    void draw(Canvas c);

    boolean drawChild(Canvas canvas, View child, long drawingTime);

    View findChildViewUnder(float x, float y);

    View findContainingItemView(View view);

    RecyclerView.ViewHolder findContainingViewHolder(View view);

    RecyclerView.ViewHolder findViewHolderForAdapterPosition(int position);

    RecyclerView.ViewHolder findViewHolderForItemId(long id);

    RecyclerView.ViewHolder findViewHolderForLayoutPosition(int position);

    RecyclerView.ViewHolder findViewHolderForPosition(int position);

    boolean fling(int velocityX, int velocityY);

    View focusSearch(View focused, int direction);

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

    void getDecoratedBoundsWithMargins(View view, Rect outBounds);

    RecyclerView.ItemAnimator getItemAnimator();

    RecyclerView.LayoutManager getLayoutManager();

    int getMaxFlingVelocity();

    int getMinFlingVelocity();

    RecyclerView.OnFlingListener getOnFlingListener();

    boolean getPreserveFocusAfterLayout();

    RecyclerView.RecycledViewPool getRecycledViewPool();

    int getScrollState();

    boolean hasFixedSize();

    boolean hasNestedScrollingParent();

    boolean hasPendingAdapterUpdates();

    void invalidateItemDecorations();

    boolean isAnimating();

    boolean isAttachedToWindow();

    boolean isComputingLayout();

    boolean isLayoutFrozen();

    boolean isNestedScrollingEnabled();

    void offsetChildrenHorizontal(int dx);

    void offsetChildrenVertical(int dy);

    void onChildAttachedToWindow(View child);

    void onChildDetachedFromWindow(View child);

    void onDraw(Canvas c);

    boolean onGenericMotionEvent(MotionEvent event);

    boolean onInterceptTouchEvent(MotionEvent e);

    void onScrollStateChanged(int state);

    void onScrolled(int dx, int dy);

    boolean onTouchEvent(MotionEvent e);

    void removeItemDecoration(RecyclerView.ItemDecoration decor);

    void removeOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener);

    void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener);

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

    void setChildDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback childDrawingOrderCallback);

    void setClipToPadding(boolean clipToPadding);

    void setHasFixedSize(boolean hasFixedSize);

    void setItemAnimator(RecyclerView.ItemAnimator animator);

    void setItemViewCacheSize(int size);

    void setLayoutFrozen(boolean frozen);

    void setLayoutManager(RecyclerView.LayoutManager layout);

    void setNestedScrollingEnabled(boolean enabled);

    void setOnFlingListener(RecyclerView.OnFlingListener onFlingListener);

    void setOnScrollListener(RecyclerView.OnScrollListener listener);

    void setPreserveFocusAfterLayout(boolean preserveFocusAfterLayout);

    void setRecycledViewPool(RecyclerView.RecycledViewPool pool);

    void setRecyclerListener(RecyclerView.RecyclerListener listener);

    void setScrollingTouchSlop(int slopConstant);

    void setViewCacheExtension(RecyclerView.ViewCacheExtension extension);

    void smoothScrollBy(int dx, int dy);

    void smoothScrollBy(int dx, int dy, Interpolator interpolator);

    void smoothScrollToPosition(int position);

    boolean startNestedScroll(int axes);

    void stopNestedScroll();

    void stopScroll();

    void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews);

    /*
     * To be added on support library v26.0.0
     */
    // boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow, int type);

    // boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow, int type);

    // RecyclerView.ItemDecoration getItemDecorationAt(int index);

    // boolean hasNestedScrollingParent(int type);

    // boolean startNestedScroll(int axes, int type);

    // void stopNestedScroll(int type);
}

