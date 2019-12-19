CREATE DATABASE IF NOT EXISTS `mit-iot` DEFAULT CHARACTER SET = utf8;
Use `mit-iot`;

-- ----------------------------
-- Table structure for base_device_info
-- ----------------------------
DROP TABLE IF EXISTS `base_device_info`;
CREATE TABLE `base_device_info` (
    `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `community_code` varchar(64) DEFAULT NULL COMMENT '所属小区编码',
    `community_name` varchar(64) DEFAULT NULL COMMENT '所属小区名称',
    `zone_id` varchar(64) DEFAULT NULL COMMENT '分区ID',
    `zone_name` varchar(64) DEFAULT NULL COMMENT '分区名称',
    `device_name` varchar(64) NOT NULL COMMENT '设备名称',
    `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型',
    `device_no` varchar(64) DEFAULT NULL COMMENT '设备编号',
    `device_serial_no` varchar(64) DEFAULT NULL COMMENT '设备序列号',
    `device_location` varchar(128) DEFAULT NULL COMMENT '设备位置',
    `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
    `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
    `status` int(2) NOT NULL DEFAULT 1 COMMENT '设备状态，0异常 1正常',
    `acquisition_time` datetime DEFAULT NULL COMMENT '上报时间',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='设备基础信息表';

-- ----------------------------
-- Table structure for wifi_probe
-- ----------------------------
DROP TABLE IF EXISTS `wifi_probe`;
CREATE TABLE `wifi_probe` (
    `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `base_device_info_id` int(32) NOT NULL COMMENT '关联base_device_info表的id',
    `index_code` varchar(64) DEFAULT NULL COMMENT 'wifi探针平台索引',
    `mac` varchar(32) DEFAULT NULL COMMENT '设备Mac地址',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_index_code` (`index_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='wifi探针设备信息表';

-- ----------------------------
-- Table structure for wifi_probe_terminal
-- ----------------------------
DROP TABLE IF EXISTS `wifi_probe_terminal`;
CREATE TABLE `wifi_probe_terminal` (
    `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `terminal_mac` varchar(32) DEFAULT NULL COMMENT '终端Mac地址',
    `first_acquisition_time` datetime DEFAULT NULL COMMENT '第一次被采集的时间',
    `last_acquisition_time` datetime DEFAULT NULL COMMENT '最后一次被采集的时间',
    `scan_time` int(16) DEFAULT NULL COMMENT '被扫描次数',
    `wifi_field_intensity` int(4) DEFAULT NULL COMMENT '信号强度',
    `index_code` varchar(64) DEFAULT NULL COMMENT 'wifi探针平台索引',
    `connected_ap_mac` varchar(32) DEFAULT NULL COMMENT '连接的热点mac地址',
    `phone_brand` varchar(32) DEFAULT NULL COMMENT '手机品牌',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_mac` (`terminal_mac`),
    KEY `idx_index_code` (`index_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='wifi探针上传终端信息表';

-- ----------------------------
-- Table structure for wifi_probe_ap
-- ----------------------------
DROP TABLE IF EXISTS `wifi_probe_ap`;
CREATE TABLE `wifi_probe_ap` (
    `id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `ap_mac` varchar(32) DEFAULT NULL COMMENT '热点Mac地址',
    `first_acquisition_time` datetime DEFAULT NULL COMMENT '第一次被采集的时间',
    `last_acquisition_time` datetime DEFAULT NULL COMMENT '最后一次被采集的时间',
    `scan_time` int(16) DEFAULT NULL COMMENT '被扫描次数',
    `wifi_field_intensity` int(4) DEFAULT NULL COMMENT '信号强度',
    `wifi_spot_encrypt_type` varchar(16) DEFAULT NULL COMMENT '加密方式',
    `channel` int(32) DEFAULT NULL COMMENT '热点频道',
    `ssid` varchar(64) DEFAULT NULL COMMENT '热点名称',
    `index_code` varchar(64) DEFAULT NULL COMMENT 'wifi探针平台索引',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_mac` (`ap_mac`),
    KEY `idx_index_code` (`index_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=Dynamic COMMENT='wifi探针上传AP信息表';

