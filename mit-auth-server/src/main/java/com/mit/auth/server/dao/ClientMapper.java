package com.mit.auth.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.auth.server.model.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClientMapper extends BaseMapper<Client> {

    List<Client> findList(Page<Client> page, @Param("params") Map<String, Object> params);
}
