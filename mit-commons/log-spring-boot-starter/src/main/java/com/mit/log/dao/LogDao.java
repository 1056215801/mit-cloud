package com.mit.log.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.log.model.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 保存日志
 * eureka-server配置不需要datasource,不会装配bean
 */
@Mapper
@ConditionalOnBean(DataSource.class)
public interface LogDao extends BaseMapper<SysLog> {

}
