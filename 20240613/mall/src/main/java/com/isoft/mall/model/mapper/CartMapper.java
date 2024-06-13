package com.isoft.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.isoft.mall.model.pojo.Cart;
import com.isoft.mall.model.sql.CartSql;
import com.isoft.mall.vo.CartVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 通过商品id和用户id查出购物车
     * @param userId                         用户id
     * @param productId                      商品id
     * @return 购物车
     */
    @SelectProvider(type = CartSql.class,method = "selectCartByUserIdAndProductId")
    @ResultMap(value = "com.isoft.mall.model.dao.CartMapper.BaseResultMap")
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId")Integer productId);

    /**
     * 购物车列表
     * @param userId 用户Id
     * @return 返回一个购物车列表
     */
    @SelectProvider(type = CartSql.class,method ="selectList")
    List<CartVo> selectList(Integer userId);

    /**
     * 选择购物车
     * 复用逻辑，当productId传入为null时，将用户中所有产品的selected都置为选中或者未选中
     * @param userId 用户Id
     * @param productId 商品Id
     * @param selected 选中状态
     * @return 返回成功的条数
     */
    @UpdateProvider(type = CartSql.class,method = "selectOrNot")
    Integer selectOrNot(@Param("userId") Integer userId, @Param("productId")Integer productId,@Param("selected")Integer selected);
}
