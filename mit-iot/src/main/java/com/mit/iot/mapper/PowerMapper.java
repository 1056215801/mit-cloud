package com.mit.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.iot.model.ConfigInfo;
import org.mapstruct.Mapper;

@Mapper
public interface PowerMapper extends BaseMapper<ConfigInfo> {
}
