package com.pri.yunshuwanli.cloudticket.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;

public class UserInfoActivity extends AppCompatActivity {
    public static void JumpAct(Activity context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        User user = UserManager.getUser();
        textView1 = findViewById(R.id.tv_tax_name);
        textView2 = findViewById(R.id.tv_ck_info);
//        textView3 = findViewById(R.id.tv_gt_info);
        textView4 = findViewById(R.id.tv_device_id);
        textView5 = findViewById(R.id.tv_user_name);

        textView1.setText(user.getGsmc());
        textView2.setText(user.getKpdmc());
        textView4.setText(UserManager.getUID());
        textView5.setText(user.getSkr());

    }
}
