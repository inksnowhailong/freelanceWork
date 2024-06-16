package com.isoft.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.model.dao.ProductMapper;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.request.AddProductReq;
import com.isoft.mall.model.request.ProductListReq;
import com.isoft.mall.service.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

/**
 * @author shen
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Override
    public void add(AddProductReq addProductReq){
        Product product =  new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }

        int count = productMapper.insert(product);
        if (count==0){
            throw new MallException(MallExceptionEnum.CREATE_FAILED);
        }
    }
    /**
     * 更新商品
     */
    @Override
    public void update(Product updateProduct){
        updateProduct.setUpdateTime(new Date());
        int count = productMapper.updateById(updateProduct);
        if (count==0){
            throw new MallException(MallExceptionEnum.UPDATE_FILED);
        }
    }
    /**
     * 删除商品
     */
    @Override
    public void delete(Integer id){
        Product productOld = productMapper.selectById(id);
//        查不到该记录无法删除
        if (productOld==null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteById(id);
        if (count==0){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }
    /**
     * 批量上下架功能
     */
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    /**
     * 商品后台分页查询
     * @param pageNum 从哪个开始
     * @param pageSize 一页有几个
     */
    @Override
    public PageInfo<Product> listForAdmin(Integer pageNum, Integer pageSize,String name) {
        PageHelper.startPage(pageNum,pageSize);
        //如果name不为空,则查询
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(name)){
            queryWrapper.like("name","%"+name+"%");
        }
        queryWrapper.orderByDesc("create_time");
        List<Product> products = productMapper.selectList(queryWrapper);
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        //排除状态为0的商品
        queryWrapper.ne("status", 0);
        List<Product> products = productMapper.selectList(queryWrapper);
        //循环products，如果status=3，那么价格打八折
        for (Product product : products) {
            if (product.getStatus().equals(2)){
                product.setPrice(product.getPrice()*80/100);
            }
        }
        PageInfo pageInfo = new PageInfo(products);

        return pageInfo;
    }

}
