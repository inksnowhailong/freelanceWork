package com.isoft.mall.model.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CartMapperTest {
    @Resource
    CartMapper cartMapper;
    @Test
    void selectCartByUserIdAndProductId() {
//        cartMapper.selectCartByUserIdAndProductId()
    }
}
