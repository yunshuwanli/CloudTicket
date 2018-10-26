package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.adapter.GoodsListAdapter;
import com.pri.yunshuwanli.cloudticket.entry.SpItemEvent;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import yswl.com.klibrary.base.MActivity;

public class GoodsListActivity extends MActivity {
    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, GoodsListActivity.class);
        context.startActivity(intent);
    }

    RecyclerView mRecyclerView;
    GoodsListAdapter myAdapter;
    TextView count;
    LinearLayout llCar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new GoodsListAdapter(this, UserManager.getUser().getSpList(), R.layout.item_list_goods);
        myAdapter.setOnClickListener(new GoodsListAdapter.OnClickListener() {
            @Override
            public void onClick(User.SpListBean info) {
                BillingActivity.JumpAct(GoodsListActivity.this, info);
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        count = findViewById(R.id.select_count);
        llCar = findViewById(R.id.ll_shapping);
        llCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShappingListActivity.JumpAct(GoodsListActivity.this);
            }
        });
        UserManager.getUser().setShappingCount(new ArrayList<User.SpListBean>());
    }



    @Override
    protected void onResume() {
        super.onResume();


    }

    /*-----------eventbus 注册---------------*/

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe(sticky = true)
    public void onEvent(SpItemEvent event) {
        User.SpListBean bean = event.sldetail;

        if (UserManager.getUser() != null) {
            List<User.SpListBean> shappingCar = UserManager.getUser().getShappingCount();
            if (shappingCar == null) {
                shappingCar = new ArrayList<>();
            }
            if (bean != null) {
                shappingCar.add(bean);
            }
            count.setText(shappingCar.size() + "");
//                UserManager.getUser().setShappingCount(shappingCar);

        }


        EventBus.getDefault().removeStickyEvent(event);


    }

    /*-----------eventbus 注册end---------------*/


    /**
     * 清空购物车
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.getUser().getShappingCount().clear();
    }
}
