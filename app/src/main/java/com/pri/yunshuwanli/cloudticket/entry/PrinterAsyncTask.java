package com.pri.yunshuwanli.cloudticket.entry;

import android.app.Activity;
import android.os.AsyncTask;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;

public class PrinterAsyncTask extends AsyncTask<OrderInfo, Void, Integer> {

    public interface CallBack {
        void onCallBack(int result);
    }

    Activity activity;
    CallBack callBack;

    public PrinterAsyncTask(Activity activity, CallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Integer doInBackground(OrderInfo... infos) {
        PrinterUtil.initPrinter(App.getIdal());
        int b = PrinterUtil.startCarPrinter(activity, infos[0]);
        return b;
    }


    @Override
    protected void onPostExecute(Integer res) {
        if (callBack != null)
            callBack.onCallBack(res);
    }
}
