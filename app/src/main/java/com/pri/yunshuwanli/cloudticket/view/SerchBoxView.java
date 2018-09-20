package com.pri.yunshuwanli.cloudticket.view;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.pri.yunshuwanli.cloudticket.R;

public class SerchBoxView extends RelativeLayout {
	private Context mContext;
	private EditText mEditText;
	public SerchBoxView(Context context) {
		super(context);
		init(context);
	}
	public SerchBoxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	public SerchBoxView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
    private void init(Context context){
    	mContext = context;
    	View v = LayoutInflater.from(mContext).inflate(R.layout.view_common_serchbox, this,false);
    	mEditText = (EditText)v.findViewById(R.id.comm_serchbox_edittext);
    	this.setFocusable(true);
    	this.setFocusableInTouchMode(true);
    	this.addView(v);
    }
    public void addTextChangedListener(TextWatcher txtWatcher){
    	mEditText.addTextChangedListener(txtWatcher);
    }

	public EditText getEditText() {
		return mEditText;
	}
}