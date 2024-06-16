package com.isoft.mall.service.Impl;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isoft.mall.model.dao.*;
import com.isoft.mall.model.pojo.*;
import com.isoft.mall.service.OrderService;
import com.isoft.mall.util.OrderCodeFactory;
import com.isoft.mall.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shen
 * Order订单的业务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderService {
    @Resource
    ProductMapper productMapper;
    @Resource
    OrderItemMapper orderItemMapper;
    @Resource
    CartMapper cartMapper;


    /**
     * 创建订单
     * @param orderNewVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(OrderNewVo orderNewVo) throws JsonProcessingException {
        //当前订单的列表
        String productJson = orderNewVo.getProductList();
        //将json字符串转换为对象
        ObjectMapper objectMapper = new ObjectMapper();
        //购物车的商品
        List<ProductVo> productList = objectMapper.readValue(productJson, new TypeReference<List<ProductVo>>(){});

        Integer userId = orderNewVo.getUserId();
        //订单号
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));

        //订单信息
        productList.forEach(product -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(Integer.valueOf(product.getProductId()));
            orderItem.setProductName(product.getName());
            String img = product.getImg();
            //去掉前缀
            img = img.substring(img.lastIndexOf("/images/"));
            orderItem.setProductImg(img);
            orderItem.setUnitPrice(Double.valueOf(product.getPrice()));
            orderItem.setQuantity(Integer.valueOf(product.getCount()));
            orderItem.setUserId(userId);
            orderItem.setCreateTime(new Date());
            orderItemMapper.insertSelective(orderItem);
        });
        //删除product的库存
        productList.forEach(product -> {
            Product product1 = productMapper.selectById(Integer.valueOf(product.getProductId()));
            product1.setStock(product1.getStock()-Integer.valueOf(product.getCount()));
            productMapper.updateByPrimaryKeySelective(product1);
        });
        //删除购物车中的商品
        productList.forEach(product -> {
            QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<Cart>().eq("user_id", userId).eq("product_id", product.getProductId());
            cartMapper.delete(cartQueryWrapper);
        });

        return orderNo;
    }

    /**
     * 给用户看的购物车列表
     */
    public List<OrderVO> listForCustomer(Integer userId) {
        List<OrderItem> orderItemList = orderItemMapper.selectList(new QueryWrapper<OrderItem>().eq("user_id", userId));

        // 创建时间相同的订单放在一起
        Map<Date, List<OrderItem>> groupedByCreateDate = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getCreateTime));

        // 将分组后的数据转换为List<OrderVO>
        List<OrderVO> orderVOArrayList = new ArrayList<>();
        for (Map.Entry<Date, List<OrderItem>> entry : groupedByCreateDate.entrySet()) {
            OrderVO orderVO = new OrderVO();
            orderVO.setCreateTime(entry.getKey());
            orderVO.setOrderItemList(entry.getValue());
            double totalPrice = entry.getValue().stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();
            orderVO.setTotalPrice(totalPrice);
            orderVOArrayList.add(orderVO);
        }
        //orderVOArrayList按照创建时间排序
        orderVOArrayList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));

        return orderVOArrayList;
    }



}
