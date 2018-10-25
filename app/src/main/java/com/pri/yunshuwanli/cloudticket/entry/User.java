package com.pri.yunshuwanli.cloudticket.entry;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import yswl.com.klibrary.util.DateJsonDeserializer;

public class User implements Serializable{


    private String gsmc;
    private String kpdmc;
    private String skr;
    private String gsdm;
    private String appId;
    private String clientNo;
    private String key;
    private String isStartDWZ;
    private List<SpListBean> spList;

    public List<SpListBean> getShappingCount() {
        return shappingCount;
    }

    public void setShappingCount(List<SpListBean> shappingCount) {
        this.shappingCount = shappingCount;
    }

    //自增数据 购物车商品列表
    private List<SpListBean> shappingCount;

    /**
     * gsdm : hcz
     * appId : RJ10a9117ed9ae
     * clientNo : hcz001  //开票点代码
     * key : 6f20efe130923de16e1619b3087a6f4a
     * gsmc : 上海火车站南站
     * kpdmc : 火车站南广场停车场
     * skr : 万松
     ** isStartDWZ : 1
     * spList : [{"spdm":"A","spmc":"珠宝A","spdj":123.444,"sfmrsp":"1"},{"spdm":"B","spmc":"珠宝B","spdj":123.444,"sfmrsp":"0"}]
     */



    public static User jsonToUser(JSONObject json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateJsonDeserializer()).create();
        User user = gson.fromJson(json.toString(), new TypeToken<User>(){}.getType());
        return user;
    }


    public User() {
    }

    public String getGsmc() {
        return gsmc;
    }

    public void setGsmc(String gsmc) {
        this.gsmc = gsmc;
    }

    public String getKpdmc() {
        return kpdmc;
    }

    public void setKpdmc(String kpdmc) {
        this.kpdmc = kpdmc;
    }

    public String getSkr() {
        return skr;
    }

    public void setSkr(String skr) {
        this.skr = skr;
    }

    public String getGsdm() {
        return gsdm;
    }

    public void setGsdm(String gsdm) {
        this.gsdm = gsdm;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsStartDWZ() {
        return isStartDWZ;
    }

    public void setIsStartDWZ(String isStartDWZ) {
        this.isStartDWZ = isStartDWZ;
    }

    public List<SpListBean> getSpList() {
        return spList;
    }

    public void setSpList(List<SpListBean> spList) {
        this.spList = spList;
    }


    public static class SpListBean implements Serializable{
        /**
         * spdm : A //商品代码
         * spmc : 珠宝A  //商品名称
         * spdj : 123.444  //商品单价
         * sfmrsp : 1  //是否默认商品，1是，0否
         */


        private String spdm;
        private String spmc;
        private double spdj;
        private String sfmrsp;


        public int amount;//多选统计字段

        public double getReal_total() {
            return real_total;
        }

        public void setReal_total(double real_total) {
            this.real_total = real_total;
        }

        private double real_total;//真实总金额；

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        private double count;//数量

        public SpListBean(String spdm, String spmc, double spdj, String sfmrsp) {
            this.spdm = spdm;
            this.spmc = spmc;
            this.spdj = spdj;
            this.sfmrsp = sfmrsp;
        }

        public SpListBean() {
        }

        public String getSpdm() {
            return spdm;
        }

        public void setSpdm(String spdm) {
            this.spdm = spdm;
        }

        public String getSpmc() {
            return spmc;
        }

        public void setSpmc(String spmc) {
            this.spmc = spmc;
        }

        public double getSpdj() {
            return spdj;
        }

        public void setSpdj(double spdj) {
            this.spdj = spdj;
        }

        public String getSfmrsp() {
            return sfmrsp;
        }

        public void setSfmrsp(String sfmrsp) {
            this.sfmrsp = sfmrsp;
        }
    }
}
