package com.pri.yunshuwanli.cloudticket.entry;
@Deprecated
public class OrderInfoWarpp {

    /**
     * appId : RJd037a9ea5557
     * clientNo : zsq_01
     * sign : c10a7ee8f389d7a1e27804499387cb03
     * reqType : 66
     * data : {"orderNo":"test_ME24156071","totalAmount":"1170.00","orderDate ":"2018-06-22 23:59:59","carNo":"沪 A88888","remark":"某某场库,停车时间 201809061433-201809061533 共计一小时"}
     */

    private String appId;
    private String clientNo;
    private String sign;
    private String reqType;
    private OrderInfo data;

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public OrderInfo getData() {
        return data;
    }

    public void setData(OrderInfo data) {
        this.data = data;
    }


}
