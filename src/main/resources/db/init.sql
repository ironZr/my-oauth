CREATE TABLE `sso_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL COMMENT '登录名',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `is_disabled` char(1) DEFAULT '1' COMMENT '是否禁用  1正常 2禁用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`) USING BTREE COMMENT 'uk_account'
) ENGINE=InnoDB AUTO_INCREMENT=1590550790383968259 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';



CREATE TABLE `oauth_app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `company_name` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `app_id` varchar(100) NOT NULL COMMENT '接入的客户端ID',
  `app_name` varchar(100) DEFAULT NULL COMMENT '客户端名称',
  `app_secret` varchar(255) NOT NULL COMMENT '接入的客户端的密钥',
  `app_uri` varchar(1000) DEFAULT NULL COMMENT '客户端index地址',
  `description` varchar(1000) DEFAULT NULL COMMENT '描述信息',
  `status` char(1) DEFAULT '1' COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_user` varchar(255) DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_app_id` (`app_id`) USING BTREE COMMENT 'uk_app_id',
  UNIQUE KEY `uk_app_name` (`app_name`) USING BTREE COMMENT 'uk_app_name'
) ENGINE=InnoDB AUTO_INCREMENT=1590269156111777795 DEFAULT CHARSET=utf8 COMMENT='接入的客户端信息表';