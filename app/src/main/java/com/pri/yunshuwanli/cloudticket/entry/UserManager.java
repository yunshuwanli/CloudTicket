package com.pri.yunshuwanli.cloudticket.entry;

import com.pri.yunshuwanli.cloudticket.App;
import com.pri.yunshuwanli.cloudticket.Contant;


import yswl.com.klibrary.http.okhttp.MSPUtils;

import static com.pri.yunshuwanli.cloudticket.Contant.SP_File_NANE;
import static com.pri.yunshuwanli.cloudticket.Contant.UUID;

public class UserManager {

    public static volatile User user;

    public static User getUser() {
        if (user == null) {
            Object obj = MSPUtils.StrToObj(MSPUtils.getString(Contant.SP_File_NANE,Contant.USER_INFO, null));
            if (obj instanceof User) {
                user = (User) obj;
            }
        }
        return user;
    }

    public static void setUid(String uid){
        MSPUtils.put(Contant.SP_File_NANE,Contant.UUID,uid);
    }
    public static void clear(){
        MSPUtils.clear(App.getApplication(),Contant.SP_File_NANE);
    }

    public static String getUID(){
       return MSPUtils.getString(SP_File_NANE, UUID, "");
    }

    public static User setUser(User user) {
        if (user == null)
            return UserManager.user;
        MSPUtils.put(Contant.SP_File_NANE,Contant.USER_INFO,MSPUtils.ObjToStr(user));
        return UserManager.user = user;
    }

    public static String getAppId() {
        String appid = null;
        if(user == null){
            getUser();
        }
        if(user != null){
            appid = user.getAppId();
        }

        return appid;
    }



}
