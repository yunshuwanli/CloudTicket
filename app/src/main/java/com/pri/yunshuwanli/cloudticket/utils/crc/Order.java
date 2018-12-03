package com.pri.yunshuwanli.cloudticket.utils.crc;

import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;

import java.util.Calendar;

public class Order {

    private int id;

    // 厂商编号
    private int vendorNumber;

    // 地址编码
    private int addressNumber;

    // 设备编码
    private long deviceNumber;

    // 进入时间
    private Calendar enterTime;

    // 离开时间
    private Calendar leaveTime;

    // 离场类型：0"月租长包车辆",1"时租访客车辆",2"免费车辆",3"异常未知车辆"
    private int leaveType;

    // 总剩余车位
    private int totallyRemainedCount;

    // 月租剩余车位
    private int monthlyRemainedCount;

    // 时租剩余车位
    private int hourlyRemainedCount;

    // 车牌号
    private String plateNumber;

    // 停车时长
    private long parkingTime;

    // 停车金额
    private long payAmount;

    // 支付类型："0 现金支付","1 交通卡支付","2 银行卡支付","3 手机支付"
    private int payType;



    public int getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(int vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public long getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(long deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public Calendar getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Calendar enterTime) {
        this.enterTime = enterTime;
    }

    public Calendar getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Calendar leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(int leaveType) {
        this.leaveType = leaveType;
    }

    public int getTotallyRemainedCount() {
        return totallyRemainedCount;
    }

    public void setTotallyRemainedCount(int totallyRemainedCount) {
        this.totallyRemainedCount = totallyRemainedCount;
    }

    public int getMonthlyRemainedCount() {
        return monthlyRemainedCount;
    }

    public void setMonthlyRemainedCount(int monthlyRemainedCount) {
        this.monthlyRemainedCount = monthlyRemainedCount;
    }

    public int getHourlyRemainedCount() {
        return hourlyRemainedCount;
    }

    public void setHourlyRemainedCount(int hourlyRemainedCount) {
        this.hourlyRemainedCount = hourlyRemainedCount;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public long getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(long parkingTime) {
        this.parkingTime = parkingTime;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id = ").append(this.id).append("\n");
        sb.append("vendorNumber = ").append(this.getVendorNumber()).append("\n");
        sb.append("addressNumber = ").append(this.addressNumber).append("\n");
        sb.append("deviceNumber = ").append(this.deviceNumber).append("\n");
        sb.append("enterTime = ").append(this.enterTime.toString()).append("\n");
        sb.append("leaveTime = ").append(this.leaveTime.toString()).append("\n");
        sb.append("leaveType = ").append(this.leaveType).append("\n");
        sb.append("totallyRemainedCount = ").append(this.totallyRemainedCount).append("\n");
        sb.append("monthlyRemainedCount = ").append(this.monthlyRemainedCount).append("\n");
        sb.append("hourlyRemainedCount = ").append(this.hourlyRemainedCount).append("\n");
        sb.append("plateNumber = ").append(this.plateNumber).append("\n");
        sb.append("parkingTime = ").append(this.parkingTime).append("\n");
        sb.append("payAmount = ").append(this.payAmount).append("\n");
        sb.append("payType = ").append(this.payType).append("\n");
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
