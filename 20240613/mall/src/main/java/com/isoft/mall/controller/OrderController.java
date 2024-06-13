package com.isoft.mall.controller;

import com.github.pagehelper.PageInfo;
import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.model.request.CreateOrderReq;
import com.isoft.mall.service.OrderService;
import com.isoft.mall.vo.OrderVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("order/detail")
    @ApiOperation("前台订单详情")
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("order/list")
    @ApiOperation("前台订单列表")
    public ApiRestResponse list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("order/cancel")
    @ApiOperation("前台取消订单")
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }
    /**
     * 生成支付用的二维码
     */
    @PostMapping("order/qrcode")
    @ApiOperation("生成支付二维码")
    public ApiRestResponse qrcode(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }
    /**
     * 支付接口
     */
    @GetMapping("pay")
    @ApiOperation("支付接口")
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

}
