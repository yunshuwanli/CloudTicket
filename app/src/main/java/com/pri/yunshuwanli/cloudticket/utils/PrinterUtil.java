package com.pri.yunshuwanli.cloudticket.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Looper;

import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.acitivity.MainActivity;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterBean;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.logger.KLogger;

import java.util.List;

import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

public class PrinterUtil {

    private static final String TAG = PrinterUtil.class.getSimpleName();

    public static void initPrinter(IDAL idal) {
        if(idal==null){
            try {
                idal = NeptuneLiteUser.getInstance().getDal(App.getApplication().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
                KLogger.e(TAG,"----- 初始化打印失败 ："+e.getMessage());
            }
        }
        PrinterTester.getInstance().init(idal);
        PrinterTester.getInstance().fontSet(EFontTypeAscii.FONT_16_24, EFontTypeExtCode.FONT_24_24);
        PrinterTester.getInstance().spaceSet(Byte.parseByte("1"), Byte.parseByte("0"));
        PrinterTester.getInstance().leftIndents(Short.parseShort("0"));
        PrinterTester.getInstance().setGray(Integer.parseInt("4"));
        PrinterTester.getInstance().setInvert(false);
        PrinterTester.getInstance().step(Integer.parseInt("50"));
    }

    public static boolean startShappingPrinter(Activity activity, PrinterBean bean, String url) {
        Bitmap bitmap = BitmapUtils.getShappingBitmap(activity, bean,url);
        PrinterTester.getInstance().printBitmap(bitmap);
        PrinterTester.getInstance().printStr("\n \n \n \n", null);

        final String status = PrinterTester.getInstance().start();
        KLogger.i(TAG,"-----打印完成状态------：" + status);
        if (status.equals("打印成功")) {
            return true;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(status);
            }
        });
        return false;
    }

    public static boolean startCarPrinter(Activity activity, OrderInfo info) {
        Bitmap bitmap = BitmapUtils.getTicktBitmap(activity, info);
        PrinterTester.getInstance().printBitmap(bitmap);
        PrinterTester.getInstance().printStr("\n \n \n \n", null);

        final String status = PrinterTester.getInstance().start();
//            PrinterTester.getInstance().
        KLogger.i(TAG,"-----打印完成状态------：" + status);
        if (status.equals("打印成功")) {
            return true;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(status);
            }
        });
        return false;
    }
}
