package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.adapter.ShappingCarListAdapter;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterBean;
import com.pri.yunshuwanli.cloudticket.entry.PrinterShappingAsyncTask;
import com.pri.yunshuwanli.cloudticket.entry.SpItemEvent;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;
import com.pri.yunshuwanli.cloudticket.utils.QRCodeUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.ToastUtil;

public class ShappingListActivity extends MActivity implements HttpCallback<JSONObject> {
    private static final String TAG = "ShappingListActivity";

    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, ShappingListActivity.class);
        context.startActivity(intent);
    }

    RecyclerView mRecyclerView;
    ShappingCarListAdapter myAdapter;
    List<User.SpListBean> mData;
    TextView mToltal;
    private void gotoPrint(String url, PrinterBean bean) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("data", bean);
        new PrinterShappingAsyncTask(ShappingListActivity.this, new PrinterShappingAsyncTask.CallBack() {
            @Override
            public void onCallBack(boolean result) {
                //TODO请求网络
                //关闭页面，清空购物车
                UserManager.getUser().getShappingCount().clear();
                SpItemEvent.postEvent(null);
                ShappingListActivity.this.finish();
            }
        }).execute(map);
    }

    TextView mPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shapping_list);

        mRecyclerView = findViewById(R.id.recyclerview);
        mToltal = findViewById(R.id.total);
        mPrint = findViewById(R.id.print);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mData = UserManager.getUser().getShappingCount();
        myAdapter = new ShappingCarListAdapter(this, mData, R.layout.item_list_shapping_car);
        myAdapter.setOnClickListener(new ShappingCarListAdapter.OnClickListener() {
            @Override
            public void onClick(User.SpListBean info, int position) {
                mData.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position, myAdapter.getList().size());

                mToltal.setText(String.valueOf(getTotalPrice()));
                //通知商品列表购物车数量更新
                SpItemEvent.postEvent(null);
            }
        });
        mRecyclerView.setAdapter(myAdapter);

        mToltal.setText(String.valueOf(getTotalPrice()));

        mPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mData != null && mData.size() > 0) {
                    PrinterBean bean = creatOrderDetail(mData);
                    String qrUrl = SignUtil.getQrCodeUrl(bean,UserManager.userSimpeQR());
                    qrUrl = qrUrl.replace("\n","");
                    if (UserManager.userSimpeQR()) {
                        requestQrCodeURl(qrUrl);
                    } else {
                        gotoPrint(qrUrl, bean);
                    }
                }

            }
        });


    }

    private void requestQrCodeURl(String longUrl) {
        Map<String, Object> data = new LinkedHashMap<>();
        try {
            data.put("longUrl", URLEncoder.encode(longUrl,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String, Object> dataWra = new HashMap<>();
        dataWra.put("appId", UserManager.getUser().getAppId());
        dataWra.put("sign", SignUtil.getSignStr(data));
        dataWra.put("data", data);
        dataWra.put("reqType", "04");
        String jsonpar = GsonUtil.GsonString(dataWra);
        String url ;
        if(App.getApplication().isTestUrl()){
            url = Contant.TEST_BASE_URL_POS;
        }else {
            url = Contant.BASE_URL_POS;
        }
        HttpClientProxy.getInstance().postJSONAsyn(url, 2, jsonpar, this);
    }

    private PrinterBean creatOrderDetail(List<User.SpListBean> list) {
        OrderInfo info = new OrderInfo();
        info.setOrderNo(OrderInfo.getOderID());
        info.setOrderDate(DateUtil.getTodayDate3());
        PrinterBean bean = new PrinterBean(info, list);
        return bean;
    }


    String getTotalPrice() {
        double total = 0;
        if (mData != null) {
            for (User.SpListBean bean : mData) {
                total += bean.getReal_total();
            }
        }
        return String.valueOf(new BigDecimal(total).setScale(2,BigDecimal.ROUND_HALF_UP));


    }


    @Override
    public void onSucceed(int requestId, JSONObject result) {
        if (requestId == 2 && result != null) {
            if (result.optString("code").equals("0000")) {
                try {
                    String str = result.optJSONObject("data").optString("shortUrl");
                    gotoPrint(SignUtil.getShortUrl(str), creatOrderDetail(mData));
                }catch (Exception e){

                    KLogger.e(TAG, "-----长连接转短连接json解析失败: " +
                            "\n----- msg: " + result.toString()
                    );
                }

            } else {
                ToastUtil.showToast("验签失败，请重试");
                KLogger.e(TAG, "-----长连接转短连接失败: " +
                        "\n----- msg: " + result.toString()
                );
            }
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg) {
        ToastUtil.showToast("网络错误，请重试");
        KLogger.e(TAG, "-----网络错误:长连接转换错误 " +
                "\n----- msg: " + errorMsg

        );
    }
}
