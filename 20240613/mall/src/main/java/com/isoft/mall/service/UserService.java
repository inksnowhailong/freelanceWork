package com.isoft.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.model.pojo.User;

/**
 * @author shen
 * UserService
 */
public interface UserService {
    User getUser();
    /**
     * 注册
     */
    void register(String username,String password) throws MallException;
    /**
     * 登录
     */
    User login(String userName, String password) throws MallException;

    /**
     * @param user 传入对象
     * @throws MallException
     * 更新签名
     */
    void updateInformation(User user) throws MallException;

    /**
     * @param user 传入对象
     * @return 是否越权
     * 越权校验
     */
    boolean checkAdminRole(User user);

    IPage<User> userPage(IPage<User> page, QueryWrapper<User> queryWrapper);
}
