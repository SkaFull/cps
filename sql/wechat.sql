-- ============================================================
-- 微信小程序一键登录功能 - 数据库脚本
-- 创建时间: 2026-03-13
-- 说明: 包含微信用户关联、淘宝客代理申请、小程序会话、登录记录等表
-- ============================================================

-- ----------------------------
-- 1. 微信用户关联表 (仅代理人员使用)
-- ----------------------------
CREATE TABLE `sys_wechat_user` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
   `user_id` bigint(20) NOT NULL COMMENT '用户ID',
   `openid` varchar(100) NOT NULL COMMENT '微信openid',
   `unionid` varchar(100) DEFAULT NULL COMMENT '微信unionid',
   `nickname` varchar(100) DEFAULT NULL COMMENT '微信昵称',
   `avatar` varchar(500) DEFAULT NULL COMMENT '微信头像',
   `gender` varchar(1) DEFAULT NULL COMMENT '性别',
   `province` varchar(50) DEFAULT NULL COMMENT '省份',
   `city` varchar(50) DEFAULT NULL COMMENT '城市',
   `country` varchar(50) DEFAULT NULL COMMENT '国家',
   `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `remark` varchar(500) DEFAULT NULL COMMENT '备注',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_openid` (`openid`),
   KEY `idx_user_id` (`user_id`),
   KEY `idx_unionid` (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户关联表';

-- ----------------------------
-- 2. 淘宝客代理申请表 (新增，仅代理人员可使用)
-- ----------------------------
CREATE TABLE `sys_tbk_agent_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `mini_app_id` varchar(100) NOT NULL COMMENT '微信小程序AppID',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `wechat` varchar(50) NOT NULL COMMENT '微信号',
  `app_key` varchar(100) NOT NULL COMMENT '淘宝客AppKey',
  `app_secret` varchar(200) NOT NULL COMMENT '淘宝客AppSecret',
  `adzone_id` varchar(50) NOT NULL COMMENT '推广位ID',
  `invitation_code` varchar(20) DEFAULT NULL COMMENT '代理专属邀请码',
  `status` char(1) DEFAULT '0' COMMENT '申请状态(0-待审核,1-已生效,2-已失效)',
  `audit_by` varchar(64) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mini_app_id` (`mini_app_id`),
  UNIQUE KEY `uk_invitation_code` (`invitation_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='淘宝客代理申请表';

-- ----------------------------
-- 3. 小程序会话表 (新增，仅代理人员使用)
-- ----------------------------
CREATE TABLE `sys_miniapp_session` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `session_key` varchar(100) NOT NULL COMMENT '微信会话密钥',
  `openid` varchar(100) NOT NULL COMMENT '用户OpenID',
  `unionid` varchar(100) DEFAULT NULL COMMENT '用户UnionID',
  `token` varchar(500) NOT NULL COMMENT 'JWT Token',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_token` (`token`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序会话表';

-- ----------------------------
-- 4. 用户登录记录表 (新增，仅代理人员登录时记录)
-- ----------------------------
CREATE TABLE `sys_miniapp_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `openid` varchar(100) NOT NULL COMMENT '微信OpenID',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(128) DEFAULT NULL COMMENT '登录IP',
  `login_location` varchar(255) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型(miniapp-小程序)',
  `device_brand` varchar(50) DEFAULT NULL COMMENT '设备品牌',
  `device_model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `mini_app_version` varchar(50) DEFAULT NULL COMMENT '小程序版本',
  `wx_version` varchar(50) DEFAULT NULL COMMENT '微信版本',
  `status` char(1) DEFAULT '0' COMMENT '登录状态(0-成功,1-失败)',
  `msg` varchar(255) DEFAULT NULL COMMENT '提示消息',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_openid` (`openid`),
  KEY `idx_login_time` (`login_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序登录记录表';

-- ----------------------------
-- 5. 数据字典配置
-- ----------------------------
-- 申请状态字典
INSERT INTO sys_dict_type VALUES (100, '申请状态', 'sys_tbk_apply_status', '0', 'admin', sysdate(), '', NULL, '代理申请状态');
INSERT INTO sys_dict_data VALUES (1000, 1, '待审核', '0', 'sys_tbk_apply_status', '', 'info', 'N', '0', 'admin', sysdate(), '', NULL, '待审核状态');
INSERT INTO sys_dict_data VALUES (1001, 2, '已生效', '1', 'sys_tbk_apply_status', '', 'success', 'N', '0', 'admin', sysdate(), '', NULL, '已生效状态');
INSERT INTO sys_dict_data VALUES (1002, 3, '已失效', '2', 'sys_tbk_apply_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', NULL, '已失效状态');
