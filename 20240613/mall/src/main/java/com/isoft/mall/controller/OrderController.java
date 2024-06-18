package com.isoft.mall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.model.pojo.OrderItem;
import com.isoft.mall.service.OrderService;
import com.isoft.mall.vo.OrderNewVo;
import com.isoft.mall.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shen
 * 订单Controller
 */
@RestController
public class OrderController {
    @Resource
    OrderService orderService;


    @PostMapping("order/create")
    @ApiOperation("创建订单")
    public ApiRestResponse create(@RequestBody OrderNewVo orderNewVo) throws JsonProcessingException {
        String orderNo = orderService.createOrder(orderNewVo);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("order/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list(@RequestParam Integer userId){
        List<OrderVO> list = orderService.listForCustomer(userId);
        return ApiRestResponse.success(list);
    }

}
