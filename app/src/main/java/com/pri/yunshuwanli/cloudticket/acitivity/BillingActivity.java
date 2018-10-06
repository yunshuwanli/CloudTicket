package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.utils.CarKeyboardUtil;

import yswl.com.klibrary.base.MActivity;

public class BillingActivity extends MActivity implements View.OnTouchListener {
    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, BillingActivity.class);
        context.startActivity(intent);
    }

    EditText mEditText_CarNo;
    EditText mEditText_Price;
    CarKeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        initUI();

    }

    private void initUI() {
        mEditText_CarNo = findViewById(R.id.car_no);
        mEditText_Price = findViewById(R.id.price);
        keyboardUtil = new CarKeyboardUtil(this, mEditText_CarNo);
        mEditText_CarNo.setOnTouchListener(this);
        mEditText_Price.setOnTouchListener(this);
        mEditText_CarNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains("港") || text.contains("澳") || text.contains("学") ){
                    mEditText_CarNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                }else{
                    mEditText_CarNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.car_no){
            keyboardUtil.hideSystemKeyBroad();
            keyboardUtil.hideSoftInputMethod();
            if (!keyboardUtil.isShow())
                keyboardUtil.showKeyboard();
        }else {
            if (keyboardUtil.isShow())
                keyboardUtil.hideKeyboard();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyboardUtil.isShow()){
            keyboardUtil.hideKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
