package yswl.com.klibrary.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public View itemView;
    SparseArray<View> views;

    public BaseViewHolder(View itemView, int viewType) {
        super(itemView);
        this.itemView = itemView;
        if (viewType==BaseRecyclerAdapter.TYPE_EMPTY||viewType==BaseRecyclerAdapter.TYPE_HEADER||viewType==BaseRecyclerAdapter.TYPE_FOOTER){
            return;
        }
        views = new SparseArray<>();
    }

    public static <T extends BaseViewHolder> T getHolder(View itemview, int viewType){

        return (T) new BaseViewHolder(itemview,viewType);
    }

    public <T extends View> T findById(int viewId){
        View childreView = views.get(viewId);
        if (childreView == null){
            childreView = itemView.findViewById(viewId);
            views.put(viewId,childreView);
        }
        return (T) childreView;
    }

    public BaseViewHolder setOnclickListener(int viewId,View.OnClickListener listener){
        View view = findById(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}

