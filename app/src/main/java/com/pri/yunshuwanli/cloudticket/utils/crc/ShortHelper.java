package com.pri.yunshuwanli.cloudticket.utils.crc;

import java.util.ArrayList;
import java.util.List;

public class ShortHelper {

    /**
     * 这里首先将byte做升为成为short,因此，我们的算法中，统一采用short为最小单位。
     * byte = short  1位 最大值 256
     * short = int   2位 最大值 65536
     * int = long    4位 最大值 4294967296
     * @param
     * @return
     */

    public static short[] int2ShortArray(int n, boolean... arguments){
        boolean isRevers = false;
        if( arguments.length > 0){
            isRevers = arguments[0];
        }
        short[] res = shortArray(n, 2);
        if(isRevers){
            res = ShortHelper.arrayReverse(res);
        }
        return res;
    }

    public static List<Short> int2shortList(int n, boolean... arguments){
        boolean isRevers = false;
        if( arguments.length > 0){
            isRevers = arguments[0];
        }

        List<Short> resList = new ArrayList<Short>();
        for(short b : int2ShortArray(n, isRevers)){
            resList.add(b);
        }

        return resList;
    }

    public static short[] long2ShortArray(long n, boolean... arguments){
        boolean isRevers = false;
        if( arguments.length > 0){
            isRevers = arguments[0];
        }
        short[] res = shortArray(n, 4);
        if(isRevers){
            res = ShortHelper.arrayReverse(res);
        }
        return res;
    }


    public static List<Short> long2shortList(long n, boolean... argunments){
        boolean isRevers = false;
        if( argunments.length > 0){
            isRevers = argunments[0];
        }

        List<Short> resList = new ArrayList<Short>();
        for(short b : long2ShortArray(n, isRevers)){
            resList.add(b);
        }
        return resList;
    }

    public static List<Short> skipNShort(int n){
        List<Short> res = new ArrayList<Short>();
        for (int i = 0; i < n; i++){
            res.add((short)0);
        }
        return res;
    }

    public static short[] shortArray(long n, int b){
        short[] bArr = new short[b];
        for (int i = 0; i < b; i++){
            bArr[i] = (short)(n & 0xFF);
            n = n >> 8;
        }
        return bArr;
    }

    public static String shortArray2String(short[] nArr){
        StringBuilder sb = new StringBuilder();
        for(short n: nArr){
            sb.append(n);
        }
        return sb.toString();
    }

    public static short[] arrayReverse(short[] nArr){
        short[] newArr = new short[nArr.length];
        for (int i = 0; i < nArr.length; i++){
            newArr[i] = nArr[nArr.length - 1 - i];
        }
        return newArr;
    }

    public static void main(String[] args){

        System.out.println(ShortHelper.shortArray2String(ShortHelper.int2ShortArray(3600)));
    }
}
