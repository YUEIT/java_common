package cn.yue.base.test;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * 介绍：
 * 作者：jelly
 * 邮箱：yangguodong@imcoming.cn
 * 时间：2017/12/1 10:06
 */

public class OrderBean {
    /**
     * order_id : 50171128604440001
     * mark_code : 11
     * consignee : tu
     * consignee_tel : 18888888888
     * adress : 哼
     * create_time : 1511858845000
     * deliveryed_time : 0
     * delivery_fee : 1111
     * amount : 8.04
     * status : 1000
     */

    @SerializedName("order_id")
    private String orderId;
    @SerializedName("mark_code")
    private int markCode;
    @SerializedName("consignee")
    private String consignee;
    @SerializedName("consignee_tel")
    private String consigneeTel;
    @SerializedName("adress")
    private String adress;
    @SerializedName("create_time")
    private long createTime;
    @SerializedName("deliveryed_time")
    private String deliveryedTime;
    @SerializedName("delivery_fee")
    private BigDecimal deliveryFee;
    @SerializedName("amount")
    private BigDecimal amount;
    @SerializedName("status")
    private int status;
    @SerializedName("statusDescription")
    private String statusDescription;
    @SerializedName("real_pay")
    private BigDecimal real_pay;
    @SerializedName("settle_amount")
    private BigDecimal settleAmount;  //结算金额
    @SerializedName("order_type")
    private int orderType;          //订单类型 0 外送订单  1 退款订单
    @SerializedName("is_pick_up")
    private int isPickUp; // 是否自提 0 否（外送订单） 1 是 （自提订单）
    @SerializedName("comment")
    private String comment;
    @SerializedName("refundStatus")
    private int refundStatus;//退款状态 0-未申请退款 1-申请退款中 2-退款中 3-退款失败 4-退款成功
    @SerializedName("refundStatusStr")
    private String refundStatusStr;



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMarkCode() {
        return markCode;
    }

    public void setMarkCode(int markCode) {
        this.markCode = markCode;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDeliveryedTime() {
        return deliveryedTime;
    }

    public void setDeliveryedTime(String deliveryedTime) {
        this.deliveryedTime = deliveryedTime;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public BigDecimal getReal_pay() {
        return real_pay;
    }

    public void setReal_pay(BigDecimal real_pay) {
        this.real_pay = real_pay;
    }

    public String getStatusStr() {
        String str = "未知";
        switch (status) {
            case 1000:
                str = "待付款";
                break;
            case 1001:
                str = "已支付";
                break;
            case 2001:
                str = "配送中";
                break;
            case 2002:
            case 3001:
                str = "已完成";
                break;
            case 9000:
                str = "已取消";
                break;
            case 8000:
                str = "错误";
                break;
        }
        return str;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BigDecimal getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderTypeString(){
        switch (orderType){
            case 0: return "普通订单";
            case 1: return "退款订单";
            default: return "";
        }
    }

    public int getIsPickUp() {
        return isPickUp;
    }

    public void setIsPickUp(int isPickUp) {
        this.isPickUp = isPickUp;
    }

    public String getIsPickUpString(){
        if(isPickUp == 0){
            return "外送订单";
        }else if(isPickUp == 1){
            return "自提订单";
        }else{
            return "";
        }
    }

    public String getIsPickUpStr(){
        if(isPickUp == 0){
            return "外送";
        }else if(isPickUp == 1){
            return "自提";
        }else{
            return "";
        }
    }

    public int getRefundStatus() {
        return refundStatus;
    }
    public String getRefundStatusStr() {
        return refundStatusStr;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundStatusStr(String refundStatusStr) {
        this.refundStatusStr = refundStatusStr;
    }
}
