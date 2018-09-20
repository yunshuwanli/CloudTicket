package com.pri.yunshuwanli.cloudticket.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.fragment.ListFragment;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
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

public class SearchingActivity extends MActivity implements HttpCallback<JSONObject> {
    private static final String TAG = SearchingActivity.class.getSimpleName();

    public static void JumpAct(Context context) {
        Intent intent = new Intent(context, SearchingActivity.class);
        context.startActivity(intent);
    }

    TextView textView_search;
    SerchBoxView serchBoxView;
    ListFragment mResultFragment;

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
                ArrayList<OrderInfo> infos = OrderInfo.jsonToList(result.optJSONArray("orderList"));
                showResultUI(infos);
            } else {
                result.optString("msg");
            }
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg) {

    }
}
