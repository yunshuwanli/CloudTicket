package com.pri.yunshuwanli.cloudticket.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Looper;

import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.acitivity.MainActivity;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;

import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.ToastUtil;

public class PrinterUtil {

    public static void initPrinter(IDAL idal) {
        PrinterTester.getInstance().init(App.getIdal());

        PrinterTester.getInstance().fontSet(EFontTypeAscii.FONT_16_24, EFontTypeExtCode.FONT_24_24);
        PrinterTester.getInstance().spaceSet(Byte.parseByte("1"), Byte.parseByte("0"));
        PrinterTester.getInstance().leftIndents(Short.parseShort("0"));
        PrinterTester.getInstance().setGray(Integer.parseInt("4"));
        PrinterTester.getInstance().setInvert(false);
//        PrinterTester.getInstance().printStr("       \n   \n", null);
        PrinterTester.getInstance().step(Integer.parseInt( "200"));
    }

    public static boolean startPrinter(Activity activity, OrderInfo info) {
        Bitmap bitmap = BitmapUtils.getTicktBitmap(activity, info);
        PrinterTester.getInstance().printBitmap(bitmap);
//        PrinterTester.getInstance().printStr("\n 打印测试语句ABCabc123~!#$富豪コンピュータ", null);

        String status = PrinterTester.getInstance().start();
//            PrinterTester.getInstance().
        L.d("打印完成状态：" + status);
        if (status.equals("打印成功")) {
            return true;
        }
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            Looper.prepare();
            ToastUtil.showToast(status);
            Looper.loop();
        } else {
            ToastUtil.showToast(status);
        }
        return false;
    }
}
