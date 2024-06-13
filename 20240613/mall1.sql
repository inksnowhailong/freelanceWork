/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : mall

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 13/06/2024 19:47:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mall_cart
-- ----------------------------
DROP TABLE IF EXISTS `mall_cart`;
CREATE TABLE `mall_cart`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `product_id` int NOT NULL COMMENT '商品id',
  `user_id` int NOT NULL COMMENT '用户id',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `selected` int NOT NULL DEFAULT 1 COMMENT '是否已勾选：0代表未勾选，1代表已勾选',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_cart
-- ----------------------------

-- ----------------------------
-- Table structure for mall_category
-- ----------------------------
DROP TABLE IF EXISTS `mall_category`;
CREATE TABLE `mall_category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '分类目录名称',
  `type` int NOT NULL COMMENT '分类目录级别，例如1代表一级，2代表二级，3代表三级',
  `parent_id` int NOT NULL COMMENT '父id，也就是上一级目录的id，如果是一级目录，那么父id为0',
  `order_num` int NOT NULL COMMENT '目录展示时的排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_category
-- ----------------------------
INSERT INTO `mall_category` VALUES (3, '新鲜水果', 1, 0, 1, '2020-06-18 01:17:00', '2020-06-28 17:11:26');
INSERT INTO `mall_category` VALUES (4, '平价水果', 2, 3, 1, '2020-06-18 01:17:00', '2020-06-28 16:25:10');
INSERT INTO `mall_category` VALUES (5, '海鲜水产', 1, 0, 2, '2020-06-18 01:17:00', '2020-06-28 16:25:20');
INSERT INTO `mall_category` VALUES (6, '精选肉类', 1, 0, 3, '2020-06-18 01:17:00', '2020-06-28 16:25:21');
INSERT INTO `mall_category` VALUES (7, '螃蟹', 2, 5, 1, '2020-06-18 01:17:00', '2020-06-28 16:25:15');
INSERT INTO `mall_category` VALUES (8, '鱼类', 2, 5, 2, '2020-06-18 01:17:00', '2020-06-28 16:25:16');
INSERT INTO `mall_category` VALUES (9, '冷饮冻食', 1, 0, 4, '2020-06-20 13:45:28', '2020-06-28 16:25:22');
INSERT INTO `mall_category` VALUES (10, '蔬菜蛋品', 1, 0, 5, '2020-06-20 13:45:28', '2020-06-28 16:25:23');
INSERT INTO `mall_category` VALUES (11, '草莓', 2, 3, 2, '2020-06-18 01:17:00', '2020-06-28 15:44:42');
INSERT INTO `mall_category` VALUES (12, '奇异果', 2, 3, 3, '2020-06-18 01:17:00', '2020-06-28 16:25:12');
INSERT INTO `mall_category` VALUES (13, '虾', 2, 5, 3, '2020-06-18 01:17:00', '2020-06-28 16:25:17');
INSERT INTO `mall_category` VALUES (14, '车厘子', 2, 3, 4, '2020-06-18 01:17:00', '2020-06-28 16:25:12');
INSERT INTO `mall_category` VALUES (15, '鲜切菠萝', 2, 27, 5, '2020-06-18 01:17:00', '2020-02-11 00:42:33');
INSERT INTO `mall_category` VALUES (16, '牛羊肉', 2, 6, 1, '2020-06-18 01:17:00', '2020-06-28 16:25:18');
INSERT INTO `mall_category` VALUES (17, '冷冻水果', 2, 9, 1, '2020-06-18 01:17:00', '2020-06-28 16:25:18');
INSERT INTO `mall_category` VALUES (18, '蔬菜综合', 2, 10, 1, '2020-06-18 01:17:00', '2020-02-11 00:48:27');
INSERT INTO `mall_category` VALUES (19, '苹果', 3, 4, 1, '2020-06-18 01:17:00', '2020-02-11 00:37:02');
INSERT INTO `mall_category` VALUES (27, '美味果切', 1, 0, 7, '2020-06-20 13:45:28', '2020-02-10 23:20:36');
INSERT INTO `mall_category` VALUES (28, '其他水果', 2, 3, 4, '2020-06-18 01:17:00', '2020-06-28 16:25:12');

-- ----------------------------
-- Table structure for mall_order
-- ----------------------------
DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '订单号（非主键id）',
  `user_id` int NOT NULL COMMENT '用户id',
  `total_price` int NOT NULL COMMENT '订单总价格',
  `receiver_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名快照',
  `receiver_mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人手机号快照',
  `receiver_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '收货地址快照',
  `order_status` int NOT NULL DEFAULT 10 COMMENT '订单状态: 0用户已取消，10未付款（初始状态），20已付款，30已发货，40交易完成',
  `postage` int NULL DEFAULT 0 COMMENT '运费，默认为0',
  `payment_type` int NOT NULL DEFAULT 1 COMMENT '支付类型,1-在线支付',
  `delivery_time` timestamp NULL DEFAULT NULL COMMENT '发货时间',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '交易完成时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单表;' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_order
-- ----------------------------

-- ----------------------------
-- Table structure for mall_order_item
-- ----------------------------
DROP TABLE IF EXISTS `mall_order_item`;
CREATE TABLE `mall_order_item`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '归属订单id',
  `product_id` int NOT NULL COMMENT '商品id',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `product_img` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商品图片',
  `unit_price` int NOT NULL COMMENT '单价（下单时的快照）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '商品数量',
  `total_price` int NOT NULL DEFAULT 0 COMMENT '商品总价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单的商品表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_order_item
-- ----------------------------

-- ----------------------------
-- Table structure for mall_product
-- ----------------------------
DROP TABLE IF EXISTS `mall_product`;
CREATE TABLE `mall_product`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '商品主键id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '产品图片,相对路径地址',
  `detail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '商品详情',
  `price` double NOT NULL COMMENT '价格,单位-分',
  `stock` int NOT NULL COMMENT '库存数量',
  `status` int NOT NULL DEFAULT 1 COMMENT '商品上架状态：0-下架，1-上架，2促销',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `use_time` datetime NULL DEFAULT NULL COMMENT '保质期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_product
-- ----------------------------
INSERT INTO `mall_product` VALUES (2, '澳洲进口大黑车厘子大樱桃包甜黑樱桃12', '/images/dae50296-04f6-4d2c-9e71-b98d2b3e4ff2.jpg', '商品毛重：1.0kg货号：608323093445原产地：智利类别：美早热卖时间：1月，11月，12月国产/进口：进口售卖方式：单品', 50, 10000, 2, '2020-06-18 16:08:15', '2024-06-13 18:53:52', '2024-06-13 08:00:00');

-- ----------------------------
-- Table structure for mall_user
-- ----------------------------
DROP TABLE IF EXISTS `mall_user`;
CREATE TABLE `mall_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码，MD5加密',
  `personalized_signature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '个性签名',
  `email_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '邮件地址',
  `role` int NOT NULL DEFAULT 1 COMMENT '角色，1-普通用户，2-管理员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mall_user
-- ----------------------------
INSERT INTO `mall_user` VALUES (2, 'admin', 'y7G7joM1xDNdiLhfM0m0+Q==', '更新了我的签名', '123123@11.com', 2, '2020-06-17 15:11:32', '2024-06-13 13:23:21', '中国', '小黑', '139123123');
INSERT INTO `mall_user` VALUES (9, 'xiaomu2', 'AWRuqaxc6iryhHuA4OnFag==', '祝你今天好心情', '123123@11.com', 2, '2020-02-09 20:39:47', '2024-06-13 13:23:22', '中国', '小白', '123123123');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin123', '123', '312312', '3123', '12321');

SET FOREIGN_KEY_CHECKS = 1;
