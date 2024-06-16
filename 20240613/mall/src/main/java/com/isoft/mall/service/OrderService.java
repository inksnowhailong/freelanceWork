package com.isoft.mall.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.isoft.mall.model.pojo.OrderItem;
import com.isoft.mall.model.request.CreateOrderReq;
import com.isoft.mall.vo.OrderNewVo;
import com.isoft.mall.vo.OrderVO;

import java.util.List;

/**
 * @author shen
 * Order业务接口
 */
public interface OrderService {

    String createOrder(OrderNewVo orderNewVo) throws JsonProcessingException;

    List<OrderVO> listForCustomer(Integer userId);

}
