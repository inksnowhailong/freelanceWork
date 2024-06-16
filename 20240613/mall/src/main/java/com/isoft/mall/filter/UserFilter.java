package com.isoft.mall.filter;

import com.isoft.mall.common.Constant;
import com.isoft.mall.model.pojo.User;
import com.isoft.mall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户过滤器
 */
public class UserFilter implements Filter {
    /**
     * 不考虑多线程安全的问题
     */
    public static User currentUser;

    @Autowired
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("UserFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute(Constant.MALL_USER);
        }

        // 获取 session 数据后，先判断是否为登录接口或产品列表接口
        if (requestURI.endsWith("/login") || requestURI.endsWith("/product/list")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 如果用户未登录
        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }

        // 让逻辑走下去
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
