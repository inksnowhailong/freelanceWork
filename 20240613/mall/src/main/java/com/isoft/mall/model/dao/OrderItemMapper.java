package com.isoft.mall.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.isoft.mall.model.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

}
