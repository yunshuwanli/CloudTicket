package com.pri.yunshuwanli.cloudticket.utils;

import android.content.Context;

import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;

import java.util.ArrayList;
import java.util.List;

public class DataFactory {

    public static List<OrderInfo> saveListData(Context context) {
        List<OrderInfo> list = new ArrayList<>();
        OrderDao dao = new OrderDao(context);
        OrderInfo info = null;
        for (int i = 0; i < 500; i++) {

            info = new OrderInfo("test_MA241560" + i, 10.00 + i,
                    "2018-09-18 23:59:59", "沪C888" + i,"wu");
            dao.add(info);
            info = new OrderInfo("test_MB241560" + i, 10.00 + i,
                    "2018-09-17 23:59:59", "沪A88888","wu");
            dao.add(info);
            info = new OrderInfo("test_MC241560" + i, 10.00 + i,
                    "2018-09-16 23:59:59", "浙A33333","wu");
            dao.add(info);
        }

        return list;
    }


    public static List<OrderInfo> saveListData2(Context context) {
        List<OrderInfo> list = new ArrayList<>();
        OrderDao dao = new OrderDao(context);
        OrderInfo info = null;

            info = new OrderInfo("test_1", 10.00 ,
                    "2018-09-17 23:59:59", "沪C88812","wu");
            dao.add(info);
           info = new OrderInfo("test_2", 10.00 ,
                "2018-09-16 23:59:59", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_3", 10.00 ,
                "2018-09-15 23:59:59", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_4", 10.00 ,
                "2018-09-18 00:30:00", "沪C88812","wu");
        dao.add(info);

        info = new OrderInfo("test_5", 10.00 ,
                "2018-09-18 00:00:00", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_6", 10.00 ,
                "2018-09-18 23:59:59", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_7", 10.00 ,
                "2018-09-18 23:59:59", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_8", 10.00 ,
                "2018-09-18 23:59:59", "沪C88812","wu");
        dao.add(info);

        info = new OrderInfo("test_9", 10.00 ,
                "2018-09-18 23:59:59", "沪C88812","wu");
        dao.add(info);
        info = new OrderInfo("test_10", 10.00 ,
                "2018-09-18 23:59:59", "沪C88812","wu");
        dao.add(info);
        return list;
    }
}
