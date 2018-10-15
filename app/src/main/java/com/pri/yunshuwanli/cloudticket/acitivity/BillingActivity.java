package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterAsyncTask;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
import com.pri.yunshuwanli.cloudticket.utils.CarKeyboardUtil;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.OrderHttpCallBack;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

/**
 * 手工录入
 */
public class BillingActivity extends MActivity implements View.OnTouchListener, View.OnClickListener, OrderHttpCallBack {
    private static final String TAG = BillingActivity.class.getSimpleName();
    private static final int HTTP_REQUEST_ID = 19;

    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, BillingActivity.class);
        context.startActivity(intent);
    }
    private OrderDao dao;
    EditText mEditText_CarNo;
    EditText mEditText_Price;
    Button button;
    CarKeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        initUI();

    }

    private void initUI() {
        mEditText_CarNo = findViewById(R.id.car_no);
        button = findViewById(R.id.submit);
        mEditText_Price = findViewById(R.id.price);
        keyboardUtil = new CarKeyboardUtil(this, mEditText_CarNo);
        button.setOnClickListener(this);
        mEditText_CarNo.setOnTouchListener(this);
        mEditText_Price.setOnTouchListener(this);
        mEditText_CarNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains("港") || text.contains("澳") || text.contains("学")) {
                    mEditText_CarNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                } else {
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
        if (v.getId() == R.id.car_no) {
            keyboardUtil.hideSystemKeyBroad();
            keyboardUtil.hideSoftInputMethod();
            if (!keyboardUtil.isShow())
                keyboardUtil.showKeyboard();
        } else {
            if (keyboardUtil.isShow())
                keyboardUtil.hideKeyboard();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    public static boolean isCarNo(String carNo) {
        Pattern p = Pattern.compile(Contant.CAR_NO_PATTERN);
        Matcher m = p.matcher(carNo);
        if (!m.matches()) {
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        String car_no = mEditText_CarNo.getText().toString().trim();
        String prince = mEditText_Price.getText().toString().trim();
        if (TextUtils.isEmpty(car_no)) {
            ToastUtil.showToast("车牌号为空");
            return;
        }
        if (TextUtils.isEmpty(prince)) {
            ToastUtil.showToast("价格为空");
            return;
        }

        if (!isCarNo(car_no)) {
            ToastUtil.showToast("车牌格式不正确");
            return;
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUpdate(false);
        orderInfo.setCarNo(car_no);
//        orderInfo.setPrintStatue(false);
        String orderNo = UserManager.getUser().getClientNo() + DateUtil.getNowTimeStamp();
        L.e(TAG, orderNo);
        orderInfo.setTotalAmount(Double.valueOf(prince));
        orderInfo.setOrderNo(orderNo);
        orderInfo.setOrderDate(DateUtil.getTodayDate());

        final OrderInfo info = orderInfo;
        new PrinterAsyncTask(this, new PrinterAsyncTask.CallBack() {
            @Override
            public void onCallBack(boolean result) {
                if (result) {
                    requestSaveOrderInfo(info);
                }
            }
        }).execute(orderInfo);

    }

    private void requestSaveOrderInfo(OrderInfo orderInfo) {
        if (orderInfo == null) return;
        final String url = "http://test.datarj.com/webService/kptService";
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderInfo.getOrderNo());
//        data.put("quantity", "1");//商品数量
//        data.put("unitPrice", orderInfo.getTotalAmount());//商品单价
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
        int requestId =  HTTP_REQUEST_ID;
        HttpClientProxy.getInstance().postJsonAsynAndParams(url, requestId, param, orderInfo, this);
    }

    @Override
    public void onSucceed(int requestId, JSONObject result, Object o) {
        if (requestId == HTTP_REQUEST_ID) {
            if (result != null) {
                if (result.optString("code").equalsIgnoreCase("0000")) {
                    //保存数据库
                    saveDataBase(o, true);
                } else {
                    String errInfo = result.optString("msg");
                    //TODO 日志埋点
                    String detailInfo = o.toString();
                    KLogger.e(TAG,"-----订单请求失败-----" +
                            "\n------返回结果:" +result.toString()+
                            "\n ------订单详情："+detailInfo);
                    ToastUtil.showToast(errInfo);
                    //失败数据保存数据库
                    saveDataBase(o, false);
                }
            }

        }
    }

    @Override
    public void onFail(int requestId, String errorMsg, Object o) {
        ToastUtil.showToast("网络错误");
        String detailInfo = o.toString();
        KLogger.e(TAG,"-----订单请求失败-----" +
                "\n------返回结果:" +errorMsg+
                "\n ------订单详情："+detailInfo);
        if (requestId == HTTP_REQUEST_ID) {
            saveDataBase(o, false);
        } else {
            //donothing
        }
    }
    private OrderDao getDao() {
        if (dao == null)
            dao = new OrderDao(this);
        return dao;
    }
    private void saveDataBase(Object o, boolean statue) {
        if (o instanceof OrderInfo) {
            OrderInfo info = (OrderInfo) o;
            info.setUpdate(statue);
            getDao().add(info);
        }
    }
}
