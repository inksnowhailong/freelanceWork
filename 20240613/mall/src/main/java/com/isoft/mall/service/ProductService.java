package com.isoft.mall.service;

import com.github.pagehelper.PageInfo;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.request.AddCategoryReq;
import com.isoft.mall.model.request.AddProductReq;
import com.isoft.mall.model.request.ProductListReq;




/**
 * @author shen
 * 商品Service
 */
public interface ProductService {

    void add(AddProductReq addProductReq);

    void update(Product updateProduct);

    void delete(Integer id);

    PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize,String name);

    PageInfo list(ProductListReq productListReq);
}
