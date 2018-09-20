package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.pri.yunshuwanli.cloudticket.R;

import yswl.com.klibrary.base.MActivity;

public class BillingActivity extends MActivity {
    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, BillingActivity.class);
        context.startActivity(intent);
    }

    EditText mEditText_CarNo;
    EditText mEditText_Price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        initUI();

    }

    private void initUI() {
        mEditText_CarNo = findViewById(R.id.car_no);
        mEditText_Price = findViewById(R.id.price);


        mEditText_CarNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("字符变换后", "afterTextChanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("字符变换前", s + "-" + start + "-" + count + "-" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("字符变换中", s + "-" + "-" + start + "-" + before + "-" + count);
            }
        });
    }


}
