package com.isoft.mall.model.mapper;

import com.isoft.mall.model.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    @Select("select * from imooc_mall_order_item where order_no=#{orderNo};")
//    @ResultMap(value = "com.isoft.mall.model.dao.OrderItemMapper.BaseResultMap")
    List<OrderItem> selectByOrderNo(String orderNo);

}
