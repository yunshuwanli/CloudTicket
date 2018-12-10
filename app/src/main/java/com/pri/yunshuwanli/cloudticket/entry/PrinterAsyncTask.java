package com.pri.yunshuwanli.cloudticket.entry;

import android.app.Activity;
import android.os.AsyncTask;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.Contant;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;
import com.pri.yunshuwanli.cloudticket.utils.SignUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import yswl.com.klibrary.http.CallBack.HttpCallback;
import yswl.com.klibrary.http.HttpClientProxy;
import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.ToastUtil;

public class PrinterAsyncTask extends AsyncTask<OrderInfo, Void, Integer> {
    private static final String TAG = "PrinterAsyncTask";
    public interface CallBack {
        void onCallBack(int result);
    }

    Activity activity;
    CallBack callBack;

    int resultcode = 0;
    public PrinterAsyncTask(Activity activity, CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Integer doInBackground(final OrderInfo... infos) {
        PrinterUtil.initPrinter(App.getIdal());
        //SignUtil.getQrCodeUrl(new PrinterBean(order, null),false)
        String qrUrl = SignUtil.getQrCodeUrl(new PrinterBean(infos[0], null),UserManager.userSimpeQR());
        qrUrl = qrUrl.replace("\n","");

        if (UserManager.userSimpeQR()) {
            Map<String, Object> data = new LinkedHashMap<>();
            try {
                data.put("longUrl", URLEncoder.encode(qrUrl,"utf-8"));
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


            HttpClientProxy.getInstance().postJSONAsyn(url, 2, jsonpar, new HttpCallback<JSONObject>() {
                @Override
                public void onSucceed(int requestId, JSONObject result) {
                    if (requestId == 2 && result != null) {
                        if (result.optString("code").equals("0000")) {
                            try {
                                String str = result.optJSONObject("data").optString("shortUrl");
                                resultcode = gotoPrint(SignUtil.getShortUrl(str), infos[0]);
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

                }
            });
        } else {
            resultcode  =gotoPrint(qrUrl,infos[0]);
        }

        return resultcode;
    }

    private int gotoPrint(String url, OrderInfo bean){
        PrinterUtil.initPrinter(App.getIdal());
        int b = PrinterUtil.startCarPrinter(activity, bean, url);
        return b;
    }


    @Override
    protected void onPostExecute(Integer res) {
        if (callBack != null)
            callBack.onCallBack(res);
    }
}
