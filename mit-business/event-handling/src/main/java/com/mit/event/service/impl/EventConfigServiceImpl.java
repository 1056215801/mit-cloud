package com.mit.event.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.event.mapper.EventConfigMapper;
import com.mit.event.model.EventConfig;
import com.mit.event.service.IEventConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 事件配置service
 */
@Service
public class EventConfigServiceImpl extends ServiceImpl<EventConfigMapper, EventConfig> implements IEventConfigService {

    @Override
    public EventConfig getByEventCode(String eventCode) {
        QueryWrapper<EventConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("event_code", eventCode);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<EventConfig> getByEventSource(String eventSource) {
        QueryWrapper<EventConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("source", eventSource);
        return baseMapper.selectList(wrapper);
    }
}
