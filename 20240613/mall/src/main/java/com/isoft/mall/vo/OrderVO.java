package com.isoft.mall.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.isoft.mall.model.pojo.OrderItem;

import java.util.Date;
import java.util.List;

/**
 * @author shen
 * order 前台展示字段
 */
public class OrderVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private List<OrderItem> orderItemList;

    private Double totalPrice;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
