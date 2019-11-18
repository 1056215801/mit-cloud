CREATE DATABASE IF NOT EXISTS `mit-event-handling` DEFAULT CHARACTER SET = utf8;
Use `mit-event-handling`;

-- ----------------------------
-- Table structure for event_config
-- 事件配置
-- ----------------------------
DROP TABLE IF EXISTS `event_config`;
CREATE TABLE `event_config`(
    `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `event_name` varchar(64) NOT NULL COMMENT '事件名称',
    `event_code` varchar(64) NOT NULL COMMENT '事件标识',
    `classification` varchar(32) DEFAULT NULL COMMENT '事件分类',
    `type` varchar(32) DEFAULT NULL COMMENT '事件类型',
    `source` varchar(32) DEFAULT NULL COMMENT '事件来源',
    `disposition_type` varchar(32) DEFAULT NULL COMMENT '事件处置类型',
    `is_push` tinyint(1) DEFAULT 0 COMMENT '是否推送网格',
    `push_content` varchar(255) DEFAULT NULL COMMENT '推送内容',
    `process_key` varchar(64) DEFAULT NULL COMMENT '对应流程key',
    PRIMARY KEY (`id`),
    KEY `idx_event_code` (`event_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='事件配置表';

-- ----------------------------
-- Records of event_config
-- ----------------------------
INSERT INTO `event_config` VALUES (1, '布控人员出现', 'CONTROL_PERSONNEL', 'PUBLIC_MANAGEMENT', 'PEACE_MANAGEMENT', 'ACTIVE_DISCOVERY', 'PROCESS', 1, '', 'test');

-- ----------------------------
-- Table structure for event_base_info
-- 事件基础信息
-- ----------------------------
DROP TABLE IF EXISTS `event_base_info`;
CREATE TABLE `event_base_info`(
   `id` varchar(64) NOT NULL COMMENT '主键',
   `event_name` varchar(64) NOT NULL COMMENT '事件名称',
   `event_code` varchar(64) NOT NULL COMMENT '事件标识',
   `happened_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '事件触发时间',
   `emergency_level` tinyint(1) DEFAULT NULL COMMENT '事件紧急程度',
   `severity` tinyint(1) DEFAULT NULL COMMENT '事件严重程度',
   `community_code` varchar(64) DEFAULT NULL COMMENT '事件发生所在小区code',
   `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
   `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
   `status` tinyint(1) DEFAULT NULL COMMENT '事件状态',
   `process_instance_id` varchar(64) DEFAULT NULL COMMENT '对应流程实例id',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='事件基础信息表';

-- ----------------------------
-- Table structure for event_control_personnel_appear
-- 布控人员出现事件
-- ----------------------------
DROP TABLE IF EXISTS `event_control_personnel_appear`;
CREATE TABLE `event_control_personnel_appear`(
  `id` varchar(64) NOT NULL COMMENT '主键',
  `event_base_info_id` varchar(64) NOT NULL COMMENT '关联的基础信息表ID',
  `capture_image_url` varchar(255) DEFAULT NULL COMMENT '抓拍图片地址',
  `capture_time` datetime DEFAULT NULL COMMENT '抓拍时间',
  `username` varchar(64) DEFAULT NULL COMMENT '姓名',
  `sex` varchar(8) DEFAULT NULL COMMENT '性别',
  `certificate_type` varchar(16) DEFAULT NULL COMMENT '证件类型',
  `certificate_number` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `person_type` varchar(16) DEFAULT NULL COMMENT '人员类型',
  `device_name` varchar(128) DEFAULT NULL COMMENT '设备名称',
  `device_code` varchar(128) DEFAULT NULL COMMENT '设备编号',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`event_base_info_id`) REFERENCES `event_base_info`(id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='布控人员出现事件表';

