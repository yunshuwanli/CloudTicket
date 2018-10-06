package com.pri.yunshuwanli.cloudticket.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.acitivity.MainActivity;
import com.pri.yunshuwanli.cloudticket.adapter.RecordListAdapter;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.utils.BitmapUtils;
import com.pri.yunshuwanli.cloudticket.utils.PrinterTester;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yswl.com.klibrary.base.BaseRecyclerAdapter;
import yswl.com.klibrary.base.BaseViewHolder;
import yswl.com.klibrary.base.MFragment;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.util.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends MFragment {

    public static ListFragment getInstance(ArrayList<OrderInfo> list) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = getBundle(list);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Bundle getBundle(ArrayList<OrderInfo> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", list);
        return bundle;
    }

    public void setList(ArrayList<OrderInfo> list) {
        myAdapter.setList(list);
        myAdapter.notifyDataSetChanged();
    }

    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    RecyclerView mRecyclerView;
    RecordListAdapter myAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new RecordListAdapter(getActivity(), getData(), R.layout.item_list_record);
        myAdapter.setOnClickListener(new RecordListAdapter.OnClickListener() {
            @Override
            public void onClick(OrderInfo info) {
                beginPrinter(info);
            }
        });
        mRecyclerView.setAdapter(myAdapter);
    }

    private boolean beginPrinter(final OrderInfo info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrinterUtil.initPrinter(App.getIdal());
                PrinterUtil.startPrinter(ListFragment.this.getMActivity(),info);
            }
        }).start();
        return false;
    }
    private ArrayList<OrderInfo> getData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getParcelableArrayList("list");
        }
        return null;
    }


}



