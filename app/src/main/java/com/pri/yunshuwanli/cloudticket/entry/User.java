package com.pri.yunshuwanli.cloudticket.entry;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import yswl.com.klibrary.util.DateJsonDeserializer;

public class User implements Serializable{

    /**
     * gsdm : hcz
     * appId : RJ10a9117ed9ae
     * clientNo : hcz001
     * key : 6f20efe130923de16e1619b3087a6f4a
     * gsmc : 上海火车站南站
     * kpdmc : 火车站南广场停车场
     * skr : 万松
     */

    private String gsdm;
    private String appId;
    private String clientNo;
    private String key;
    private String gsmc;
    private String kpdmc;
    private String skr;


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
}
