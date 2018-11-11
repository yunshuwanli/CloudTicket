package com.pri.yunshuwanli.cloudticket.utils;


import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
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
        String z_start = "AAA5281600000100020000000382";
        String content = "120B0A0C1509120B0A0F18090020015800DE00BEA94131323334352020202000002AE40023CA8003";
        String jyz = "7EA7CD";
        String crc = getCrc((z_start + content ).getBytes());
        String crc2 = getCRC((z_start + content ).getBytes());

        System.out.println("16进制校验字：" + crc);
        System.out.println("16进制校验字：" + crc2);

        String z_end = crc + "CD";
        String zhen = z_start + content + z_end;
        org = org.trim().replace(" ", "");
//        if (org.length() < 80) return;
//        String content = org.substring(28, org.length() - 6);
//        if (content.length() <= 0 || content.length() < 80) return;

//        System.out.println("截取：" + content);
//        String org2 = "120B08160507120B081705070064000A005A00BBA64131323334352020202000000E100000271000";
//        System.out.println("标本：" + org2);

//        String in_time = content.substring(0, 12);
//        String out_time = content.substring(12, 24);
//        String out_type = content.substring(24, 26);
//        String postion_count_total = content.substring(26, 30);
//        String postion_count_month = content.substring(30, 34);
//        String postion_count_time = content.substring(34, 38);
//        String car_no = content.substring(38, 62);
//        String time = content.substring(62, 70);
//        String total = content.substring(70, 78);
//        String pay_type = content.substring(78, 80);
//        System.out.println("in_time " + in_time);
//        System.out.println("out_time " + out_time);
//        System.out.println("out_type " + out_type);
//        System.out.println("postion_count_total " + postion_count_total);
//        System.out.println("postion_count_month " + postion_count_month);
//        System.out.println("postion_count_time " + postion_count_time);
//        System.out.println("car_no " + car_no);
//        System.out.println("time " + time);
//        System.out.println("total " + total);
//        System.out.println("pay_type " + pay_type);

//        System.out.println("进场时间" + getDecimalForHexFull2(in_time));
//        System.out.println("离场时间" + getDecimalForHexFull2(out_time));
//        System.out.println("离场类型" + decimal(out_type));
//
//        System.out.println("总剩余车位数量" + getDecimalForHexFull(postion_count_total));
//        System.out.println("月租剩余车位数量" + getDecimalForHexFull(postion_count_month));
//        System.out.println("时刻剩余车位数量" + getDecimalForHexFull(postion_count_time));
//        System.out.println("车牌号" + decodeGBK(car_no));
//        System.out.println("时长" + decimal(time));
//        System.out.println("金额" + decimal(total));
//        System.out.println("支付类型" + decimal(pay_type));

//        String zh_shr = "京A12345";
//        System.out.println(ecodeGBK(zh_shr));

        //目标0200


        System.out.println(hex("0200"));
        System.out.println(hex("0201"));
        System.out.println(getCrc("0200".getBytes()));
        System.out.println(getCrc("0200".getBytes()));


    }

    private static String getCrc(byte[] data) {
        int high;
        int flag;

        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }

        return Integer.toHexString(wcrc);
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }

    public static String getCRC2(byte[] bytes) {
//        ModBus 通信协议的 CRC ( 冗余循环校验码含2个字节, 即 16 位二进制数。
//        CRC 码由发送设备计算, 放置于所发送信息帧的尾部。
//        接收信息设备再重新计算所接收信息 (除 CRC 之外的部分）的 CRC,
//        比较计算得到的 CRC 是否与接收到CRC相符, 如果两者不相符, 则认为数据出错。
//
//        1) 预置 1 个 16 位的寄存器为十六进制FFFF(即全为 1) , 称此寄存器为 CRC寄存器。
//        2) 把第一个 8 位二进制数据 (通信信息帧的第一个字节) 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器。
//        3) 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位。
//        4) 如果移出位为 0, 重复第 3 步 ( 再次右移一位); 如果移出位为 1, CRC 寄存器与多项式A001 ( 1010 0000 0000 0001) 进行异或。
//        5) 重复步骤 3 和步骤 4, 直到右移 8 次,这样整个8位数据全部进行了处理。
//        6) 重复步骤 2 到步骤 5, 进行通信信息帧下一个字节的处理。
//        7) 将该通信信息帧所有字节按上述步骤计算完成后,得到的16位CRC寄存器的高、低字节进行交换。
//        8) 最后得到的 CRC寄存器内容即为 CRC码。

        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= (int) bytes[i];
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) == 1) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //高低位转换，看情况使用（譬如本人这次对led彩屏的通讯开发就规定校验码高位在前低位在后，也就不需要转换高低位)
        //CRC = ( (CRC & 0x0000FF00) >> 8) | ( (CRC & 0x000000FF ) << 8);
        return Integer.toHexString(CRC);
    }

    public static String getDecimalForHexFull(String data) {
        StringBuilder sb = new StringBuilder();
        while (data.length() >= 2) {
            String curr = data.substring(0, 2);
            String dec = decimal(curr);
            if (!dec.equals("0")) {
                sb.append(dec);
            }

            data = data.substring(2);
        }
        return sb.toString();
    }

    public static String getDecimalForHexFull2(String data) {
        String hexData = data;
        StringBuilder sb = new StringBuilder();
        while (hexData.length() >= 2) {
            String curr = hexData.substring(0, 2);
            String dec = decimal(curr);
            if (dec.length() < 2) {
                sb.append("0");

            }
            sb.append(dec);
            hexData = hexData.substring(2);
        }
        return sb.toString();
    }

    public static String ecodeGBK(String source) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = source.getBytes("GB2312");
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
        }

        return sb.toString();
    }

    public static String decimal(String hexStr) {
        int valueTen = Integer.parseInt(hexStr, 16);
        return String.valueOf(valueTen);
    }

    public static String hex(String hexStr) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = hexStr.getBytes();
        for (byte b : bytes) {
            sb.append(Integer.toHexString((b & 0xff)).toUpperCase());
        }

        return sb.toString();
    }


    /**
     * 从socket流解析一个bean
     *
     * @return
     */
    public static OrderInfo getOrderInroForOrg(String content) {
        OrderInfo info = new OrderInfo();
        content = content.trim().replace(" ", "");
        if (content.length() < 80) return null;
        content = content.substring(28, content.length() - 6);

        String in_time = content.substring(0, 12);
        String out_time = content.substring(12, 24);
        String out_type = content.substring(24, 26);
        String postion_count_total = content.substring(26, 30);
        String postion_count_month = content.substring(30, 34);
        String postion_count_time = content.substring(34, 38);
        String car_no = content.substring(38, 62);
        String time = content.substring(62, 70);
        String total = content.substring(70, 78);
        String pay_type = content.substring(78, 80);

//        System.out.println("进场时间" + getDecimalForHexFull2(in_time));
//        System.out.println("离场时间" + getDecimalForHexFull2(out_time));
//        System.out.println("离场类型" + decimal(out_type));
//
//        System.out.println("总剩余车位数量" + getDecimalForHexFull(postion_count_total));
//        System.out.println("月租剩余车位数量" + getDecimalForHexFull(postion_count_month));
//        System.out.println("时刻剩余车位数量" + getDecimalForHexFull(postion_count_time));
//        System.out.println("车牌号" + decodeGBK(car_no));
//        System.out.println("时长" + decimal(time));
//        System.out.println("金额" + decimal(total));
//        System.out.println("支付类型" + decimal(pay_type));
        info.setOrderNo(OrderInfo.getOderID());
        info.setOrderDate(getDecimalForHexFull2(out_time));
        double price = Double.valueOf(decimal(total));
        info.setTotalAmount(price);
        info.setCarNo(decodeGBK(car_no));
        return info;
    }
}
