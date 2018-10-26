package com.pri.yunshuwanli.cloudticket;

public class Contant {

    public static final String CAR_NO_PATTERN = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(?:(?![A-Z]{4})[A-Z0-9]){4}[A-Z0-9挂学警港澳]{1}$";
    public static final String SP_File_NANE = "sp_file";
    public static final String UUID = "uuid";
    public static final String USER_INFO = "user";

    //    测试地址：
    public static final String TEST_BASE_URL= "http://test.datarj.com";
    public static final String TEST_BASE_URL_POS = TEST_BASE_URL+"/webService/posService";
    public static final String TEST_BASE_URL_KPT = TEST_BASE_URL+"/webService/kptService";
    public static final String TEST_BASE_URL_QR = "http://fpjtest.datarj.com";


    //    正式地址：

    public static final String BASE_URL = "http://open2.datarj.com";
    public static final String BASE_URL_POS = BASE_URL+"/webService/posService";
    public static final String BASE_URL_KPT = BASE_URL+"/webService/kptService";
    public static final String BASE_URL_QR = "http://fpj.datarj.com";


}
