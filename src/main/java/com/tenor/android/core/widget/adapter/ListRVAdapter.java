package com.tenor.android.core.widget.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.tenor.android.core.view.IBaseView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

/**
 * The {@link RecyclerView} implementation of {@link android.widget.ListAdapter}
 *
 * @param <CTX> the referenced context
 * @param <T>   the list item type
 * @param <VH>  the view holder type extending from {@link RecyclerView.ViewHolder}
 */
public abstract class ListRVAdapter<CTX extends IBaseView, T, VH extends RecyclerView.ViewHolder>
        extends AbstractRVAdapter<CTX, VH> {

    private final List<T> mList;

    public ListRVAdapter(@Nullable final CTX ctx) {
        super(ctx);
        mList = new ArrayList<>();
    }

    public ListRVAdapter(@NonNull final WeakReference<CTX> weakRef) {
        super(weakRef);
        mList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    @CallSuper
    @NonNull
    public List<T> getList() {
        return mList;
    }

    public void insert(@Nullable T item, boolean isAppend) {
        if (item == null) {
            return;
        }

        List<T> list = new ArrayList<>();
        list.add(item);
        insert(list, isAppend);
    }

    public void insert(@NonNull List<T> list) {
        insert(list, false);
    }

    public abstract void insert(@NonNull List<T> list, boolean isAppend);

    public void insert(int position, @NonNull List<T> list) {
        getList().addAll(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void insert(int position, @NonNull T item) {
        getList().add(position, item);
        notifyItemInserted(position);
    }

    public void clearList() {
        getList().clear();
    }

    public synchronized void threadSafeRemove(@NonNull final IThreadSafeConditions<T> conditions) {

        if (conditions == null) {
            throw new IllegalArgumentException("conditions cannot be null");
        }

        /*
         * Remove everything except PivotsRV
         *
         * Use iterator to avoid ConcurrentModificationException
         */
        ListIterator<T> iterator = getList().listIterator();
        final Stack<Integer> positions = new Stack<>();
        int position;
        T item;
        while (iterator.hasNext()) {
            position = iterator.nextIndex();
            item = iterator.next();
            if (conditions.removeIf(item)) {
                iterator.remove();
                positions.push(position);
            }
        }
        conditions.onItemsRemoved(positions);
    }

    public interface IThreadSafeConditions<T> {
        /**
         * Condition for removing <T>
         */
        boolean removeIf(T item);

        void onItemsRemoved(Stack<Integer> positions);
    }
}
