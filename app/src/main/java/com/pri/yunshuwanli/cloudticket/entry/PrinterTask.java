package com.pri.yunshuwanli.cloudticket.entry;

import android.app.Activity;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.utils.PrinterUtil;

import java.util.concurrent.Callable;

public class PrinterTask implements Callable<Boolean> {
        OrderInfo info ;
        Activity activity;
        public PrinterTask(Activity activity,OrderInfo info) {
            this.activity = activity;
            this.info = info;
        }

        @Override
        public Boolean call() throws Exception {
            PrinterUtil.initPrinter(App.getIdal());
            boolean b = PrinterUtil.startPrinter(activity,info);
            return b;
        }
    }