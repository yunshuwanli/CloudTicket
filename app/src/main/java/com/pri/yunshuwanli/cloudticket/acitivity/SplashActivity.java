package com.pri.yunshuwanli.cloudticket.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.czm.library.save.imp.LogWriter;
import com.pri.yunshuwanli.cloudticket.R;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;
import com.pri.yunshuwanli.cloudticket.utils.DataFactory;
import com.pri.yunshuwanli.cloudticket.utils.IpUtil;
import com.pri.yunshuwanli.cloudticket.utils.ServerThread;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yswl.com.klibrary.MApplication;
import yswl.com.klibrary.base.MActivity;
import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.http.okhttp.MSPUtils;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

import static com.pri.yunshuwanli.cloudticket.Contant.SP_File_NANE;
import static com.pri.yunshuwanli.cloudticket.Contant.UUID;

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
        if(requestCode == 1 && resultCode== Activity.RESULT_OK){
            requestInitUserInfo(UserManager.getUID());
        }
    }

    private void requestInitUserInfo(String uid) {
        Map<String,Object> map = new HashMap<>();
        map.put("reqType","01");
        map.put("parms", uid);
        String params =  GsonUtil.GsonString(map);
        String url = "http://test.datarj.com/webService/posService";
        HttpClientProxy.getInstance().postJSONAsyn(url,1,params,this);

    }

    @Override
    public void onSucceed(int requestId, JSONObject result) {
        if(requestId==1 && result!=null){
            if(result.optString("code").equals("0000")){
                User user =  User.jsonToUser(result.optJSONObject("data"));
                UserManager.setUser(user);
                next();
            }else {
                ToastUtil.showToast("获取初始化信息失败 code:"+result.optString("msg"));
//                //TODO TEST DATA
//                User user =  new User();
//                user.setAppId("001");
//                user.setGsmc("纳税名是上海");
//                user.setClientNo("00001");
//                user.setKey("qwerttyue");
//                user.setSkr("康子");
//                user.setKpdmc("上海南站停车场");
//                UserManager.setUser(user);
//                MainActivity.JumpAct(this);
//                ToastUtil.showToast(result.optString("msg"));
//                LogWriter.writeLog("Cloud", "根据uid获取信息失败，uid : "+
//                        UserManager.getUID() + " data: "+result);
            }
        }
    }

    @Override
    public void onFail(int requestId, String errorMsg) {
        LogWriter.writeLog("Cloud", "根据uid获取信息失败，uid : "+
                UserManager.getUID() +" msg: "+errorMsg);
        ToastUtil.showToast("获取初始化信息失败 code:"+errorMsg);
    }
}
