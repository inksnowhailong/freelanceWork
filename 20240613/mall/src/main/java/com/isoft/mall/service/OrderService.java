package com.isoft.mall.service;

import com.github.pagehelper.PageInfo;
import com.isoft.mall.model.request.CreateOrderReq;
import com.isoft.mall.vo.OrderVO;

/**
 * @author shen
 * Order业务接口
 */
public interface OrderService {
    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    void pay(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void deliver(String orderNo);

    void finish(String orderNo);
}
