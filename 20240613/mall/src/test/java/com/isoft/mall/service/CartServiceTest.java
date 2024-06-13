package com.isoft.mall.service;

import com.isoft.mall.model.mapper.CartMapper;
import com.isoft.mall.vo.CartVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

@SpringBootTest
class CartServiceTest {
    @Resource
    CartService cartService;
    @Resource
    CartMapper cartMapper;
    @Test
    void list() {
        List<CartVo> list = cartService.list(23);
//        List<CartVo> list = cartMapper.selectList(23);
        list.forEach(System.out::println);
    }
}
