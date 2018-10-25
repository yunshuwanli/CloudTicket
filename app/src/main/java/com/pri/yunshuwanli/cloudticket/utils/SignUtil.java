package com.pri.yunshuwanli.cloudticket.utils;

import android.util.Base64;

import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.entry.PrinterBean;
import com.pri.yunshuwanli.cloudticket.entry.User;
import com.pri.yunshuwanli.cloudticket.entry.UserManager;

import org.apaches.commons.codec.digest.DigestUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.MD5Util;

public class SignUtil {

    //appId 由发票云服务平台初始化,webservice 中的第 1 个参数
//    private static final String appId = "RJ1234567890";

    //key 由发票云服务平台初始化,双方约定后保密
//    private static final String key = "abcdefghijklmnopqrstnvwxyz123456";
    //webservice 接口中的第 3 个参数,业务数据,如 InvoiceData,QueryData,OrderData
//    String data = "{"serialNumber": "2016062412444500001",..}";
// 生成签名的原数据,顺序必须如此
//    String signSourceData = "data=" + data + "&key=" + key;
    //通过 md5 算法获得签名串,webservice 中的第 2 个参数
//    String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(signSourceData);

    public static String getSignStr(Map<String, Object> data) {
        String dataStr = GsonUtil.GsonString(data);
        String signSourceData = "data=" + dataStr + "&key=" + UserManager.getUser().getKey();
        return DigestUtils.md5Hex(signSourceData);
    }

    private static final String QR_CODE_URL_RORMAT = "http://fpjtest.datarj.com/einv/kptService/%s/%s";
    private static final String QR_CODE_URL_SHORT_RORMAT = "http://fpjtest.datarj.com/e?";
    private static final String QR_CODE_URL_PARMAR_RORMAT = "kptService/%s/%s";
    private static final String TAG = SignUtil.class.getSimpleName();

    public static String getQrCodeUrl(PrinterBean printer,boolean isNeedChange) {
        if (printer == null || printer.info == null) return "";
        OrderInfo orderInfo = printer.info;
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("on", orderInfo.getOrderNo());//平台订单号
        data.put("ot", orderInfo.getOrderDate());//日期
        data.put("sn", UserManager.getUser().getClientNo());//门店号、开票点代码
        List<User.SpListBean> listBeans = printer.list;
        if (listBeans != null && listBeans.size() > 0) {
            StringBuilder sbPrice = new StringBuilder();
            StringBuilder sbCode = new StringBuilder();
            StringBuilder sbCount = new StringBuilder();
            for (User.SpListBean bean : listBeans) {
                sbPrice.append(bean.getReal_total());
                sbPrice.append(",");
                sbCode.append(bean.getSpdm());
                sbCode.append(",");
                sbCount.append(bean.getCount());
                sbCount.append(",");
            }
            String price = sbPrice.substring(0, sbPrice.length() - 1);
            String code = sbCode.substring(0, sbCode.length() - 1);
            String count = sbCount.substring(0, sbCount.length() - 1);
            data.put("pr", price);//订单一项商品的总金额数组
            data.put("sp", code);//商品代码数组
            data.put("qt", count);//商品代码数组
        } else { //可视为停车场二维码
            data.put("pr", orderInfo.getTotalAmount());//订单总金额
        }
        data.put("type", "2");//固定2
        String jsonStr = GsonUtil.GsonString(data);
        String original_data = "data=" + jsonStr + "&key=" + UserManager.getUser().getKey();
        L.d(TAG, "明文json ：" + original_data);
        String si_md5Str = MD5Util.MD5(original_data);
//        String si_md5Str =  DigestUtils.md5Hex(original_data);
        L.d(TAG, "si_md5Str md5：" + si_md5Str);

        String sign_before = "data=" + jsonStr + "&si=" + si_md5Str;
        String signed = new String(Base64.encode(sign_before.getBytes(), Base64.DEFAULT));
        L.d(TAG, "base64 encode：" + signed);
        String url;
        if(isNeedChange){
            url = String.format(QR_CODE_URL_PARMAR_RORMAT, UserManager.getUser().getGsdm(), signed);
            L.d(TAG, "二维码无域名长连接：" + url);
        }else {
            url = String.format(QR_CODE_URL_RORMAT, UserManager.getUser().getGsdm(), signed);
            L.d(TAG, "二维码长连接：" + url);
        }

        return url;
    }

    public static String getShortUrl(String shortParams){
        return QR_CODE_URL_SHORT_RORMAT+shortParams;
    }
}


