package com.pri.yunshuwanli.cloudticket.utils.crc;

public class YwxException extends Exception {
    private int code = 0;
    private String message = "";

    public YwxException(){
        super();
    }
    public YwxException(int code, String message){
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
