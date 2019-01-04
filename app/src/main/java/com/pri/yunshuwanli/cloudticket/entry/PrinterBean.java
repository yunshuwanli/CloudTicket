package com.pri.yunshuwanli.cloudticket.entry;

import com.pri.yunshuwanli.cloudticket.utils.DateUtil;

import java.util.List;

import yswl.com.klibrary.http.HttpClientProxy;

public class PrinterBean {
    public PrinterBean(OrderInfo info, List<User.SpListBean> list) {
        this.info = info;
        this.list = list;
    }

    public OrderInfo info;
    public List<User.SpListBean> list;


    public static PrinterBean creatOrderDetail(List<User.SpListBean> list) {
        OrderInfo info = new OrderInfo();
        info.setOrderNo(OrderInfo.getOderID());
        info.setOrderDate(DateUtil.getTodayDate3());
        PrinterBean bean = new PrinterBean(info, list);
        return bean;
    }
}
