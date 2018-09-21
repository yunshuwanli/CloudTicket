package com.pri.yunshuwanli.cloudticket.entry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import yswl.com.klibrary.util.DateJsonDeserializer;

@DatabaseTable(tableName = "tb_orderInfo")
public class OrderInfo implements Parcelable {

    public static ArrayList<OrderInfo> jsonToList(JSONArray jsonArray) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateJsonDeserializer()).create();
        ArrayList<OrderInfo> list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<OrderInfo>>(){}.getType());
        return list;
    }


    public OrderInfo(String orderNo, double totalAmount, String orderDate, String carNo) {
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.carNo = carNo;
    }

    //ormlite必须提供无参构造
    public OrderInfo() {
    }

    /**
     * orderNo : test_ME24156071
     * totalAmount : 1170.00
     * orderDate  : 2018-06-22 23:59:59
     * carNo : 沪 A88888
     * remark : 某某场库,停车时间 201809061433-201809061533 共计一小时
     */


    @DatabaseField(generatedId = true)
    private int id; //

    //数据库自增长id 也作为requestId
    public int getId() {
        return id;
    }

    @DatabaseField
    private String orderNo;//订单号
    @DatabaseField
    private double totalAmount;//总金额
    @DatabaseField
    private String orderDate;//日期
    @DatabaseField
    private String carNo;//车牌号
    @DatabaseField
    private String remark;//备注
    @DatabaseField
    private String payType = "其他";//支付方式 统一为其他
    @DatabaseField
    private boolean isUpdate;//本订单是否上传成功
    @DatabaseField
    private boolean printStatue;//本订单是否打印过
    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isPrintStatue() {
        return printStatue;
    }public String getprintStatue() {
        String statue = printStatue?"已打印":"未打印";
        return statue;
    }

    public void setPrintStatue(boolean printStatue) {
        this.printStatue = printStatue;
    }



    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.orderNo);
        dest.writeDouble(this.totalAmount);
        dest.writeString(this.orderDate);
        dest.writeString(this.carNo);
        dest.writeString(this.remark);
        dest.writeString(this.payType);
        dest.writeByte(this.isUpdate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.printStatue ? (byte) 1 : (byte) 0);
    }

    protected OrderInfo(Parcel in) {
        this.id = in.readInt();
        this.orderNo = in.readString();
        this.totalAmount = in.readDouble();
        this.orderDate = in.readString();
        this.carNo = in.readString();
        this.remark = in.readString();
        this.payType = in.readString();
        this.isUpdate = in.readByte() != 0;
        this.printStatue = in.readByte() != 0;
    }

    public static final Parcelable.Creator<OrderInfo> CREATOR = new Parcelable.Creator<OrderInfo>() {
        @Override
        public OrderInfo createFromParcel(Parcel source) {
            return new OrderInfo(source);
        }

        @Override
        public OrderInfo[] newArray(int size) {
            return new OrderInfo[size];
        }
    };
}
