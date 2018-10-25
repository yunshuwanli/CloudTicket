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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterAsyncTask;
import com.pri.yunshuwanli.cloudticket.entry.PrinterBean;
import com.pri.yunshuwanli.cloudticket.entry.SpItemEvent;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
import com.pri.yunshuwanli.cloudticket.utils.CarKeyboardUtil;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
 * 手工录入 商品信息
 */
public class BillingActivity extends MActivity implements View.OnClickListener, OrderHttpCallBack {
    private static final String TAG = BillingActivity.class.getSimpleName();
    private static final int HTTP_REQUEST_ID = 19;

    public static void JumpAct(Context context, User.SpListBean item) {
        Intent intent = new Intent(context, BillingActivity.class);
        intent.putExtra("detail", item);
        context.startActivity(intent);
    }

    TextView tv_name;
    TextView tv_type;
    TextView tv_privce;
    EditText et_number;
    EditText et_total;
    Button bt_shoppingCar;
    User.SpListBean item;
    OrderDao dao;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        item = (User.SpListBean) getIntent().getSerializableExtra("detail");
        initUI();

    }

    private void initUI() {
        if (item == null) return;
        tv_name = findViewById(R.id.name);
        tv_type = findViewById(R.id.type);
        tv_privce = findViewById(R.id.price);
        et_number = findViewById(R.id.number);
        et_total = findViewById(R.id.total);
        bt_shoppingCar = findViewById(R.id.submit);
        bt_shoppingCar.setOnClickListener(this);
        tv_name.setText(item.getSpmc());
        if (item.getSpdj() == 0) {
            tv_privce.setText("");
            et_number.setFocusable(false);
            et_number.setFocusableInTouchMode(false);

        } else {
            tv_privce.setText(String.valueOf(item.getSpdj()));
            et_number.setFocusableInTouchMode(true);
            et_number.setFocusable(true);
            et_number.requestFocus();
        }


        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag) {
                    return;
                }
                flag = true;
                if (item != null) {
                    if (item.getSpdj() != 0) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (s.toString().startsWith("0") && s.toString().length() > 1) {
                                ToastUtil.showToast("数量输入有误");
                            } else {
                                try {
                                    BigDecimal count = new BigDecimal(s.toString());
                                    BigDecimal price = new BigDecimal(item.getSpdj());
                                    BigDecimal total = count.multiply(price);
                                    et_total.setText(String.valueOf(total));
                                } catch (Exception e) {
                                    ToastUtil.showToast("数量输入有误");
                                }
                            }

                        }

                    }
                }
                flag = false;

            }
        });
        et_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (flag) {
                    return;
                }
                flag = true;
                if (item != null) {
                    if (item.getSpdj() != 0) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (s.toString().startsWith("0") && s.toString().length() > 1) {
                                ToastUtil.showToast("数量输入有误");
                            } else {
                                try {
                                    BigDecimal total = new BigDecimal(s.toString());
                                    BigDecimal price = new BigDecimal(item.getSpdj());
                                    BigDecimal count = total.divide(price, 1, BigDecimal.ROUND_HALF_UP);
                                    et_number.setText(String.valueOf(count));
                                } catch (Exception e) {
                                    ToastUtil.showToast("金额输入有误");
                                }
                            }

                        } else {
                            BigDecimal total = new BigDecimal("0");
                            BigDecimal price = new BigDecimal(item.getSpdj());
                            BigDecimal count = total.divide(price, 1, BigDecimal.ROUND_HALF_UP);
                            et_number.setText(String.valueOf(count));
                        }
                    }
                }
                flag = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        String count = et_number.getText().toString().trim();
        String total = et_total.getText().toString().trim();

        if (item == null) return;
        if (item.getSpdj() != 0) {
            if (TextUtils.isEmpty(count)) {
                ToastUtil.showToast("数量不能为空");
                return;
            }
        }
        if (TextUtils.isEmpty(total)) {
            ToastUtil.showToast("总金额不能为空");
            return;
        }
        if ((count.startsWith("0") && count.length() > 1) || (total.startsWith("0") && total.length() > 1)) {
            ToastUtil.showToast("输入数量或金额有误");
            return;
        }
        try {
            item.setReal_total(Double.valueOf(total));
            if(TextUtils.isEmpty(count)){
                item.setCount(0);
            }else {
                item.setCount(Double.valueOf(count));
            }

        } catch (Exception e) {
            ToastUtil.showToast("输入数量或金额有误");
            return;
        }


        //通知购物车刷新
        SpItemEvent.postEvent(item);
        finish();



    }

    private void requestSaveOrderInfo(PrinterBean printerBean) {
        if (printerBean == null || printerBean.info == null) return;
        final String url = "http://test.datarj.com/webService/kptService";
        OrderInfo orderInfo = printerBean.info;
        List<User.SpListBean> list = printerBean.list;
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", OrderInfo.getOderID());
        if (list != null && list.size() > 0) {
            StringBuilder sbPrice = new StringBuilder();
            StringBuilder sbCode = new StringBuilder();
            double total = 0;
            for (User.SpListBean bean : list) {
                total = total + bean.getSpdj();
                sbPrice.append(bean.getSpdj());
                sbPrice.append(",");
                sbCode.append(bean.getSpdm());
                sbCode.append(",");
            }
            String price = sbPrice.substring(0, sbPrice.length() - 1);
            String code = sbCode.substring(0, sbCode.length() - 1);
            data.put("quantity", code);
            data.put("unitPrice", price);//商品单价
            data.put("totalAmount", total);
        } else {
            data.put("totalAmount", orderInfo.getTotalAmount());
        }
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
        int requestId = HTTP_REQUEST_ID;
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
                    KLogger.e(TAG, "-----订单请求失败-----" +
                            "\n------返回结果:" + result.toString() +
                            "\n ------订单详情：" + detailInfo);
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
        KLogger.e(TAG, "-----订单请求失败-----" +
                "\n------返回结果:" + errorMsg +
                "\n ------订单详情：" + detailInfo);
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
