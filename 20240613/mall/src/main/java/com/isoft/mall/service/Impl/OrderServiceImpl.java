package com.isoft.mall.service.Impl;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.isoft.mall.common.Constant;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.filter.UserFilter;
import com.isoft.mall.model.dao.*;
import com.isoft.mall.model.pojo.Order;
import com.isoft.mall.model.pojo.OrderItem;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.pojo.User;
import com.isoft.mall.model.request.CreateOrderReq;
import com.isoft.mall.service.CartService;
import com.isoft.mall.service.OrderService;
import com.isoft.mall.service.UserService;
import com.isoft.mall.util.OrderCodeFactory;
import com.isoft.mall.util.QRCodeGenerator;
import com.isoft.mall.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    ProductMapper productMapper;
    @Resource
    OrderItemMapper orderItemMapper;

    @Value("${file.upload.ip}")
    String ip;

    /**
     * 创建订单
     * @param orderNewVo
     * @return
     * TODO 使用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(OrderNewVo orderNewVo) throws JsonProcessingException {
        //当前订单的列表
        String productJson = orderNewVo.getProductList();
        //将json字符串转换为对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductVo> productList = objectMapper.readValue(productJson, new TypeReference<List<ProductVo>>(){});

        Integer userId = orderNewVo.getUserId();
        //订单号
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        //获取用户信息
//        QueryWrapper<User> id = new QueryWrapper<User>().eq("id", userId);
//        User user = userMapper.selectOne(id);
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
            Product product1 = productMapper.selectByPrimaryKey(Integer.valueOf(product.getProductId()));
            product1.setStock(product1.getStock()-Integer.valueOf(product.getCount()));
            productMapper.updateByPrimaryKeySelective(product1);
        });
        return orderNo;
    }

    /**
     * 给用户看的购物车列表
     * TODO 使用
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

        return orderVOArrayList;
    }



}
