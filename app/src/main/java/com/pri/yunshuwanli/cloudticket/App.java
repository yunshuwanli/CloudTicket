package com.pri.yunshuwanli.cloudticket;

import android.content.Context;
import android.widget.Toast;

import com.czm.library.LogUtil;
import com.czm.library.save.imp.CrashWriter;
import com.czm.library.upload.http.HttpReporter;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pri.yunshuwanli.cloudticket.utils.PrinterTester;
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
        initCrashReport();
//        Stetho.initializeWithDefaults(this);
        com.orhanobut.logger.Logger.addLogAdapter(new AndroidLogAdapter());
        //bugly  日志与版本更新
        Bugly.init(getApplicationContext(), "50d067446d", this.getDebugSetting());


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

    private void initCrashReport() {
        LogUtil.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setLogDir(getApplicationContext(), "sdcard/" + this.getString(this.getApplicationInfo().labelRes) + "/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(false)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogLeve(LogUtil.LOG_LEVE_ERROR)//设置为日常日志也会上传
                //.setLogDebugModel(true) //设置是否显示日志信息
                //.setLogContent(LogUtil.LOG_LEVE_CONTENT_NULL)  //设置是否在邮件内容显示附件信息文字
                .setLogSaver(new CrashWriter(getApplicationContext()))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
        initHttpReporter();
    }

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        HttpReporter http = new HttpReporter(this);
        http.setUrl("http://crashreport.jd-app.com/your_receiver");//发送请求的地址
        http.setFileParam("fileName");//文件的参数名
        http.setToParam("to");//收件人参数名
        http.setTo("你的接收邮箱");//收件人
        http.setTitleParam("subject");//标题
        http.setBodyParam("message");//内容
        LogUtil.getInstance().setUploadType(http);
    }

    public static void uploadLog(Context context) {
        LogUtil.getInstance().upload(context);
        //LogWriter.writeLog("czm", "打Log测试！！！！");
        //FileUtil.deleteDir(new File(LogUtil.getInstance().getROOT())); 手动删除本地日志
    }

}
