package com.pri.yunshuwanli.cloudticket.logger;

import android.text.TextUtils;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

public class LoggerUploadUtil {

    /**
     * 满足自动上传日志 必须已经获取user信息
     * @return
     */
    public static boolean autoUploadAble(){
        return KLogger.getInstance().needAutoUploadFile() && !TextUtils.isEmpty(UserManager.getUID()) && (UserManager.getUser()!=null);
    }

    public static void requestLogger(boolean needUploadCurrTime) {
        String url = "http://test.datarj.com/webService/logService/fileUpload";
        Map<String, Object> params = new HashMap<>();

        String zipfilePath = KLogger.getInstance().getZipFile(KLogger.getFilePath(App.getApplication()),needUploadCurrTime);
        params.put("file", new File(zipfilePath));
        params.put("gsdm", UserManager.getUser().getGsdm());
        params.put("kpddm", UserManager.getUser().getClientNo());
        params.put("appId", UserManager.getUser().getAppId());
        HttpClientProxy.getInstance().postMultipart(url, 1, params, new HttpCallback<JSONObject>() {
            @Override
            public void onSucceed(int requestId, JSONObject result) {
                L.e("result"+result);
                if(result.optString("code").equals("0000")){
                    ToastUtil.showToast("上传成功");
                    KLogger.getInstance().autoClear();
                }else {
                    String msg = result.optString("msg");
                    ToastUtil.showToast("msg");
                }
            }

            @Override
            public void onFail(int requestId, String errorMsg) {
                L.e("日志上传失败");

            }
        });

    }

}
