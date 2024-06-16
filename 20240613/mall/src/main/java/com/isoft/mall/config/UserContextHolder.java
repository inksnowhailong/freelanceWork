package com.isoft.mall.config;

/**
 * @Author: shenjiabao
 * @Data: 2024/6/15 13:43
 * @Version: 1.0
 * @Description:
 */
public class UserContextHolder {
    private static final ThreadLocal<Integer> userThreadLocal = new ThreadLocal<>();

    public static void setUserId(Integer userId) {
        System.out.println("Setting userId=" + userId + " for thread " + Thread.currentThread().getId());
        userThreadLocal.set(userId);
    }

    public static Integer getUserId() {
        System.out.println("Getting userId for thread " + Thread.currentThread().getId());
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
    //判断是否设置了userId
    public static boolean isSet() {
        return userThreadLocal.get() != null;
    }
}
