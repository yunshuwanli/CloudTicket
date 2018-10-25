package com.pri.yunshuwanli.cloudticket.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.User;

public class AddSubView extends LinearLayout {

    public ImageButton add, sub;
    public TextView count;

    public AddSubView(Context context) {
        super(context);
        init(context);
    }

    public AddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddSubView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_sub_view, null);
        add = (ImageButton) view.findViewById(R.id.add);
        sub = (ImageButton) view.findViewById(R.id.sub);
        count = (TextView) view.findViewById(R.id.count);
        add.setTag(R.id.count, count);
        sub.setTag(R.id.count, count);
        sub.setVisibility(View.INVISIBLE);
        count.setVisibility(View.INVISIBLE);
        setAddOnclickListener(null);
        setSubOnclickListener(null);
        this.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


    public void setAddOnclickListener(OnClickListener l) {
        if (l != null)
            add.setOnClickListener(l);
        else
            add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView count = (TextView) v.getTag(R.id.count);
                    String countStr = count.getText().toString();
                    int countInt = Integer.valueOf(countStr);
                    countInt++;
                    if (countInt > 0) {
                        sub.setVisibility(View.VISIBLE);
                        count.setVisibility(View.VISIBLE);
                    }
                    count.setText(countInt + "");
                    updateItemData(countInt);
                }
            });
    }


    public void setSubOnclickListener(OnClickListener l) {
        if (l != null)
            sub.setOnClickListener(l);
        else
            sub.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView count = (TextView) v.getTag(R.id.count);
                    String countStr = count.getText().toString();
                    int countInt = Integer.valueOf(countStr);
                    countInt--;
                    if (countInt <= 0) {
                        countInt = 0;
                        sub.setVisibility(View.INVISIBLE);
                        count.setVisibility(View.INVISIBLE);
                    }
                    count.setText(countInt + "");
                    updateItemData(countInt);
                }
            });
    }

    OnCalculateListener mOnCalculateListener;

    public void setOnCalculateListener(OnCalculateListener l) {
        mOnCalculateListener = l;
    }

    public interface OnCalculateListener {
        void onCalculate();
    }

    private void updateItemData(int count) {
        getItemDataTag().amount = count;
        if (mOnCalculateListener != null) {
            mOnCalculateListener.onCalculate();
        }
    }

    public void setItemDataTag(User.SpListBean p) {
        count.setTag(p);
    }

    public User.SpListBean getItemDataTag() {
        return (User.SpListBean) count.getTag();
    }

}
