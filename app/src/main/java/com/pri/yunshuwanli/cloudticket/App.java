package com.pri.yunshuwanli.cloudticket;

import android.widget.Toast;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;
import com.pri.yunshuwanli.cloudticket.logger.LoggerUploadUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;


import yswl.com.klibrary.MApplication;

public class App extends MApplication {

    @Override
    public boolean getDebugSetting() {
        return true;
    }


    private static IDAL idal;

    public static IDAL getIdal() {
        return idal;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initIDAL();
//        Stetho.initializeWithDefaults(this);
        //bugly  日志与版本更新
        Bugly.init(getApplicationContext(), "50d067446d", this.getDebugSetting());

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        KLogger.getInstance().init(this);
        //启动上传日志
        if(LoggerUploadUtil.autoUploadAble()){
            LoggerUploadUtil.requestLogger(false);
        }
    }

    private void initIDAL() {
        try {
            idal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == idal) {
            Toast.makeText(this, "error occurred,DAL is null.", Toast.LENGTH_LONG).show();
            return;
        }


    }


    @Override
    public String getBaseUrl_Https() {
        return null;
    }





}
