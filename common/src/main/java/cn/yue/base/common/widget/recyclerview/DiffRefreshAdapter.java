package cn.yue.base.common.widget.recyclerview;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Created by yue on 2022/1/26
 */

public abstract class DiffRefreshAdapter<T> extends CommonAdapter<T> {

    public DiffRefreshAdapter(Context context) {
        super(context);
    }

    public DiffRefreshAdapter(Context context, List<T> list) {
        super(context, list);
    }

    private DiffCallBack mDiffCallBack = new DiffCallBack();

    public void setDataCollection(List<T> mData) {
        List<T> newList = mData;
        if (newList == null) {
            newList = new ArrayList<>();
        }
        if (getList().isEmpty() || newList.isEmpty()) {
            getList().clear();
            getList().addAll(newList);
            notifyDataSetChangedReally();
        } else {
            mDiffCallBack.setNewList(newList);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mDiffCallBack, false);
            int oldListSize = mDiffCallBack.getOldListSize();
            getList().clear();
            getList().addAll(newList);
            diffResult.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onInserted(int position, int count) {
                    notifyItemInsertedReally(position, count);
                }

                @Override
                public void onRemoved(int position, int count) {
                    if (oldListSize == count) {
                        notifyDataSetChangedReally();
                    } else {
                        notifyItemRemovedReally(position, count);
                    }
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMovedReally(fromPosition, toPosition);
                }

                @Override
                public void onChanged(int position, int count, @Nullable Object payload) {
                    notifyItemChangedReally(position, count);
                }
            });
        }
    }

    private class DiffCallBack extends DiffUtil.Callback {

        private List<T> newList;

        @Override
        public int getOldListSize() {
            return getList().size();
        }

        @Override
        public int getNewListSize() {
            if (newList == null || newList.isEmpty()) {
                return 0;
            }
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (getList().size() > oldItemPosition && newList != null && newList.size() > newItemPosition) {
                return DiffRefreshAdapter.this.areItemsTheSame(getList().get(oldItemPosition), newList.get(newItemPosition));
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            if (getList().size() > oldItemPosition && newList != null && newList.size() > newItemPosition) {
                return DiffRefreshAdapter.this.areContentsTheSame(getList().get(oldItemPosition), newList.get(newItemPosition));
            }
            return true;
        }

        void setNewList(List<T> newList) {
            this.newList = newList;
        }
    }

    protected abstract boolean areItemsTheSame(T item1, T item2);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Override
    public void setList(List<T> list) {
        setDataCollection(list);
    }
}
