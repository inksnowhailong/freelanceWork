package com.isoft.mall.model.sql;

import org.apache.ibatis.jdbc.SQL;
/**
 * Cart的SQL语句构建器
 * @author shen
 */
public class CartSql {

    public String selectCartByUserIdAndProductId(){
        return new SQL(){{
            SELECT("*");
            FROM("mall_cart");
            WHERE("user_id=#{userId} AND product_id=#{productId}");
        }}.toString();
    }

    public String selectList(){
        return new SQL(){{
            SELECT("c.id as id,p.id as productId,c.user_id as userId,c.quantity as quantity,c.selected as selected," +
                    "p.price as price,p.name as productName,p.image as productImage");
            FROM("mall_cart "+"c");
            LEFT_OUTER_JOIN("mall_product p "+
                    "on p.id=c.product_id");
            WHERE("c.user_id=#{userId}");
            AND();
            WHERE("p.status=1");
        }}.toString();
    }

    public String selectOrNot(final Integer productId){
        return new SQL(){{
            UPDATE("mall_cart");
            SET("selected=#{selected}");
            WHERE("user_Id=#{userId}");
            if (productId!=null){
                AND();
                WHERE("product_id=#{productId}");
            }
        }}.toString();
    }
}


