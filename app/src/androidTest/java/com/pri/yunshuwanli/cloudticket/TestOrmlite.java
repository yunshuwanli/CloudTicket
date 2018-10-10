package com.pri.yunshuwanli.cloudticket;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.ormlite.dao.OrderDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import yswl.com.klibrary.util.GsonUtil;
import yswl.com.klibrary.util.L;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestOrmlite extends AndroidTestCase {

    @Test
    public void addOrderInfo() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

//
//        //TODO TEST DATA
//        OrderDao dao = new OrderDao(appContext);
//        OrderInfo info = new OrderInfo("No_112132",
//                10.00, "2018-09-20 12:00:00", "ASD");
//        OrderInfo info2 = new OrderInfo("No_112133",
//                10.00, "2018-09-20 12:00:00", "DCF");
//        OrderInfo info3 = new OrderInfo("NO_112134",
//                10.00, "2018-09-20 12:00:00", "AAA");
//        dao.add(info);
//        dao.add(info2);
//        dao.add(info3);
//        dao.add(info3);
//        L.e( GsonUtil.GsonString(dao.queryAll()));
//        List<OrderInfo > list = dao.queryOrderOfCarNo("AAA");
//        if(list!=null){
//            OrderInfo orderInfo = list.get(0);
//            orderInfo.setCarNo("BBB");
//            dao.updata(orderInfo);
//        }
//        L.e( GsonUtil.GsonString( dao.queryAll()));
    }
}
