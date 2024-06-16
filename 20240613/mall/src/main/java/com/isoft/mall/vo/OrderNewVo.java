package com.isoft.mall.vo;

import com.isoft.mall.model.pojo.Product;

import java.util.List;

/**
 * @Author: shenjiabao
 * @Data: 2024/6/15 17:45
 * @Version: 1.0
 * @Description:
 */
public class OrderNewVo {

    private Integer userId;
    private String productList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }
}
