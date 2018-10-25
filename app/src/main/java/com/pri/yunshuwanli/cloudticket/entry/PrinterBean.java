package com.pri.yunshuwanli.cloudticket.entry;

import java.util.List;

public class PrinterBean {
    public PrinterBean(OrderInfo info, List<User.SpListBean> list) {
        this.info = info;
        this.list = list;
    }

    public OrderInfo info;
    public List<User.SpListBean> list;
}
