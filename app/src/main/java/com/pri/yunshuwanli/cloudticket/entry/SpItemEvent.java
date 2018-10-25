package com.pri.yunshuwanli.cloudticket.entry;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SpItemEvent {

    public User.SpListBean sldetail;

    public SpItemEvent(User.SpListBean sldetail) {
        this.sldetail = sldetail;
    }

    public static void postEvent(User.SpListBean sldetail){
        EventBus.getDefault().postSticky(new SpItemEvent(sldetail));
    }
}
