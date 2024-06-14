package com.isoft.mall.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.model.mapper.UserMapper;
import com.isoft.mall.model.pojo.User;
import com.isoft.mall.service.UserService;
import com.isoft.mall.util.MD2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

/**
 * @author shen
 * UserServices实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(2);
    }

    @Override
    public void register(String userName, String password) throws MallException {
        //查询用户名是否重名
        User result = userMapper.selectByName(userName);
        if (result!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        //写到数据库
        User user = new User();
        user.setUsername(userName);
//        user.setPassword(password);
//        采用MD5进行加密
        try {
            user.setPassword(MD2Utils.getMD5Str(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        int count = userMapper.insertSelective(user);
        if (count==0){
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     */
    @Override
    public User login(String userName, String password) throws MallException {
        String md5Password =null;
        try {
            md5Password = MD2Utils.getMD5Str(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        User user = userMapper.selectLogin(userName, md5Password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);
        queryWrapper.eq("password",md5Password);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null){
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws MallException {
//        更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);

        if (updateCount > 1){
            throw new MallException(MallExceptionEnum.UPDATE_FILED);
        }
    }

    /**
     * 越权校验
     */
    @Override
    public boolean checkAdminRole(User user){
        //1.普通用户 2.管理员
        return user.getRole().equals(2);
    }

    @Override
    public IPage<User> userPage(IPage<User> page, QueryWrapper<User> queryWrapper) {
        IPage<User> page1 = page(page, queryWrapper);
        page1.setTotal(userMapper.selectCount(queryWrapper));
        return page1;
    }

}
