###日志表
CREATE DATABASE IF NOT EXISTS `mit-log-center` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `mit-log-center`;
SET FOREIGN_KEY_CHECKS=0;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime NOT NULL COMMENT '日志创建时间',
  `url` varchar(255) COMMENT '请求路径',
  `http_method` varchar(8) COMMENT '请求方式',
  `username` varchar(50) COMMENT '操作用户名',
  `ip` varchar(18) COMMENT '客户端IP',
  `module` varchar(255) COMMENT '模块名',
  `params` text COMMENT '方法参数',
  `remark` text COMMENT '错误信息',
  `flag` tinyint(1) COMMENT '执行结果',
  `time` int(32) COMMENT '响应时间',
  PRIMARY KEY (`id`) 
) ENGINE=archive AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;