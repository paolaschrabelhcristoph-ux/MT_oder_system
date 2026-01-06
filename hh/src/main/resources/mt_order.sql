/*
Navicat MySQL Data Transfer

Source Server         : wd
Source Server Version : 80013
Source Host           : localhost:3306
Source Database       : mt_order

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2026-01-07 07:18:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cart_items`
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `size` varchar(255) DEFAULT NULL,
  `temperature` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `delivery_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  KEY `FK709eickf3kc0dujx3ub9i7btf` (`user_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK709eickf3kc0dujx3ub9i7btf` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of cart_items
-- ----------------------------

-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_time` datetime(6) NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_amount` double NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('1', '2026-01-06 17:40:57.463674', '待支付', '10', '1');
INSERT INTO `orders` VALUES ('2', '2026-01-06 17:52:38.990525', '待支付', '10', '1');
INSERT INTO `orders` VALUES ('3', '2026-01-06 17:53:32.150816', '待支付', '10', '1');
INSERT INTO `orders` VALUES ('4', '2026-01-06 17:53:34.132727', '待支付', '10', '1');
INSERT INTO `orders` VALUES ('5', '2026-01-06 17:53:36.244516', '待支付', '10', '1');
INSERT INTO `orders` VALUES ('6', '2026-01-06 18:15:59.335467', '待支付', '20', '1');
INSERT INTO `orders` VALUES ('7', '2026-01-06 23:14:20.549858', '待支付', '46', '1');
INSERT INTO `orders` VALUES ('8', '2026-01-06 23:37:56.071503', '待支付', '46', '1');
INSERT INTO `orders` VALUES ('9', '2026-01-06 23:39:58.182571', '待支付', '82', '1');
INSERT INTO `orders` VALUES ('10', '2026-01-06 23:50:15.170669', '待支付', '132', '1');
INSERT INTO `orders` VALUES ('11', '2026-01-06 23:51:50.280879', '待支付', '132', '1');
INSERT INTO `orders` VALUES ('12', '2026-01-07 05:13:51.052903', '待支付', '162', '1');
INSERT INTO `orders` VALUES ('13', '2026-01-07 05:33:17.856166', '待支付', '229', '1');
INSERT INTO `orders` VALUES ('14', '2026-01-07 05:34:10.004657', '待支付', '229', '1');
INSERT INTO `orders` VALUES ('15', '2026-01-07 05:34:13.200757', '待支付', '229', '1');
INSERT INTO `orders` VALUES ('16', '2026-01-07 05:41:28.112887', '已支付', '239', '1');
INSERT INTO `orders` VALUES ('17', '2026-01-07 05:41:31.208508', '已支付', '239', '1');
INSERT INTO `orders` VALUES ('18', '2026-01-07 05:53:41.071246', '已支付', '239', '1');
INSERT INTO `orders` VALUES ('19', '2026-01-07 06:10:43.136973', '已支付', '149', '1');
INSERT INTO `orders` VALUES ('20', '2026-01-07 06:11:17.685807', '已支付', '149', '1');
INSERT INTO `orders` VALUES ('21', '2026-01-07 06:12:05.859078', '已支付', '149', '1');
INSERT INTO `orders` VALUES ('22', '2026-01-07 06:15:47.797074', '已支付', '169', '1');
INSERT INTO `orders` VALUES ('23', '2026-01-07 06:29:24.577728', '已支付', '10', '1');
INSERT INTO `orders` VALUES ('24', '2026-01-07 06:34:39.104184', '已支付', '10', '1');
INSERT INTO `orders` VALUES ('25', '2026-01-07 06:39:05.483609', '已支付', '9', '1');
INSERT INTO `orders` VALUES ('26', '2026-01-07 06:40:54.823642', '已支付', '18', '1');
INSERT INTO `orders` VALUES ('27', '2026-01-07 07:12:01.000921', '已支付', '10', '1');
INSERT INTO `orders` VALUES ('28', '2026-01-07 07:17:28.556317', '已支付', '8', '1');

-- ----------------------------
-- Table structure for `order_items`
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `temperature` varchar(255) DEFAULT NULL,
  `delivery_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order_items
-- ----------------------------
INSERT INTO `order_items` VALUES ('1', '10', '1', '1', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('2', '10', '1', '2', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('3', '10', '1', '3', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('4', '10', '1', '4', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('5', '10', '1', '5', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('6', '10', '1', '6', '5', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('7', '10', '1', '6', '1', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('21', '10', '1', '11', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('22', '10', '5', '11', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('23', '8', '6', '11', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('24', '12', '2', '11', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('25', '10', '1', '12', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('26', '10', '5', '12', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('27', '8', '6', '12', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('28', '12', '3', '12', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('29', '9', '2', '12', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('30', '10', '1', '13', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('31', '10', '8', '13', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('32', '8', '8', '13', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('33', '12', '4', '13', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('34', '9', '3', '13', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('35', '10', '1', '14', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('36', '10', '8', '14', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('37', '8', '8', '14', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('38', '12', '4', '14', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('39', '9', '3', '14', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('40', '10', '1', '15', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('41', '10', '8', '15', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('42', '8', '8', '15', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('43', '12', '4', '15', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('44', '9', '3', '15', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('45', '10', '1', '16', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('46', '10', '9', '16', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('47', '8', '8', '16', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('48', '12', '4', '16', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('49', '9', '3', '16', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('50', '10', '1', '17', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('51', '10', '9', '17', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('52', '8', '8', '17', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('53', '12', '4', '17', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('54', '9', '3', '17', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('55', '10', '1', '18', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('56', '10', '9', '18', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('57', '8', '8', '18', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('58', '12', '4', '18', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('59', '9', '3', '18', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('60', '10', '1', '19', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('61', '8', '8', '19', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('62', '12', '4', '19', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('63', '9', '3', '19', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('64', '10', '1', '20', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('65', '8', '8', '20', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('66', '12', '4', '20', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('67', '9', '3', '20', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('68', '10', '1', '21', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('69', '8', '8', '21', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('70', '12', '4', '21', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('71', '9', '3', '21', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('72', '10', '1', '22', '5', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('73', '8', '8', '22', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('74', '12', '4', '22', '2', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('75', '9', '3', '22', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('76', '10', '2', '22', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('77', '10', '1', '23', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('78', '10', '1', '24', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('79', '9', '1', '25', '3', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('80', '10', '1', '26', '1', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('81', '8', '1', '26', '4', '小', '多冰', null);
INSERT INTO `order_items` VALUES ('82', '10', '1', '27', '1', '小', '多冰', '辽宁工程技术大学');
INSERT INTO `order_items` VALUES ('83', '8', '1', '28', '4', '小', '多冰', '辽宁工程技术大学');

-- ----------------------------
-- Table structure for `products`
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES ('1', '珍珠奶茶1', 'https://img95.699pic.com/photo/60018/3945.jpg_wh860.jpg', '珍珠奶茶1', '10');
INSERT INTO `products` VALUES ('2', '奶盖茶1', 'https://img95.699pic.com/photo/60018/3945.jpg_wh860.jpg', '奶盖茶1', '12');
INSERT INTO `products` VALUES ('3', '水果茶1', 'https://img95.699pic.com/photo/60018/3945.jpg_wh860.jpg', '水果茶1', '9');
INSERT INTO `products` VALUES ('4', '咖啡1', 'https://img95.699pic.com/photo/60018/3945.jpg_wh860.jpg', '咖啡1', '8');
INSERT INTO `products` VALUES ('5', '季节限定1', 'https://img95.699pic.com/photo/60018/3945.jpg_wh860.jpg', '季节限定1', '10');

-- ----------------------------
-- Table structure for `product_attributes`
-- ----------------------------
DROP TABLE IF EXISTS `product_attributes`;
CREATE TABLE `product_attributes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcex46yvx4g18b2pn09p79h1mc` (`product_id`),
  CONSTRAINT `FKcex46yvx4g18b2pn09p79h1mc` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of product_attributes
-- ----------------------------
INSERT INTO `product_attributes` VALUES ('1', '默认温度', '温度', '多冰', '1');
INSERT INTO `product_attributes` VALUES ('2', '默认份量', '份量', '中', '1');
INSERT INTO `product_attributes` VALUES ('3', '默认温度', '温度', '多冰', '2');
INSERT INTO `product_attributes` VALUES ('4', '默认份量', '份量', '中', '2');
INSERT INTO `product_attributes` VALUES ('5', '默认温度', '温度', '多冰', '3');
INSERT INTO `product_attributes` VALUES ('6', '默认份量', '份量', '中', '3');
INSERT INTO `product_attributes` VALUES ('7', '默认温度', '温度', '多冰', '4');
INSERT INTO `product_attributes` VALUES ('8', '默认份量', '份量', '中', '4');
INSERT INTO `product_attributes` VALUES ('9', '默认温度', '温度', '多冰', '5');
INSERT INTO `product_attributes` VALUES ('10', '默认份量', '份量', '中', '5');

-- ----------------------------
-- Table structure for `product_types`
-- ----------------------------
DROP TABLE IF EXISTS `product_types`;
CREATE TABLE `product_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6iopyn5hbyxusogmmwjr5ci2q` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of product_types
-- ----------------------------
INSERT INTO `product_types` VALUES ('1', '传统口味的奶茶系列', '经典奶茶');
INSERT INTO `product_types` VALUES ('2', '新鲜水果制作的茶饮', '水果茶');
INSERT INTO `product_types` VALUES ('3', '各种咖啡饮品', '咖啡系列');
INSERT INTO `product_types` VALUES ('4', '带有奶盖的特色茶饮', '奶盖茶');
INSERT INTO `product_types` VALUES ('5', '根据季节推出的限定产品', '季节限定');

-- ----------------------------
-- Table structure for `product_type_mappings`
-- ----------------------------
DROP TABLE IF EXISTS `product_type_mappings`;
CREATE TABLE `product_type_mappings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhskcoi09dp1rsaixxvp06qxid` (`product_id`),
  KEY `FKjr4eejv0mh5jwo2p0mrfg8l3c` (`type_id`),
  CONSTRAINT `FKhskcoi09dp1rsaixxvp06qxid` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKjr4eejv0mh5jwo2p0mrfg8l3c` FOREIGN KEY (`type_id`) REFERENCES `product_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of product_type_mappings
-- ----------------------------
INSERT INTO `product_type_mappings` VALUES ('1', '1', '1');
INSERT INTO `product_type_mappings` VALUES ('2', '2', '4');
INSERT INTO `product_type_mappings` VALUES ('3', '3', '2');
INSERT INTO `product_type_mappings` VALUES ('4', '4', '3');
INSERT INTO `product_type_mappings` VALUES ('5', '5', '5');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `avatar_path` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_login_time` datetime(6) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '', '/uploads/avatars/2a44c6dc-a785-49c8-bc57-0cf65365b492.jpg', '2026-01-06 15:58:47.763146', '3158913286@qq.com', '2026-01-07 07:17:08.884808', 'hh', '2026-01-06 23:40:56.098211', 'hh');
