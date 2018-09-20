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

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestOrmlite extends AndroidTestCase {

    @Test
    public void addOrderInfo() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();


        OrderDao dao = new OrderDao(appContext);
        dao.add(new OrderInfo("test_ME24156071", " 1170.00",
                "2018-06-22 23:59:59", "沪 A88888",
                "某某场库,停车时间 201809061433-201809061533 共计一小时"));

        List<OrderInfo> list = dao.queryAll();
    }
}
