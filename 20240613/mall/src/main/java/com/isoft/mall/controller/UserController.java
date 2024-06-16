package com.isoft.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.common.Constant;
import com.isoft.mall.config.UserContextHolder;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.model.pojo.User;
import com.isoft.mall.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * @author shen
 * 描述： 用户控制器
 */
@Controller
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;



/**
 * 注册接口
 */
    @ResponseBody
    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws MallException {
        if (ObjectUtils.isEmpty(userName)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (ObjectUtils.isEmpty(password)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        //密码长度不能少于八位
        if (password.length()<8){
            return ApiRestResponse.error(MallExceptionEnum.PASSWORD_TO_SHORT);
        }
        userService.register(userName,password);
        return ApiRestResponse.success();
    }
/**
 * 登录接口
 */
    @ResponseBody
    @GetMapping("/login")
    public ApiRestResponse login(@RequestParam("userName")String userName, @RequestParam("password")String password,
                                 HttpSession session) throws MallException {
        if (ObjectUtils.isEmpty(userName)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (ObjectUtils.isEmpty(password)){
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(userName, password);
        //保存用户信息时，保护密码，不能返回加密的密码，所以设置成null
        user.setPassword(null);
        //将User对象放在session中，保存登录状态
        session.setAttribute(Constant.MALL_USER,user);
        UserContextHolder.setUserId(user.getId());
        System.out.println("After setting userId, current thread is " + Thread.currentThread().getId());

        return ApiRestResponse.success(user);
    }
/**
 * 更新个人签名
 */
    @ResponseBody
    @PostMapping("/user/update")
    public  ApiRestResponse updateUserInfo(HttpSession session,@RequestParam String signature) throws MallException {
        User currentUser=(User)session.getAttribute(Constant.MALL_USER);
        if (currentUser == null){
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

/**
 * 登出，并且清除session
 */
    @ResponseBody
    @PostMapping("/user/logout")
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }


    @GetMapping("/user/information")
    @ResponseBody
    public User getUser(HttpSession session){
        return (User)session.getAttribute(Constant.MALL_USER);
    }

}
