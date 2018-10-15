package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.adapter.RecordListAdapter;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;

import java.util.List;

import yswl.com.klibrary.base.BaseRecyclerAdapter;
import yswl.com.klibrary.base.BaseViewHolder;
import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.EmptyRecyclerView;

@Deprecated
public class RecordActivity extends MActivity {

    public static final String TYPE = "TYPE";//数据库内未查询到数据，服务端查询结果展示
    public static final String KEY = "search_key";//数据库内未查询到数据，服务端查询结果展示

    /**
     * @param context
     * @param key     搜索关键字
     */
    public static void JumpAct(Context context,  String key) {
        Intent intent = new Intent(context, RecordActivity.class);
        intent.putExtra(KEY, key);
        context.startActivity(intent);
    }

    public String type;
    public String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initUI();
        type = getIntent().getStringExtra(TYPE);
        key = getIntent().getStringExtra(KEY);


    }

    private List<Object> getData(String type) {


        return null;
    }

    private List<Object> requestNetData(String key) {
        String url = "";

//        HttpClientProxy.getInstance().getAsyn(url, 1,);
        return null;
    }

    private List<Object> queryData() {
        return null;
    }


    RecyclerView recyclerView;

    private void initUI() {
        findViewById(R.id.search_tip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchingActivity.JumpAct(RecordActivity.this);
            }
        });
        recyclerView = findView(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EmptyRecyclerView emptyView = new EmptyRecyclerView(this);
        emptyView.setNoticeAndIcon("没有找到哟", "暂无数据", -1);
        RecordListAdapter adapter = new RecordListAdapter(this,null,R.layout.item_list_record);
        adapter.setEmptyView(emptyView);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


}
