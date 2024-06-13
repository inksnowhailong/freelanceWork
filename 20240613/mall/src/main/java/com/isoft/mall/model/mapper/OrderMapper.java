package com.isoft.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.isoft.mall.model.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    @Select("select * from mall_order where order_no=#{orderNo};")
    @ResultMap(value = "com.isoft.mall.model.dao.OrderMapper.BaseResultMap")
    Order selectByOrderNo(String orderNo);
    /**
     * 注意别在sql语句后面加分号，这个需要分页查询
     * @param userId
     * @return
     */
    @Select("select * from mall_order where user_id=#{userId} order by create_time desc ")
    @ResultMap(value = "com.isoft.mall.model.dao.OrderMapper.BaseResultMap")
    List<Order> selectForCustomer(Integer userId);

    @Select("select * from mall_order order by create_time desc")
    @ResultMap(value = "com.isoft.mall.model.dao.OrderMapper.BaseResultMap")
    List<Order> selectAllForAdmin();
}
