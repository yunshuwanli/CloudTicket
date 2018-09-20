package com.pri.yunshuwanli.cloudticket.utils;

import android.util.Base64;

import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;
import yswl.com.klibrary.util.MD5Util;

public class SignUtil {

    //appId 由发票云服务平台初始化,webservice 中的第 1 个参数
    private static final String appId = "RJ1234567890";

    //key 由发票云服务平台初始化,双方约定后保密
    private static final String key = "abcdefghijklmnopqrstnvwxyz123456";
    //webservice 接口中的第 3 个参数,业务数据,如 InvoiceData,QueryData,OrderData
//    String data = "{"serialNumber": "2016062412444500001",..}";
// 生成签名的原数据,顺序必须如此
//    String signSourceData = "data=" + data + "&key=" + key;
    //通过 md5 算法获得签名串,webservice 中的第 2 个参数
//    String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(signSourceData);

    public static String getSignStr(Map<String, Object> data) {
        String dataStr = GsonUtil.GsonString(data);
        String signSourceData = "data=" + dataStr + "&key=" + key;
        return MD5Util.MD5(signSourceData);
    }

    private static final String QR_CODE_URL_RORMAT = "http://fpjtest.datarj.com/einv/kptService/%s/%s";
    private static final String TAG = SignUtil.class.getSimpleName();

    public static String getQrCodeUrl(OrderInfo orderInfo) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("on", orderInfo.getOrderNo());
        data.put("ot", orderInfo.getOrderDate());
        data.put("pr",orderInfo.getTotalAmount()+"");
        data.put("sn", "门店号/开票点代码");
        data.put("sp", "");
        data.put("type", "2");
        String jsonStr = GsonUtil.GsonString(data);

        String original_data = "data=" + jsonStr + "&key=" + key;
        String si_md5Str = MD5Util.MD5(original_data);
        String sign_before = "data=" + jsonStr + "&si=" + si_md5Str;
        String signed = new String(Base64.encode(sign_before.getBytes(), Base64.DEFAULT));
        L.d(TAG,"base64 encode："+signed);
        String url = String.format(QR_CODE_URL_RORMAT, "66", signed);
        L.d(TAG,"二维码网址："+url);
        return url;
    }
}
