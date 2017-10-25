package com.tenor.android.core.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.tenor.android.core.measurable.IRecyclerView;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.weakref.IWeakRefHandler;
import com.tenor.android.core.weakref.IWeakRefObject;
import com.tenor.android.core.weakref.WeakRefCurrentHandler;

import java.lang.ref.WeakReference;

/**
 * {@link WeakRefRecyclerView} is a subclass of {@link RecyclerView} for search suggestions
 * <p>
 * "AppCompatTextView" under "com.android.support:appcompat-v7" is not required, yet highly recommended
 * to ensure this widget is working properly on API 20-
 */
public class WeakRefRecyclerView extends RecyclerView implements IRecyclerView, IWeakRefObject<Context>,
        IWeakRefHandler<WeakRefRecyclerView> {

    private final WeakReference<Context> mWeakRef;
    private final WeakRefCurrentHandler<WeakRefRecyclerView> mHandler;

    public WeakRefRecyclerView(Context context) {
        this(context, null);
    }

    public WeakRefRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeakRefRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWeakRef = new WeakReference<>(context);
        mHandler = new WeakRefCurrentHandler<>(this);
    }

    @NonNull
    public WeakRefCurrentHandler<WeakRefRecyclerView> getHandler() {
        return mHandler;
    }

    @Nullable
    @Override
    public Context getRef() {
        return mWeakRef.get();
    }

    @NonNull
    @Override
    public WeakReference<Context> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }
}
