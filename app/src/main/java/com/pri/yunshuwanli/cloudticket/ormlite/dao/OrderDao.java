package com.pri.yunshuwanli.cloudticket.ormlite.dao;

import android.content.Context;
import android.text.format.DateUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pri.yunshuwanli.cloudticket.entry.OrderInfo;
import com.pri.yunshuwanli.cloudticket.ormlite.DatabaseHelper;
import com.pri.yunshuwanli.cloudticket.utils.DateUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDao {
    Context context;
    private Dao<OrderInfo, Integer> userDaoOpe;
    private DatabaseHelper helper;

    public OrderDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            userDaoOpe = helper.getDao(OrderInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public synchronized void add(OrderInfo orderInfo) {
        try {
//            List<OrderInfo> list = userDaoOpe.queryForMatching(orderInfo);
//            if (list != null && list.size() > 0) return;
            userDaoOpe.createIfNotExists(orderInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public synchronized void updata(OrderInfo orderInfo) {
        try {
            userDaoOpe.update(orderInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public synchronized void deleteNDayBefore(int day) {
        try {
            String dayS = DateUtil.getDate(day);

            DeleteBuilder<OrderInfo, Integer> builder = userDaoOpe.deleteBuilder();
            builder.where().le("orderDate", dayS); //le <  ge >
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<OrderInfo> queryOrderNDayBefore(int day) {
        String dayS = DateUtil.getDate(day);
        try {
            return userDaoOpe.queryBuilder().where().le("orderDate", dayS).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    //查询失败订单
    public List<OrderInfo> queryOrderInfoUpdataFail() {
        try {
            return userDaoOpe.queryBuilder().where().eq("isUpdate", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询时间段内的订单数据 单位天
    public List<OrderInfo> queryOrderOfTime(int day) {
        try {
            String today = DateUtil.getTodayDate();
            String dayS = DateUtil.getDate(day);
            QueryBuilder<OrderInfo, Integer> builder = userDaoOpe.queryBuilder();

            return builder.orderBy("orderDate", false).where().between("orderDate", dayS, today).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //按车牌查询订单数据
    public List<OrderInfo> queryOrderOfCarNo(String s) {
        try {
            return userDaoOpe.queryBuilder().where().eq("carNo", s).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<OrderInfo> queryMatching(OrderInfo orderInfo) {
        try {
            List<OrderInfo> list = userDaoOpe.queryForMatching(orderInfo);
            if (list != null && list.size() > 0) return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void delete(OrderInfo orderInfo) {
        try {
            List<OrderInfo> list = userDaoOpe.queryForMatching(orderInfo);
            if (list != null && list.size() > 0) {
                userDaoOpe.delete(orderInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<OrderInfo> queryAll() {
        try {
            return userDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
