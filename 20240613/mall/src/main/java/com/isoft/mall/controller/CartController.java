package com.isoft.mall.controller;

import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.filter.UserFilter;
import com.isoft.mall.service.CartService;
import com.isoft.mall.vo.CartVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public ApiRestResponse list(){
        //内部获取用户ID，防止横向越权
        List<CartVo> cartList = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(cartList);
    }

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public ApiRestResponse add(@RequestParam Integer productId,Integer count){
        List<CartVo> cartVoList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVoList);
    }

    @PostMapping("/update")
    @ApiOperation("更新购物车")
    public ApiRestResponse update(@RequestParam Integer productId,Integer count){
        List<CartVo> cartVoList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVoList);
    }
    @PostMapping("/delete")
    @ApiOperation("删除购物车")
    public ApiRestResponse delete(@RequestParam Integer productId){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVo> cartVoList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVoList);
    }
    @PostMapping("/select")
    @ApiOperation("选中/不选中购物车的某商品")
    public ApiRestResponse select(@RequestParam Integer productId, Integer selected){
        List<CartVo> cartVoList = cartService.selectOrNot(UserFilter.currentUser.getId(),productId,selected);
        return ApiRestResponse.success(cartVoList);
    }

    @PostMapping("/selectAll")
    @ApiOperation("全选择/全不选中购物车的某商品")
    public ApiRestResponse selectAll(@RequestParam Integer selected){

        List<CartVo> cartVoList = cartService.selectAllOrNot(UserFilter.currentUser.getId(),selected);
        return ApiRestResponse.success(cartVoList);
    }
}
