package com.pri.yunshuwanli.cloudticket;

import android.util.Base64;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.MD5Util;

public class TestQRUrl{

        private static final String QR_CODE_URL_RORMAT = "http://fpjtest.datarj.com/einv/kptService/%s/%s";
        private static final String key = "a";

        @Test
        public  void getQrCodeUrl() {

            Map<String, String> data = new LinkedHashMap<>();
            data.put("on", "1");
            data.put("ot", "2");
            data.put("pr", "3");
            data.put("sn", "4");
            data.put("sp", "5");
            data.put("type", "6");
            String jsonStr = GsonUtil.GsonString(data);
            System.out.println("data:" + jsonStr);
            String original_data = "data=" + jsonStr + "&key=" + key;
            System.out.println("original_data" + original_data);
            String si_md5Str = MD5Util.MD5(original_data);
            System.out.println("si_md5Str:" + si_md5Str);
            String sign_before = "data=" + jsonStr + "&si=" + si_md5Str;
            System.out.println("sign_before：" + si_md5Str);


            String signed = new String(Base64.encode(sign_before.getBytes(), Base64.DEFAULT));
            String url = String.format(QR_CODE_URL_RORMAT, "66", signed);
            System.out.println(url);
        }
}
