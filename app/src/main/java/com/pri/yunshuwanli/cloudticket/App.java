package com.pri.yunshuwanli.cloudticket;

import android.content.Context;
import android.widget.Toast;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pri.yunshuwanli.cloudticket.utils.KLogger;
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


        KLogger.getInstance().init(this);
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
