package com.pri.yunshuwanli.cloudticket.utils;


import android.util.Log;

import com.pri.yunshuwanli.cloudticket.logger.KLogger;

import java.io.UnsupportedEncodingException;

public class String2HexUtils {


    private static final String TAG = "String2HexUtils";

    /**
     * 将一组16进制流转换成汉字字符串
     *
     * @param hexString
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getChineseByHexString(String hexString) throws UnsupportedEncodingException {
        if (hexString.length() % 4 != 0) {
            KLogger.e(TAG,"16进制字符串不符合转码字符数要求");
            return null;
        }
        int size = hexString.length() / 4;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(getCharByHex(hexString.substring(i * 4, (i + 1) * 4)));
        }
        return result.toString();
    }

    /**
     * 将4个16进制转换成一个汉字
     *
     * @param hex
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getCharByHex(String hex) throws UnsupportedEncodingException {
        if (hex.length() != 4) {
            KLogger.e(TAG,"16进制不符合转码字符数要求");
            return null;
        }
        byte a0 = (byte) Integer.parseInt(hex.substring(0, 2), 16);
        byte a1 = (byte) Integer.parseInt(hex.substring(2), 16);
        byte[] gbk = new byte[]{a0, a1};
        return new String(gbk, "GBK");
    }


    public static void main(String args[]) throws UnsupportedEncodingException {
        String message = "BEA941313233343520202020";
        System.err.println(getChineseByHexString(message));
    }

}
