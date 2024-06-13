package com.isoft.mall.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author shen
 * 生成订单号工具类
 * 方法：时间+随机数
 */
public class OrderCodeFactory {
    private static String getDateTime(){
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
    private static int getRandom(Long n){
        Random random = new Random();
        // 获取一个5为随机数
        return (int)(random.nextDouble()*(90000))+10000;
    }

    /**
     * @param userId 没有使用到userId，以后可能会根据userId生成订单号
     * @return 时间和随机数
     */
    public static String getOrderCode(Long userId){
        return getDateTime()+getRandom(userId);
    }
}
