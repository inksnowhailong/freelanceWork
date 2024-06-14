package com.isoft.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.request.ProductListReq;
import com.isoft.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台商品Controller
 */
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/product/detail")
    public ApiRestResponse detail(@RequestParam Integer id){
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @GetMapping("/product/cuslist")
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo pageInfo = productService.list(productListReq);
        return ApiRestResponse.success(pageInfo);
    }

}
