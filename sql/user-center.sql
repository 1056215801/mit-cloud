CREATE DATABASE IF NOT EXISTS `mit-user-center` DEFAULT CHARACTER SET = utf8;
Use `mit-user-center`;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '用户名（登录账号）',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `nickname` varchar(255) DEFAULT NULL COMMENT '姓名',
  `head_img_url` varchar(1024) DEFAULT NULL COMMENT '头像地址',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(64) DEFAULT NULL COMMENT '电子邮箱',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别，0男 1女',
  `is_system_user` tinyint(1) DEFAULT 1 COMMENT '是否系统用户，1是 0不是',
  `user_type` int(2) DEFAULT NULL COMMENT '用户类型',
  `dept_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `position` varchar(255) DEFAULT NULL COMMENT '职务',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '用户状态，0禁用 1正常',
  `level` int(2) DEFAULT NULL COMMENT '账号级别',
  `province_code` varchar(32) DEFAULT NULL COMMENT '省编码',
  `province_name` varchar(32) DEFAULT NULL COMMENT '省名称',
  `city_code` varchar(32) DEFAULT NULL COMMENT '市编码',
  `city_name` varchar(32) DEFAULT NULL COMMENT '市名称',
  `area_code` varchar(32) DEFAULT NULL COMMENT '区/县编码',
  `area_name` varchar(32) DEFAULT NULL COMMENT '区/县名称',
  `street_code` varchar(32) DEFAULT NULL COMMENT '镇/街道办编码',
  `street_name` varchar(32) DEFAULT NULL COMMENT '镇/街道办名称',
  `committee_code` varchar(32) DEFAULT NULL COMMENT '居委会编码',
  `committee_name` varchar(32) DEFAULT NULL COMMENT '居委会名称',
  `community_code` varchar(32) DEFAULT NULL COMMENT '小区编码',
  `community_name` varchar(32) DEFAULT NULL COMMENT '小区名称',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `last_login_time` datetime NULL COMMENT '最后一次登录时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$TJkwVdlpbHKnV45.nBxbgeFHmQRmyWlshg94lFu2rKxVtT2OMniDO', '管理员', '', NULL, NULL, NULL, 1, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) NOT NULL COMMENT '角色标识',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'ADMIN', '管理员', NULL, CURRENT_TIMESTAMP, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(32) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '菜单名称',
  `type` tinyint(1) NOT NULL COMMENT '菜单类型',
  `path` varchar(1024) DEFAULT NULL COMMENT '前端路由URL',
  `permission_code` varchar(64) DEFAULT NULL COMMENT '权限标识',
  `parent_id` int(11) NOT NULL COMMENT '父级ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) NOT NULL COMMENT '排序值',
  `hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否隐藏，1隐藏',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES
(1, '系统管理', 1, '#!sys', NULL, -1, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(2, '用户管理', 1, '#!sys/user', NULL, 1, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(3, '增加用户', 2, NULL, 'sys:user:add', 2, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(4, '修改用户', 2, NULL, 'sys:user:edit', 2, 'layui-icon-friends', 2, 0, CURRENT_TIMESTAMP, NULL),
(5, '删除用户', 2, NULL, 'sys:user:del', 2, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(6, '菜单管理', 1, '#!sys/menu', NULL, 1, 'layui-icon-friends', 2, 0, CURRENT_TIMESTAMP, NULL),
(7, '增加菜单', 2, NULL, 'sys:menu:add', 6, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(8, '修改菜单', 2, NULL, 'sys:menu:edit', 6, 'layui-icon-friends', 2, 0, CURRENT_TIMESTAMP, NULL),
(9, '删除菜单', 2, NULL, 'sys:menu:del', 6, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(10, '组织机构', 1, '#!sys/dept', NULL, 1, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(11, '增加组织', 2, NULL, 'sys:dept:add', 9, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(12, '修改组织', 2, NULL, 'sys:dept:edit', 9, 'layui-icon-friends', 2, 0, CURRENT_TIMESTAMP, NULL),
(13, '删除组织', 2, NULL, 'sys:dept:del', 9, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(14, '角色管理', 1, '#!sys/role', NULL, 1, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(15, '增加角色', 2, NULL, 'sys:role:add', 14, 'layui-icon-friends', 1, 0, CURRENT_TIMESTAMP, NULL),
(16, '修改角色', 2, NULL, 'sys:role:edit', 14, 'layui-icon-friends', 2, 0, CURRENT_TIMESTAMP, NULL),
(17, '删除角色', 2, NULL, 'sys:role:del', 14, 'layui-icon-friends', 3, 0, CURRENT_TIMESTAMP, NULL),
(18, '角色授权', 2, NULL, 'sys:role:auth', 14, 'layui-icon-friends', 4, 0, CURRENT_TIMESTAMP, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单权限ID',
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18);
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(50) NOT NULL COMMENT '机构名称',
    `parent_id` int(11) NOT NULL COMMENT '父级机构ID',
    `sort` int(11) NOT NULL COMMENT '排序',
    `remark` varchar(50) DEFAULT NULL COMMENT '描述',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='组织机构表';

-- ----------------------------
-- Record of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES
('1', '东湖区', '-1', '1', NULL, CURRENT_TIMESTAMP, NULL),
('2', '东湖区公安局', '1', '1', NULL, CURRENT_TIMESTAMP, NULL),
('3', '东湖区扫黑办', '1', '2', NULL, CURRENT_TIMESTAMP, NULL),
('4', '东湖区信访局', '1', '3', NULL, CURRENT_TIMESTAMP, NULL),
('5', '东湖区教育局', '1', '4', NULL, CURRENT_TIMESTAMP, NULL),
('6', '东湖区民政局', '1', '5', NULL, CURRENT_TIMESTAMP, NULL),
('7', '东湖区司法局', '1', '6', NULL, CURRENT_TIMESTAMP, NULL),
('8', '东湖区卫计委', '1', '7', NULL, CURRENT_TIMESTAMP, NULL),
('9', '东湖区市场和质量监督管理局', '1', '8', NULL, CURRENT_TIMESTAMP, NULL),
('10', '东湖区安监局', '1', '9', NULL, CURRENT_TIMESTAMP, NULL),
('11', '东湖区团区委', '1', '10', NULL, CURRENT_TIMESTAMP, NULL),
('12', '东湖区人力资源和社会保障局', '1', '11', NULL, CURRENT_TIMESTAMP, NULL),
('13', '东湖区环保局', '1', '12', NULL, CURRENT_TIMESTAMP, NULL),
('14', '东湖区城市建设局', '1', '13', NULL, CURRENT_TIMESTAMP, NULL),
('15', '东湖区城市管理局', '1', '14', NULL, CURRENT_TIMESTAMP, NULL),
('16', '东湖区住房保障和房产管理局', '1', '15', NULL, CURRENT_TIMESTAMP, NULL),
('17', '东湖区文化广电旅游新闻出版局', '1', '16', NULL, CURRENT_TIMESTAMP, NULL),
('18', '东湖区综治委', '1', '17', NULL, CURRENT_TIMESTAMP, NULL),
('19', '东湖区综治办', '1', '18', NULL, CURRENT_TIMESTAMP, NULL),
('20', '东湖区综治中心', '1', '19', NULL, CURRENT_TIMESTAMP, NULL),
('21', '东湖区人民法院', '1', '20', NULL, CURRENT_TIMESTAMP, NULL),
('22', '公园街道办事处', '1', '21', NULL, CURRENT_TIMESTAMP, NULL),
('23', '滕王阁街道', '1', '22', NULL, CURRENT_TIMESTAMP, NULL),
('24', '八一桥街道', '1', '23', NULL, CURRENT_TIMESTAMP, NULL),
('25', '百花洲街道', '1', '24', NULL, CURRENT_TIMESTAMP, NULL),
('26', '墩子塘街道', '1', '25', NULL, CURRENT_TIMESTAMP, NULL),
('27', '大院街道', '1', '26', NULL, CURRENT_TIMESTAMP, NULL),
('28', '豫章街道', '1', '27', NULL, CURRENT_TIMESTAMP, NULL),
('29', '董家窑街道', '1', '28', NULL, CURRENT_TIMESTAMP, NULL),
('30', '彭家桥街道', '1', '29', NULL, CURRENT_TIMESTAMP, NULL),
('31', '扬农管理处', '1', '30', NULL, CURRENT_TIMESTAMP, NULL),
('32', '扬子洲镇', '1', '31', NULL, CURRENT_TIMESTAMP, NULL),
('33', '贤士湖管理处', '1', '32', NULL, CURRENT_TIMESTAMP, NULL),
('34', '民巷社区', '23', '1', NULL, CURRENT_TIMESTAMP, NULL),
('35', '章江路社区', '23', '2', NULL, CURRENT_TIMESTAMP, NULL),
('36', '上凤凰坡社区', '23', '3', NULL, CURRENT_TIMESTAMP, NULL),
('37', '滕王阁社区', '23', '4', NULL, CURRENT_TIMESTAMP, NULL),
('38', '射步亭社区', '23', '5', NULL, CURRENT_TIMESTAMP, NULL),
('39', '李家巷社区', '23', '6', NULL, CURRENT_TIMESTAMP, NULL),
('40', '子固路社区', '23', '7', NULL, CURRENT_TIMESTAMP, NULL),
('41', '铁街社区', '23', '8', NULL, CURRENT_TIMESTAMP, NULL),
('42', '榕门路社区', '23', '9', NULL, CURRENT_TIMESTAMP, NULL),
('43', '民巷社区01', '34', '1', NULL, CURRENT_TIMESTAMP, NULL),
('44', '民巷社区02', '34', '2', NULL, CURRENT_TIMESTAMP, NULL);
COMMIT;

-- ----------------------------
--  Table structure for `sys_dept_relation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
    `ancestor` int(11) NOT NULL COMMENT '祖先节点',
    `descendant` int(11) NOT NULL COMMENT '后代节点',
    PRIMARY KEY (`ancestor`,`descendant`),
    KEY `idx1` (`ancestor`),
    KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='部门关系表';

-- ----------------------------
--  Records of `sys_dept_relation`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES
('1', '1'), ('1', '2'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('1', '7'), ('1', '8'), ('1', '9'), ('1', '10'),
('1', '11'), ('1', '12'), ('1', '13'), ('1', '14'), ('1', '15'), ('1', '16'), ('1', '17'), ('1', '18'), ('1', '19'), ('1', '20'),
('1', '21'), ('1', '22'), ('1', '23'), ('1', '24'), ('1', '25'), ('1', '26'), ('1', '27'), ('1', '28'), ('1', '29'), ('1', '30'),
('1', '31'), ('1', '32'), ('1', '33'),
('2', '2'),  ('3', '3'), ('4', '4'), ('5', '5'), ('6', '6'), ('7', '7'), ('8', '8'), ('9', '9'), ('10', '10'), ('11', '11'),
('12', '12'),  ('13', '13'), ('14', '14'), ('15', '15'), ('16', '16'), ('17', '17'), ('18', '18'), ('19', '19'), ('20', '20'), ('21', '21'),
('22', '22'),
('23', '23'), ('23', '34'), ('23', '35'), ('23', '36'), ('23', '37'), ('23', '38'), ('23', '39'), ('23', '40'), ('23', '41'), ('23', '42'),
('24', '24'), ('25', '25'), ('26', '26'), ('27', '27'), ('28', '28'), ('29', '29'), ('30', '30'), ('31', '31'),
('32', '32'),  ('33', '33'),
('34', '34'), ('34', '43'), ('34', '44'),
('35', '35'), ('36', '36'), ('37', '37'), ('38', '38'), ('39', '39'), ('40', '40'), ('41', '41'), ('42', '42'), ('43', '43'), ('44', '44');
COMMIT;