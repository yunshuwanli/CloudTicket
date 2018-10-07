package com.pri.yunshuwanli.cloudticket.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.pri.yunshuwanli.cloudticket.R;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.util.ToastUtil;

public class UIDSettingActivity extends MActivity {

    public static void JumpAct(Activity context,int requestId) {
        Intent intent = new Intent(context, UIDSettingActivity.class);
        context.startActivityForResult(intent,requestId);
    }
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidsetting);

        editText =findViewById(R.id.et_uid);
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String uid = editText.getText().toString().trim();
               if(TextUtils.isEmpty(uid)){
                   ToastUtil.showToast("请输入设备ID");
               }else {
                   Intent intent = new Intent();
                   intent.putExtra("UID",uid);
                   setResult(Activity.RESULT_OK,intent);
                   finish();

               }
            }
        });
    }




}
