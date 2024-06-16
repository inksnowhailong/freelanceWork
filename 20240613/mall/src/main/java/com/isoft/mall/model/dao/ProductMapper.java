package com.isoft.mall.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.isoft.mall.model.pojo.Product;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductMapper extends BaseMapper<Product>  {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    @Select("select * from mall_product where name=#{name,jdbcType=VARCHAR}")
    Product selectByName(@Param("name") String name);

    /**
     * 批量上下架
     * 动态拼接
     */
    int batchUpdateSellStatus(@Param("ids") Integer[] ids,@Param("sellStatus") Integer sellStatus);


}
