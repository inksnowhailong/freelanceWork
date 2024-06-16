package com.isoft.mall.config;



import com.isoft.mall.filter.UserFilter;
import com.isoft.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: shenjiabao
 * @Data: 2024/6/15 11:35
 * @Version: 1.0
 * @Description:
 */
//@Configuration
//public class UserFilterConfig {
//    @Bean
//    public UserFilter userFilter() {
//        return new UserFilter();
//    }
//    @Bean(name = "userFilterConf")
//    public FilterRegistrationBean<UserFilter> userFilterC() {
//        FilterRegistrationBean<UserFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(userFilter());
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.addUrlPatterns("/login/*");
//        registrationBean.setName("UserFilter");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }
//}
