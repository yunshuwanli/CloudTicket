package com.pri.yunshuwanli.cloudticket.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.view.AddSubView;

import java.util.List;

import yswl.com.klibrary.base.BaseRecyclerAdapter;
import yswl.com.klibrary.base.BaseViewHolder;

public class GoodsListAdapter extends BaseRecyclerAdapter<User.SpListBean> {
    public GoodsListAdapter(Context mContext, List<User.SpListBean> mList, int itemLayoutId) {
        super(mContext, mList, itemLayoutId);
    }

    public interface OnClickListener {
        void onClick(User.SpListBean info);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public void onBind(BaseViewHolder holder, final User.SpListBean data, final int position) {
        TextView name = holder.findById(R.id.name);
        TextView price = holder.findById(R.id.price);
//        AddSubView addSubView = holder.findById(R.id.count);
        name.setText(data.getSpmc());
        if(data.getSpdj() == 0){
            price.setText("");
        }else {
            price.setText(String.valueOf(data.getSpdj()));
        }

//        addSubView.setItemDataTag(data);
//        addSubView.setOnCalculateListener(new AddSubView.OnCalculateListener() {
//            @Override
//            public void onCalculate() {
////                setAmount();
//            }
//        });
//        if (data.amount == 0) {
//            addSubView.sub.setVisibility(View.INVISIBLE);
//            addSubView.count.setVisibility(View.INVISIBLE);
//            addSubView.count.setText("0");
//        } else {
//            addSubView.sub.setVisibility(View.VISIBLE);
//            addSubView.count.setVisibility(View.VISIBLE);
//            addSubView.count.setText(data.amount + "");
//        }
        holder.setOnclickListener(R.id.ll_goods_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onClick(data);
                }
            }
        });

    }
}