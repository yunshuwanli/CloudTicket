package com.pri.yunshuwanli.cloudticket.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.adapter.RecordListAdapter;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.keeplive.foreground.DaemonService;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
import com.pri.yunshuwanli.cloudticket.utils.BitmapUtils;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;
import com.pri.yunshuwanli.cloudticket.utils.PopupWindowUtil;
import com.pri.yunshuwanli.cloudticket.utils.PrinterTester;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;
import com.pri.yunshuwanli.cloudticket.view.WrapContentLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.OrderHttpCallBack;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.EmptyRecyclerView;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

public class MainActivity extends MActivity implements View.OnClickListener, OrderHttpCallBack {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int HTTP_REQUEST_ID = -1;

    public static void JumpAct(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    TextView data;
    TextView price;
    TextView count;
    RecyclerView recyclerView;
    RefreshLayout refreshLayout;
    RecordListAdapter adapter;
    static final List<String> items = new ArrayList<>(2);
    static int i = 0;

    static {
        items.add("上传日志");
        items.add("系统信息");
        i = new Random(100).nextInt();
    }

    private OrderDao dao;
    List<OrderInfo> ondayOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //开启进程保活
//        startService(new Intent(getApplicationContext(), DaemonService.class));
        initView();
        initTimer();
        initDataBase();

    }

    private void initDataBase() {
        dao = new OrderDao(this);
    }

    private OrderDao getDao() {
        if (dao == null)
            dao = new OrderDao(this);
        return dao;
    }




    //模拟订单生成
    void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //TODO TEST DATA

               OrderInfo info = new OrderInfo("20180913000000" + i,
                        10.00, "2018-09-28 23:59:59",
                        "沪A88888",
                        "某某场库，停车时间201809061433-201809061533共计一小时");
                if (beginPrinter(info)) {
                    requestSaveOrderInfo(info, false);
                }

            }
        }, 1000 * 10, 1000 * 60 * 60);
//
//        ServerThread serverThread = new ServerThread();
//        new Thread(serverThread).start();


    }

    private boolean beginPrinter(final OrderInfo info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrinterUtil.initPrinter(App.getIdal());
                PrinterUtil.startPrinter(MainActivity.this,info);
            }
        }).start();
        return false;
    }


    private void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
            }
        });
        data = findViewById(R.id.data);
        data.setText(DateUtil.getTodayDate2());
        price = findViewById(R.id.price);
        count = findViewById(R.id.ticket_count);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));

        adapter = new RecordListAdapter(this, null, R.layout.item_list_record);
        EmptyRecyclerView emptyView = new EmptyRecyclerView(this);
        emptyView.setNoticeAndIcon("没有找到哟", "暂无数据", -1);
        adapter.setEmptyView(emptyView);
        adapter.setOnClickListener(new RecordListAdapter.OnClickListener() {
            @Override
            public void onClick(OrderInfo info) {
                beginPrinter(info);
            }
        });
        recyclerView.setAdapter(adapter);
        refreshLayout.autoRefresh();

        findViewById(R.id.billing).setOnClickListener(this);
        findViewById(R.id.iv_menu).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ondayOrders = new OrderDao(MainActivity.this).queryOrderOfTime(10);
                L.e(TAG, "data size: " + ondayOrders.size());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count.setText(ondayOrders.size() + "");
                        price.setText(getTotalAmo(ondayOrders) + "");
                        refreshLayout.finishRefresh();
                        adapter.setList(ondayOrders);
                    }
                });
                OrderDao dao = new OrderDao(MainActivity.this);
                List<OrderInfo> failOrders = dao.queryOrderInfoUpdataFail();
                if (failOrders != null && failOrders.size() > 0) {
                    for (OrderInfo fail : failOrders) {
                        requestSaveOrderInfo(fail, true);
                    }
                }
            }
        }).start();


    }

    private double getTotalAmo(List<OrderInfo> list) {
        double total = 0;
        for (OrderInfo info : list) {
            total += info.getTotalAmount();
        }
        return total;
    }

    private void requestSaveOrderInfo(OrderInfo orderInfo, boolean isFailData) {
        if (orderInfo == null) return;
        final String url = "http://test.datarj.com/webService/kptService";
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderInfo.getOrderNo());
        data.put("quantity", "1");//商品数量
        data.put("unitPrice", "1");//商品单价
        data.put("totalAmount", orderInfo.getTotalAmount());
        data.put("orderDate", orderInfo.getOrderDate());
        data.put("carNo", orderInfo.getCarNo());
        data.put("remark", orderInfo.getRemark());
        Map<String, Object> params = new HashMap<>();
        params.put("appId", UserManager.getAppId());
        params.put("reqType", "66");
        params.put("sourceType", "1");
        params.put("clientNo", UserManager.getUser().getClientNo());
        params.put("sign", SignUtil.getSignStr(data));
        params.put("data", data);
        String param = GsonUtil.GsonString(params);
        int requestId = isFailData ? orderInfo.getId() : HTTP_REQUEST_ID;
        HttpClientProxy.getInstance().postJsonAsynAndParams(url, requestId, param, orderInfo, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.billing) {
            BillingActivity.JumpAct(MainActivity.this);
        }
        if (v.getId() == R.id.search) {
            SearchingActivity.JumpAct(MainActivity.this);
        }
        if (v.getId() == R.id.iv_menu) {

            popupWindow(items);
        }
    }

    PopupWindowUtil popupWindow;

    private void popupWindow(final List<String> items) {
        if (popupWindow == null) {
            popupWindow = new PopupWindowUtil(this, items);
        }
        popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (position == 0) {
                    requestLogger();
                }
                if (position == 1) {
                    UserInfoActivity.JumpAct(MainActivity.this);
                }
            }
        });
        //根据后面的数字 手动调节窗口的宽度
        popupWindow.show(findViewById(R.id.iv_menu), 4);

    }

    private void requestLogger() {


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public void onSucceed(int requestId, JSONObject result, Object o) {
        if (requestId == HTTP_REQUEST_ID) {
            if (result != null) {
                if (result.optString("code").equalsIgnoreCase("0000")) {
                    saveDataBase(o, true);

                } else {
                    String errInfo = result.optString("msg");
                    //TODO 日志埋点
                    //logutil
                    ToastUtil.showToast(errInfo);
                    saveDataBase(o, false);
                }
            }

        } else {//失败的数据
            if (result != null && result.optString("code").equalsIgnoreCase("0000")) {
                updataDataBase(o, true);
            } else {
                String errInfo = result.optString("msg");
                //TODO 日志埋点
            }
        }
    }

    private void saveDataBase(Object o, boolean statue) {
        if (o instanceof OrderInfo) {
            OrderInfo info = (OrderInfo) o;
            info.setUpdate(statue);
            getDao().add(info);
        }
        updateRecyleViewData(o);
    }

    private void updateRecyleViewData(Object o) {
        if (o instanceof OrderInfo) {
            OrderInfo info = (OrderInfo) o;
            if (adapter != null && recyclerView != null) {
                adapter.addItem(info);
//                adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);

            }

            if (count != null) {
                int currCount = Integer.valueOf(count.getText().toString());
                currCount++;
                count.setText(String.valueOf(currCount));
            }

            if (price != null) {
                double curprice = Double.valueOf(price.getText().toString());
                curprice = curprice + info.getTotalAmount();
                price.setText(String.valueOf(curprice));
            }


        }
    }

    private void updataDataBase(Object o, boolean statue) {
        if (o instanceof OrderInfo) {
            OrderInfo info = (OrderInfo) o;
            info.setUpdate(statue);
            getDao().updata(info);
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg, Object o) {
        ToastUtil.showToast("网络错误");
        if (requestId == HTTP_REQUEST_ID) {
            saveDataBase(o, false);
        } else {
            //donothing
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除30天之前的数据
        List<OrderInfo> list = getDao().queryOrderNDayBefore(30);
        if (list != null && list.size() > 0) {
            getDao().deleteNDayBefore(30);
        }

    }
}