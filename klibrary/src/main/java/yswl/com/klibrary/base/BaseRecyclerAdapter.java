package yswl.com.klibrary.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerAdapter 封装类
 * 可以添加Header，Footer，EmptyView占位符
 * 可以设置item的点击事件，以及EmptyView的点击事件
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {
    public Context mContext;
    public List<T> mList;
    public LayoutInflater inflater;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_EMPTY = 2;
    public static final int TYPE_FOOTER = 3;
    public View mEmptyView;
    public View mHeaderView;
    public View mFooterView;
    public int itemLayoutId;

    @Override
    public void onClick(View v) {
        if (listener != null) {
            Integer pos = (Integer) v.getTag();
            if (pos != null && pos != -1) {
                listener.onItemClick((Integer) v.getTag());
                return;
            }
            if (mEmptyView != null && emptyViewClickListener != null) {
                emptyViewClickListener.onEmptyViewClick();
            }
        }
    }

    /**
     * item 点击监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener = null;

    /**
     * 为item 添加点击监听
     *
     * @param listener BaseRecyclerAdapter.OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * emptyView 点击监听接口
     */
    public interface OnEmptyViewClickListener {
        void onEmptyViewClick();
    }

    private OnEmptyViewClickListener emptyViewClickListener = null;

    /**
     * 为emptyView 添加点击监听
     *
     * @param listener
     */
    public void setOnEmptyViewClickListener(OnEmptyViewClickListener listener) {
        this.emptyViewClickListener = listener;
    }


    public BaseRecyclerAdapter(Context mContext, List<T> mList, int itemLayoutId) {
        this.mContext = mContext;
        this.mList = mList;
        this.itemLayoutId = itemLayoutId;
        inflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerAdapter(Context mContext, int itemLayoutId) {
        this(mContext, null, itemLayoutId);
    }

    public void addList(List<T> list) {
        if (mList != null) {
            this.mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }
    public void addItem(T e) {
        if (mList != null) {
            this.mList.add(0,e);
//            this.notifyItemChanged(mList.size()-1);
        }
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_EMPTY:
                itemView = mEmptyView;
                break;
            case TYPE_HEADER:
                itemView = mHeaderView;
                break;
            case TYPE_FOOTER:
                itemView = mFooterView;
                break;
            case TYPE_NORMAL:
                itemView = LayoutInflater.from(mContext).inflate(itemLayoutId, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(mContext).inflate(itemLayoutId, parent, false);
                break;
        }
        if (itemView != null)
            itemView.setOnClickListener(this);
        return BaseViewHolder.getHolder(itemView, viewType);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_EMPTY || getItemViewType(position) == TYPE_FOOTER) {
            holder.itemView.setTag(-1);
            return;
        }
        int pos = getRealPosition(holder);
        holder.itemView.setTag(pos);
        onBind(holder, mList.get(pos), pos);
    }

    public abstract void onBind(BaseViewHolder holder, T t, int position);

    @Override
    public int getItemViewType(int position) {//判断顺序固定
        if (position == 0 && mHeaderView != null) return TYPE_HEADER;
        if (mEmptyView != null) {
            if (mList == null || mList.size() == 0) {
                return TYPE_EMPTY;
            }
        }
        if (mHeaderView == null && mFooterView == null) return TYPE_NORMAL;
        if (mFooterView != null && position == getItemCount() - 1) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    public int getItemCountOnlyData() {
        return mList != null && mList.size() > 0 ? mList.size() : 0;
    }

    @Override
    public int getItemCount() {
        int itemCount = mList != null && mList.size() > 0 ? mList.size() : 0;
        if (null != mEmptyView && itemCount == 0) {
            itemCount++;
        }
        if (null != mHeaderView) {
            itemCount++;
        }
        if (null != mFooterView) {
            itemCount++;
        }
        return itemCount;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }
}
