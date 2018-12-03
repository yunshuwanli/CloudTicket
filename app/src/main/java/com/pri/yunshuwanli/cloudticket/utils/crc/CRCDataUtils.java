package com.pri.yunshuwanli.cloudticket.utils.crc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CRCDataUtils {

    final byte byte170 = (byte)170;

    /**
     * 解码方法
     * @param hexStr
     * @return
     */
    public static Order decode(String hexStr) throws YwxException {

        char[] hexChars = hexStr.toCharArray();
        Order o = new Order();


        char[] idChars = Arrays.copyOfRange(hexChars, 6, 10);
        o.setId( HexHelper.hexStr2Int( idChars, false ) );

        char[] vendorNumberChars = Arrays.copyOfRange(hexChars, 10, 14);
        o.setVendorNumber( HexHelper.hexStr2Int( vendorNumberChars, true ) );

        char[] addressNumberChars = Arrays.copyOfRange(hexChars, 14, 18);
        o.setAddressNumber( HexHelper.hexStr2Int( addressNumberChars, true ) );

        char[] deviceNumber = Arrays.copyOfRange(hexChars, 18, 26);
        o.setDeviceNumber( HexHelper.hexStr2Long( deviceNumber, true ) );

        char[] enterTime = Arrays.copyOfRange(hexChars, 28, 40);
        o.setEnterTime(charArr2Time(enterTime));
        char[] leaveTime = Arrays.copyOfRange(hexChars, 40, 52);
        o.setLeaveTime(charArr2Time(leaveTime));

        o.setLeaveType(HexHelper.hexStr2Short(hexChars[52], hexChars[53]));

        char[] totallyRemainedCountChar = Arrays.copyOfRange(hexChars, 54, 58);
        o.setTotallyRemainedCount( HexHelper.hexStr2Int( totallyRemainedCountChar, false ) );
        char[] monthlyRemainedCountChar = Arrays.copyOfRange(hexChars, 58, 62);
        o.setMonthlyRemainedCount( HexHelper.hexStr2Int( monthlyRemainedCountChar, false ) );
        char[] hourlyRemainedCountChar = Arrays.copyOfRange(hexChars, 62, 66);
        o.setHourlyRemainedCount( HexHelper.hexStr2Int( hourlyRemainedCountChar, false ) );

        char[] plateNumberChars = Arrays.copyOfRange(hexChars, 66, 90);
        o.setPlateNumber(chars2plateNumber(plateNumberChars));

        char[] parkingTimeChars = Arrays.copyOfRange(hexChars, 90, 98);
        o.setParkingTime( HexHelper.hexStr2Long( parkingTimeChars, true ) );

        char[] payAmountChars = Arrays.copyOfRange(hexChars, 98, 106);
        o.setPayAmount( HexHelper.hexStr2Long( payAmountChars, true ) );

        o.setPayType(HexHelper.hexStr2Short(hexChars[106], hexChars[107]));

        if(!securityValdation(hexStr, o)){
            throw new YwxException(2, "接收数据校验位验证失败");
        }

        return o;
    }

    /**
     * 安全位验证
     * @param hexStr
     * @param o
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean securityValdation(String hexStr, Order o) throws YwxException {
        String yz = encode(o);
        return hexStr.equals(yz);
    }

    /**
     * 编码方法
     * @return
     */
    public static String encode(Order sendData) throws YwxException {

        List<Short> resList = new ArrayList<Short>();
        resList.add((short) 170);
        resList.add((short) 165);
        resList.add((short) 0);

        resList.addAll(ShortHelper.int2shortList(sendData.getId()));
        resList.addAll(ShortHelper.int2shortList(sendData.getVendorNumber(), true)); // 写入 厂商编号字节数组（字节被翻转了）
        resList.addAll(ShortHelper.int2shortList(sendData.getAddressNumber(), true)); // 写入 地址编码 字节数组（字节被翻转了）
        resList.addAll(ShortHelper.long2shortList(sendData.getDeviceNumber(), true)); // 写入 设备编码字节数组（字节被翻转了）

        resList.add((short) 130);

        resList.addAll(CRCDataUtils.timeResolve(sendData.getEnterTime()));      // 进场时间
        resList.addAll(CRCDataUtils.timeResolve(sendData.getLeaveTime()));      // 出场时间
        resList.add((short)sendData.getLeaveType());    //离开类别,可能为0，1，2，3
        resList.addAll(ShortHelper.int2shortList(sendData.getTotallyRemainedCount())); // 剩余车辆 使用ushort定长2字节
        resList.addAll(ShortHelper.int2shortList(sendData.getMonthlyRemainedCount())); // 剩余月租车位 使用ushort定长
        resList.addAll(ShortHelper.int2shortList(sendData.getHourlyRemainedCount())); // // 剩余时租车位 使用ushort定长

        List<Short> pnList = new ArrayList<Short>(12);
        byte[] plateNumberBytes = null;
        try {
            plateNumberBytes = sendData.getPlateNumber().getBytes("GBK");
            for (int i = 0; i < 12; i++) {
                if(i < plateNumberBytes.length){
                    pnList.add((short)(plateNumberBytes[i] & 0xFF));
                } else {
                    pnList.add((short)32);    // 空格
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new YwxException(3, "车牌号编码失败");
        }

        resList.addAll(pnList);

        resList.addAll(ShortHelper.long2shortList(sendData.getParkingTime(), true));   // 停车时长，翻转 4字节
        resList.addAll(ShortHelper.long2shortList(sendData.getPayAmount(), true));      // 支付金额翻转，翻转 4字节
        resList.add((short)sendData.getPayType());// 支付方式
        resList.addAll(ShortHelper.skipNShort(2));
        resList.add((short)205);

        int length = resList.size() - 17;
        resList.set(2, (short)length);


        // 验证字段
        short[] securityShorts = ShortHelper.int2ShortArray(CrcHelper.CalcCrc16(resList, 0, resList.size() - 3));
        resList.set(resList.size() - 3, securityShorts[0]);
        resList.set(resList.size() - 2, securityShorts[1]);

        return HexHelper.shortsToHexString(resList);
    }

    /**
     * 编码方法
     * @return
     */
    public static String encodeResult(Order sendData,int errCode) throws YwxException {

        List<Short> resList = new ArrayList<Short>();
        resList.add((short) 170);
        resList.add((short) 165);
        resList.add((short) 0);

        resList.addAll(ShortHelper.int2shortList(sendData.getId()));
        resList.addAll(ShortHelper.int2shortList(sendData.getVendorNumber(), true)); // 写入 厂商编号字节数组（字节被翻转了）
        resList.addAll(ShortHelper.int2shortList(sendData.getAddressNumber(), true)); // 写入 地址编码 字节数组（字节被翻转了）
        resList.addAll(ShortHelper.long2shortList(sendData.getDeviceNumber(), true)); // 写入 设备编码字节数组（字节被翻转了）

        resList.add((short) 130);

        resList.add((short)2);    //回应类型固定 2
        resList.add((short)errCode);    //错误代码0,1
        resList.add((short)205);
        int length = resList.size() - 17;
        resList.set(2, (short)length);


        // 验证字段
        short[] securityShorts = ShortHelper.int2ShortArray(CrcHelper.CalcCrc16(resList, 0, resList.size() - 3));
        resList.set(resList.size() - 3, securityShorts[0]);
        resList.set(resList.size() - 2, securityShorts[1]);

        return HexHelper.shortsToHexString(resList);
    }

    public static List<Short> timeResolve(Calendar c){
        List<Short> res = new ArrayList<Short>();
        res.add((short)(c.get(Calendar.YEAR) - 2000));
        res.add((short)(c.get(Calendar.MONTH) + 1));
        res.add((short)c.get(Calendar.DAY_OF_MONTH));
        res.add((short)c.get(Calendar.HOUR_OF_DAY));
        res.add((short)c.get(Calendar.MINUTE));
        res.add((short)c.get(Calendar.SECOND));
        return res;
    }

    public static Calendar charArr2Time(char[] time){
        short year = HexHelper.hexStr2Short(time[0], time[1]);
        short month = HexHelper.hexStr2Short(time[2], time[3]);
        short day = HexHelper.hexStr2Short(time[4], time[5]);

        short hour = HexHelper.hexStr2Short(time[6], time[7]);
        short minute = HexHelper.hexStr2Short(time[8], time[9]);
        short second = HexHelper.hexStr2Short(time[10], time[11]);

        Calendar c = Calendar.getInstance();
        c.set(2000 + year, month - 1, day, hour, minute, second);
        return c;
    }

    public static String chars2plateNumber(char[] pn) throws YwxException {
        byte[] bytes = new byte[pn.length / 2];
        for(int i =0; i < pn.length ; i++){
            bytes[i / 2] =HexHelper.chars2byte(pn[i], pn[++i]);
        }
        String res = null;
        try {
            res = new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new YwxException(4, "车牌号解码失败");
        }
        return res;
    }

    public static void main(String[] args) throws YwxException {

        Order o = new Order();
        o.setId(1);
        o.setVendorNumber(1);
        o.setAddressNumber(2);
        o.setDeviceNumber(3);
        Calendar enterTime = Calendar.getInstance();
        enterTime.set(2018, 10, 8, 22, 5, 7);
        o.setEnterTime(enterTime);
        Calendar leaveTime = Calendar.getInstance();
        leaveTime.set(2018, 10, 8, 23, 5, 7);
        o.setLeaveTime(leaveTime);
        o.setLeaveType(0);
        o.setTotallyRemainedCount(100);
        o.setMonthlyRemainedCount(10);
        o.setHourlyRemainedCount(90);
        o.setPlateNumber("沪A12345");
        o.setParkingTime(3600);
        o.setPayAmount(10000);
        o.setPayType(0);

        //AA A5 28 0100 0001 0002 00000003 82 12 0B 08 16 05 07 12 0B 08 17 05 07 00 6400 0A00 5A00 BBA641313233343520202020 00000E10 00002710 00 C0F4 CD
        //AA A5 28 0100 0001 0002 00000003 82 12 0B 08 16 05 07 12 0B 08 17 05 07 00 6400 0A00 5A00 BBA641313233343520202020 00000E10 00002710 00 C0F4 CD
        System.out.println(CRCDataUtils.encode(o));

        Order o2 = decode("AAA5280100000100020000000382120B08160507120B081705070064000A005A00BBA64131323334352020202000000E100000271000C0F4CD");
        System.out.println(o2.toString());
    }
}
