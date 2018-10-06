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
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.fragment.ListFragment;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
import com.pri.yunshuwanli.cloudticket.utils.CarKeyboardUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;
import com.pri.yunshuwanli.cloudticket.view.SerchBoxView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.MKeyBoardUtils;
import yswl.com.klibrary.util.ToastUtil;

public class SearchingActivity extends MActivity implements HttpCallback<JSONObject>, View.OnTouchListener {
    private static final String TAG = SearchingActivity.class.getSimpleName();

    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, SearchingActivity.class);
        context.startActivity(intent);
    }

    TextView textView_search;
    SerchBoxView serchBoxView;
    ListFragment mResultFragment;
    CarKeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        textView_search = findViewById(R.id.submit);
        textView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = serchBoxView.getEditText().getText().toString().trim();
                MKeyBoardUtils.hideSoftKeyboard(SearchingActivity.this);
                searchKey(key);
            }
        });
        serchBoxView = findViewById(R.id.et_key);

        keyboardUtil = new CarKeyboardUtil(this, serchBoxView.getEditText());
        serchBoxView.getEditText().setOnTouchListener(this);

        serchBoxView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.contains("港") || text.contains("澳") || text.contains("学") ){
                    serchBoxView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                }else{
                    serchBoxView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void searchKey(String key) {
        if (TextUtils.isEmpty(key)) {
            ToastUtil.showToast("请输入车牌号");
            return;
        }
        if (!isCarNo(key)) {
            ToastUtil.showToast("车牌格式不正确");
        }
        OrderDao dao = new OrderDao(this);
        ArrayList<OrderInfo> list = (ArrayList<OrderInfo>) dao.queryOrderOfCarNo(key);

        if (list == null || list.size() == 0) {
            requestOrderForCarNo(key);
        } else {
            showResultUI(list);
        }

    }

    private void requestOrderForCarNo(String key) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("carNo", key);
        data.put("clientNo", UserManager.getUser().getClientNo());
        Map<String, Object> dataWra = new HashMap<>();
        dataWra.put("appId", UserManager.getUser().getAppId());
        dataWra.put("sign", SignUtil.getSignStr(data));
        dataWra.put("data", data);
        dataWra.put("reqType", "02");
        String jsonpar = GsonUtil.GsonString(dataWra);
        String url = "http://test.datarj.com/webService/posService";
        HttpClientProxy.getInstance().postJSONAsyn(url, 2, jsonpar, this);
    }

    private void showResultUI(ArrayList<OrderInfo> list) {
        if (mResultFragment == null) {
            mResultFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(TAG);
        }
        if (mResultFragment == null) {
            mResultFragment = ListFragment.getInstance(list);
        } else {
            mResultFragment.setList(list);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mResultFragment, TAG).commitAllowingStateLoss();
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
    public void onSucceed(int requestId, JSONObject result) {
        if (requestId == 2 && result != null) {
            if (result.optString("code").equals("0000")) {
                JSONObject data = result.optJSONObject("data");
                ArrayList<OrderInfo> infos = OrderInfo.jsonToList(data.optJSONArray("orderList"));
                showResultUI(infos);
            } else {
                result.optString("msg");
            }
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = serchBoxView.getResId();
        if(v.getId() == id){
            keyboardUtil.hideSystemKeyBroad();
            keyboardUtil.hideSoftInputMethod();
            if (!keyboardUtil.isShow())
                keyboardUtil.showKeyboard();
        }else {
            if (keyboardUtil.isShow())
                keyboardUtil.hideKeyboard();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyboardUtil.isShow()){
            keyboardUtil.hideKeyboard();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
