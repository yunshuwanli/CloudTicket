package com.pri.yunshuwanli.cloudticket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.acitivity.MainActivity;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.utils.BitmapUtils;
import com.pri.yunshuwanli.cloudticket.utils.PrinterTester;

import java.util.List;

import yswl.com.klibrary.base.BaseRecyclerAdapter;
import yswl.com.klibrary.base.BaseViewHolder;

public class RecordListAdapter extends BaseRecyclerAdapter<OrderInfo> {
    public interface OnClickListener {
        void onClick(OrderInfo info);
    }
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public RecordListAdapter(Context mContext, List<OrderInfo> mList, int itemLayoutId) {
            super(mContext, mList, itemLayoutId);
        }

        @Override
        public void onBind(BaseViewHolder holder, final OrderInfo data, final int position) {
           TextView dataTime = holder.findById(R.id.data_time);
           TextView carNo = holder.findById(R.id.car_no);
           TextView price = holder.findById(R.id.pay);
           TextView payType = holder.findById(R.id.pay_type);
           Button print = holder.findById(R.id.print);
           TextView printStatue = holder.findById(R.id.print_statue);

            dataTime.setText(data.getOrderDate());
            carNo.setText(data.getCarNo());
            price.setText(data.getTotalAmount()+"");
            payType.setText(data.getPayType());
            printStatue.setText(data.getprintStatue());
            if(onClickListener !=null){
                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onClick(data);
                    }
                });
            }


        }
    }