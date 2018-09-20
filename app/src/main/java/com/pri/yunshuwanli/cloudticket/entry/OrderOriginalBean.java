package com.pri.yunshuwanli.cloudticket.entry;

public class OrderOriginalBean {
    public String taxpayer_name;//纳税人名字
    public String taxpayer_numb;//纳税人识别号
    public String date_start;//进场时间
    public String date_end;//离场时间
    public int car_type;//0 代表月租长包车辆， 1 代表时租访客车辆， 2 代表免费车辆， 3 代表异常未知车辆，其余数值预留

    public String last_parking_space;//剩余停车位总数量
    public String moth_last_parking_space;//月租剩余车位
    public String time_last_parking_space;//时租剩余车位

    public String carNo;//车牌号
    public String time;
    public String totalAmount;
    public String type_pay;

}