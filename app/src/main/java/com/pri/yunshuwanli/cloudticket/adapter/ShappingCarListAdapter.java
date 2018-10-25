package com.pri.yunshuwanli.cloudticket.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.view.AddSubView;

import java.util.List;

import yswl.com.klibrary.base.BaseRecyclerAdapter;
import yswl.com.klibrary.base.BaseViewHolder;

public class ShappingCarListAdapter extends BaseRecyclerAdapter<User.SpListBean> {
    public ShappingCarListAdapter(Context mContext, List<User.SpListBean> mList, int itemLayoutId) {
        super(mContext, mList, itemLayoutId);
    }

    public interface OnClickListener {
        void onClick(User.SpListBean info, int position);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public void onBind(BaseViewHolder holder, final User.SpListBean data, final int position) {
        TextView name = holder.findById(R.id.name);
        TextView price = holder.findById(R.id.price);
        Button delete = holder.findById(R.id.delete);
        price.setText(String.valueOf(data.getReal_total()));
        name.setText(data.getSpmc());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(data, position);
                }
            }
        });


    }
}