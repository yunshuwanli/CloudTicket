package com.pri.yunshuwanli.cloudticket;

import com.pri.yunshuwanli.cloudticket.utils.SignUtil;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import yswl.com.klibrary.util.GsonUtil;

public class TestParams {



        @Test
        public void testParasMap(){
            Map<String,Object> data = new HashMap<>();
            data.put("orderNo","");
            data.put("quantity","");
            data.put("unitPrice","");
            data.put("totalAmount","");
            data.put("orderDate","");
            data.put("carNo","");
            data.put("remark","");
            Map<String,Object> params = new HashMap<>();
            params.put("appId","");
            params.put("reqType","");
            params.put("clientNo","");
            params.put("sign", SignUtil.getSignStr(data));
            params.put("data", data);

            System.out.println("params:"+GsonUtil.GsonString(params));
        }
}
