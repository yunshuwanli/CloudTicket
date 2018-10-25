package com.pri.yunshuwanli.cloudticket.entry;

import android.app.Activity;
import android.os.AsyncTask;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;

import java.util.List;
import java.util.Map;

public class PrinterShappingAsyncTask extends AsyncTask<Map<String, Object>, Void, Boolean> {

    public interface CallBack {
        void onCallBack(boolean result);
    }

    Activity activity;
    CallBack callBack;

    public PrinterShappingAsyncTask(Activity activity, CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Boolean doInBackground(Map<String, Object>... infos) {
        PrinterUtil.initPrinter(App.getIdal());
        Map<String, Object> pa = infos[0];
        String url = (String) pa.get("url");
        PrinterBean data = (PrinterBean) pa.get("data");
        boolean b = PrinterUtil.startShappingPrinter(activity, data, url);
        return b;
    }


    @Override
    protected void onPostExecute(Boolean res) {
        if (callBack != null)
            callBack.onCallBack(res);
    }
}
