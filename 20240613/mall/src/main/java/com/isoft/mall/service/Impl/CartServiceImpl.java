package com.isoft.mall.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isoft.mall.common.Constant;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.model.mapper.CartMapper;
import com.isoft.mall.model.mapper.ProductMapper;
import com.isoft.mall.model.pojo.Cart;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.service.CartService;
import com.isoft.mall.vo.CartVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shen
 * 购物车业务实现类
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {
    @Resource
    ProductMapper productMapper;
    @Resource
    CartMapper cartMapper;

    /**
     * 购物车列表
     * @param userId 根据用户id，查出购物车列表
     * @return 购物车列表集合
     */
    @Override
    public List<CartVo> list(Integer userId){
        List<CartVo> cartVos = cartMapper.selectList(userId);
        //totalPrice 是查不到的，需要手动去算
        for (int i = 0; i < cartVos.size(); i++) {
            CartVo cartVo = cartVos.get(i);
            cartVo.setTotalPrice(cartVo.getPrice()*cartVo.getQuantity());
        }
        return cartVos;
    }

    /**
     * 添加购物车
     * @param userId 用户id
     * @param productId 商品id
     * @param count 想要购买的数量
     */
    @Override
    public List<CartVo> add(Integer userId, Integer productId, Integer count){
        //1.验证添加是否合法
        validProduct(productId,count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        //2.不在购物车的情况
        if (cart==null){
            //这个商品之前不在购物车里，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.SELECTED);
            cartMapper.insertSelective(cart);
        }else {
            //这个商品已经在购物车里，则数量相加
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.SELECTED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    /**
     * 作为程序员，不是先想代码的业务逻辑，而是异常处理
     * 验证添加是否合法
     * @param productId 商品id
     * @param count 想要购买的数量
     */
    private void validProduct(Integer productId, Integer count) {
        //1.查出商品
        Product product = productMapper.selectByPrimaryKey(productId);
        //2.判断商品是否存在，是否上架
        //只用1和0，这种是偷懒，需要在Constant以代码枚举的形式，显示上下架
        if (product==null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
            throw new MallException(MallExceptionEnum.NO_SALE);
        }
        //3.判断商品的库存
        if (count>product.getStock()){
            throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    /**
     * 更新的方法
     */
    @Override
    public List<CartVo> update(Integer userId, Integer productId, Integer count){
        //1.验证添加是否合法
        validProduct(productId,count);
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        //2.这个商品之前不在购物车里，无法更新
        if (cart==null){
            throw new MallException(MallExceptionEnum.UPDATE_FILED);
        }else {
            //这个商品已经在购物车里，则更新数量
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.SELECTED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    /**
     * 删除购物车
     */
    @Override
    public List<CartVo> delete(Integer userId, Integer productId){
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        //2.这个商品之前不在购物车里，无法删除
        if (cart==null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }else {
            //这个商品已经在购物车里，则可以删除
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }
    /**
     * 单选 选中购物车中的物品
     * 复用逻辑:当productId传入null的时候
     */
    @Override
    public List<CartVo> selectOrNot(Integer userId, Integer productId, Integer selected){
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart==null){
            //这个商品之前不在购物车里，无法选中
            throw new MallException(MallExceptionEnum.UPDATE_FILED);
        }else {
            //这个商品之前不在购物车里，则可以选中或者不选中
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return this.list(userId);
    }
    /**
     * 选中购物车中的物品 全选或全不选
     */
    @Override
    public List<CartVo> selectAllOrNot(Integer userId,Integer selected){
        //改变选中状态
        Integer integer = cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }

}
