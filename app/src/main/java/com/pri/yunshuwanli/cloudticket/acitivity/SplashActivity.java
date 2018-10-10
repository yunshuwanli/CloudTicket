package com.pri.yunshuwanli.cloudticket.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import yswl.com.klibrary.MApplication;
import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.ToastUtil;


public class SplashActivity extends MActivity implements HttpCallback<JSONObject> {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String uuid = UserManager.getUID();
        if (TextUtils.isEmpty(uuid)) {
            UIDSettingActivity.JumpAct(SplashActivity.this, 1);
        } else {
            requestInitUserInfo(uuid);
        }


//        String root = KLogger.getFilePath(this);
//        File rootF = new File(root);
//        if (!rootF.exists()) {
//            rootF.mkdirs();
//        }
//
//        File file1 = new File(root, "2018-10-04.log");
//        File file2 = new File(root, "2018-10-05.log");
//        File file3 = new File(root, "2018-10-06.log");
//        File file4 = new File(root, "2018-10-03.log");
//        File file5 = new File(root, "2018-10-02.log");
//        try {
//            file1.createNewFile();
//            file2.createNewFile();
//            file3.createNewFile();
//            file4.createNewFile();
//            file5.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void next() {
        MApplication.getApplication().getGolbalHander().postDelayed(new Runnable() {
            @Override
            public void run() {

                MainActivity.JumpAct(SplashActivity.this);
            }
        }, 3000);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String uid = data.getStringExtra("UID");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            requestInitUserInfo(uid);
            saveUid(uid);
        }
    }

    private void requestInitUserInfo(String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("reqType", "01");
        map.put("parms", uid);
        String params = GsonUtil.GsonString(map);
        String url = "http://test.datarj.com/webService/posService";
        HttpClientProxy.getInstance().postJSONAsyn(url, 1, params, this);

    }

    @Override
    public void onSucceed(int requestId, JSONObject result) {
        if (requestId == 1 && result != null) {
            if (result.optString("code").equals("0000")) {

                User user = User.jsonToUser(result.optJSONObject("data"));
                UserManager.setUser(user);
                next();
            } else {
                UserManager.setUid("");
                ToastUtil.showToast("获取初始化信息失败 code:" + result.optString("msg"));
            }
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg) {
        UserManager.setUid("");
        KLogger.d("Cloud", "根据uid获取信息失败，uid : " +
                UserManager.getUID() + " msg: " + errorMsg);
        ToastUtil.showToast("获取初始化信息失败 " + errorMsg);
    }

    private void saveUid(String uid) {
        UserManager.setUid(uid);
    }
}
