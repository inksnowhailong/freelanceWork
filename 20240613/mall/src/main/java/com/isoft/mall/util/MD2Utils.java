package com.isoft.mall.util;

import com.isoft.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author shen
 * MD5工具
 * 哈希工具
 */
public class MD2Utils {
    public static String getMD5Str(String setValue)throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((setValue+ Constant.SALT).getBytes()));
    }
    //用这个方法测试生成的MD5的值
    public static void main(String[] args) {
        String md5 = null;
        try {
            md5 = getMD5Str("admin123");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        System.out.println(md5);
    }
}
