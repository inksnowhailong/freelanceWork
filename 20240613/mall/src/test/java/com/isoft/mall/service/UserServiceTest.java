package com.isoft.mall.service;

import com.isoft.mall.exception.MallException;
import com.isoft.mall.model.mapper.UserMapper;
import com.isoft.mall.model.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;
    @Test
    public void login() throws MallException {
        User mumu7 = userMapper.selectLogin("mumu7","12345678");

//        User user = userMapper.selectByPrimaryKey(2);
//
//        System.out.println(user);
        System.out.println(mumu7);
    }
}
