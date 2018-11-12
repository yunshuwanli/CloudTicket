package com.pri.yunshuwanli.cloudticket;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 *
 * CRC数组处理工具类及数组合并
 */
public class CrcUtil {
    /**
     * 测试方法不能static 不能有参
     */
    @Test
    public void test() throws UnsupportedEncodingException {
         byte[] data = {(byte)0xAA,0x0C,0x01,0x00,0x01,0x00,0x00,0x04,0x05,0x17,0x05,0x01,(byte)0xA0,(byte)0x86,0x01,0x00};

        byte[] crcData3 = CrcUtil.setParamCRC(data);
        if(CrcUtil.isPassCRC(crcData3, 2)){
            System.out.println("3验证通过");
        }else{
            System.out.println("3验证失败");
        }

        String z_start = "AAA5280000000100020000000382";
        String content = "120B09151A16120B0A001D160020015800DE00BEA94131323334352020202000002AE40023CA8003";
        String jyz = "4440";
        //十六进制转byte
        byte[]  tes = hexStringToBytes(z_start+content);
        System.out.println("2验证byte:" +new String(tes,"utf-8"));
        byte[] crcData2 = CrcUtil.setParamCRC(tes);
        for(int i = 0;i<tes.length;i++){
            if(CrcUtil.isPassCRC(crcData2, i)){
                System.out.println(i+"验证通过");
            }else{
                System.out.println(i+"验证失败");
            }
        }

        byte[] crcData = CrcUtil.setParamCRC((z_start+content).getBytes());
        System.out.println("验证byte:" +(z_start+content));
        if(CrcUtil.isPassCRC(crcData, 3)){
            System.out.println("验证通过");
        }else{
            System.out.println("验证失败");
        }

//        String z_end = CRC16M.getBufHexStr(sbuf);
//        System.out.println("校验字："+new String(sbuf));
//        System.out.println("校验字2："+z_end);
//        String zhen = z_start + content + z_end;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    /**
     * 为Byte数组最后添加两位CRC校验
     * @param buf（验证的byte数组）
     * @return
     */
    public static byte[] setParamCRC(byte[] buf) {
        int checkCode = 0;
        checkCode = crc_16_CCITT_False(buf, buf.length);
        byte[] crcByte = new byte[2];
        crcByte[0] = (byte) ((checkCode >> 8) & 0xff);
        crcByte[1] = (byte) (checkCode & 0xff);
        // 将新生成的byte数组添加到原数据结尾并返回
        return concatAll(buf, crcByte);
    }
 
    /**
     * CRC-16/CCITT-FALSE x16+x12+x5+1 算法
     *
     * info
     * Name:CRC-16/CCITT-FAI
     * Width:16
     * Poly:0x1021
     * Init:0xFFFF
     * RefIn:False
     * RefOut:False
     * XorOut:0x0000
     * @param bytes
     * @param length
     * @return
     */
    public static int crc_16_CCITT_False(byte[] bytes, int length) {
        int crc = 0xffff; // initial value
        int polynomial = 0x1021; // poly value
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        //输出String字样的16进制
        String strCrc = Integer.toHexString(crc).toUpperCase(); 
        System.out.println(strCrc);
        return crc;
    }
 
    /***
     * CRC校验是否通过
     * @param srcByte
     * @param length(验证码字节长度)
     * @return
     */
    public static boolean isPassCRC(byte[] srcByte, int length) {
 
        // 取出除crc校验位的其他数组，进行计算，得到CRC校验结果
        int calcCRC = calcCRC(srcByte, 0, srcByte.length - length);
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((calcCRC >> 8) & 0xff);
        bytes[1] = (byte) (calcCRC & 0xff);
 
        // 取出CRC校验位，进行计算
        int i = srcByte.length;
        byte[] b = { srcByte[i - 2] ,srcByte[i - 1] };
 
        // 比较
        return bytes[0] == b[0] && bytes[1] == b[1];
    }
 
    /**
     * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
     * @param  buf
     * @param crcLen
     */
    private static int calcCRC(byte[] buf, int offset, int crcLen) {
        int start = offset;
        int end = offset + crcLen;
        int crc = 0xffff; // initial value
        int polynomial = 0x1021;
        for (int index = start; index < end; index++) {
            byte b = buf[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }
 
    /**
     * 多个数组合并
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}