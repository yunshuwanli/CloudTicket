package com.pri.yunshuwanli.cloudticket.utils;


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
            KLogger.e(TAG, "16进制字符串不符合转码字符数要求");
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
     * 或转换成两个英文
     *
     * @param hex
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getCharByHex(String hex) throws UnsupportedEncodingException {
        if (hex.length() != 4) {
            KLogger.e(TAG, "16进制不符合转码字符数要求");
            return null;
        }
        byte a0 = (byte) Integer.parseInt(hex.substring(0, 2), 16);
        byte a1 = (byte) Integer.parseInt(hex.substring(2), 16);
        byte[] gbk = new byte[]{a0, a1};
        return new String(gbk, "GBK");
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String decodeGBK(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "GB2312");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static void main(String args[]) throws UnsupportedEncodingException {
        String org = "AAA5 28 0100 00010002 00000003 82 120B08160507 120B08170507 00 64000A005A00  BBA641313233343520202020 00000E100000271000C0F4CD";
        org = org.trim().replace(" ", "");
        if (org.length() < 0 || org.length() < 32) return;
        String content = org.substring(28, org.length() - 6);
        if (content.length() <= 0 || content.length() < 80) return;

        System.out.println("截取：" + content);
        String org2 = "120B08160507120B081705070064000A005A00BBA64131323334352020202000000E100000271000";
        System.out.println("标本：" + org2);

        String in_time = content.substring(0, 12);
        String out_time = content.substring(12, 24);
        String out_type = content.substring(24, 26);
        String postion_count = content.substring(26, 38);
        String car_no = content.substring(38, 62);
        String time = content.substring(62, 70);
        String total = content.substring(70, 78);
        String pay_type = content.substring(78, 80);
        System.out.println("in_time " + in_time);
        System.out.println("out_time " + out_time);
        System.out.println("out_type " + out_type);
        System.out.println("postion_count " + postion_count);
        System.out.println("car_no " + car_no);
        System.out.println("time " + time);
        System.out.println("total " + total);
        System.out.println("pay_type " + pay_type);


        String message = "BEA941313233343520202020";
        System.out.println("进场时间"+decodeGBK(in_time));
        System.out.println("离场时间"+decodeGBK(out_time));
        System.out.println("离场类型"+decodeGBK(out_type));
        System.out.println("剩余车位数量"+decodeGBK(postion_count));
        System.out.println("车牌号"+decodeGBK(car_no));
        System.out.println("时长"+decodeGBK(time));
        System.out.println("金额"+decodeGBK(total));
        System.out.println("支付类型"+decodeGBK(pay_type));

        String zh_shr = "京A12345";
        System.out.println(ecodeGBK(zh_shr));
    }

    public static String ecodeGBK(String source) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = source.getBytes("GB2312");
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
        }

        return sb.toString();
    }


}
