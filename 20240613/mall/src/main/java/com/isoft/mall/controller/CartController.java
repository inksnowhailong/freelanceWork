package com.isoft.mall.controller;

import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.config.UserContextHolder;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.filter.UserFilter;
import com.isoft.mall.service.CartService;
import com.isoft.mall.vo.CartVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Resource
    CartService cartService;

    @GetMapping("/list")
    @ApiOperation("购物车列表")
    public ApiRestResponse list(Integer userId){

        //内部获取用户ID，防止横向越权
        List<CartVo> cartList = cartService.list(userId);
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam("productId") Integer productId,Integer count,Integer userId ){
        List<CartVo> cartVoList = cartService.add(userId, productId, count);
        return ApiRestResponse.success(cartVoList);
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(@RequestParam Integer productId,Integer count,Integer userId){

        List<CartVo> cartVoList = cartService.update(userId, productId, count);
        return ApiRestResponse.success(cartVoList);
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车")
    public ApiRestResponse delete(@RequestParam Integer productId,Integer userId){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVo> cartVoList = cartService.delete(userId, productId);
        return ApiRestResponse.success(cartVoList);
    }


}
