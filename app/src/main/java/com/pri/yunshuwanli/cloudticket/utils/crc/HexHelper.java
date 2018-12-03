package com.pri.yunshuwanli.cloudticket.utils.crc;

import java.util.List;

public class HexHelper {

    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String shortsToHexString(List<Short> shorts){
        StringBuilder sb = new StringBuilder();
        for(short n : shorts){
            sb.append(short2HexString(n));
        }
        return sb.toString();
    }

    public static String short2HexString(short n){
        char[] hexChars = new char[2];
        int v = n & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }

    public static String byte2HexString(byte n){
        return short2HexString(n);
    }

    public static String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte n : bytes){
            sb.append(byte2HexString(n));
        }
        return sb.toString();
    }

    public static short hexStr2Short(char a, char b){

        return (short) (atIndex(a) * 16 + atIndex(b));
    }

    public static byte chars2byte(char a, char b){
        short s = hexStr2Short(a, b);
        return (byte)(s  & 0xFF);
    }


    public static int hexStr2Int(char[] hca, boolean isRevers){

        short a = hexStr2Short( hca[0], hca[1]);
        short b = hexStr2Short( hca[2], hca[3]);

        if(isRevers) {
            return a * 16 + b;
        } else {
            return b * 16 + a;
        }

    }

    public static long hexStr2Long(char[] hca, boolean isRevers){
        short a = hexStr2Short( hca[0], hca[1]);
        short b = hexStr2Short( hca[2], hca[3]);
        short c = hexStr2Short( hca[4], hca[5]);
        short d = hexStr2Short( hca[6], hca[7]);



        if(isRevers) {
//            return a * 4294967296 + b * 65536 + c * 256 + d;
            return b * 65536 + c * 256 + d;
        } else {
//            return d * 4294967296 + c * 65536 + b * 256 + a;
            return c * 65536 + b * 256 + a;
        }

    }

    public static int atIndex(char a){
        int index = 0;
        for(int i = 0; i < 16; i++){
            if (hexArray[i] == a){
                index = i;
            }
        }
        return index;
    }
}
