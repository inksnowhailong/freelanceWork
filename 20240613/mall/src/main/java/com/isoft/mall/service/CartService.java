package com.isoft.mall.service;

import com.isoft.mall.vo.CartVo;

import java.util.List;

/**
 * @author shen
 * 购物车业务接口
 */
public interface CartService {
    List<CartVo> add(Integer userId, Integer productId, Integer count);

    List<CartVo> list(Integer userId);

    List<CartVo> update(Integer userId, Integer productId, Integer count);

    List<CartVo> delete(Integer userId, Integer productId);

    List<CartVo> selectOrNot(Integer userId, Integer productId, Integer selected);

    List<CartVo> selectAllOrNot(Integer userId, Integer selected);
}
