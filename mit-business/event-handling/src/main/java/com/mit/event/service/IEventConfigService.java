package com.mit.event.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.event.model.EventConfig;

import java.util.List;

public interface IEventConfigService extends IService<EventConfig> {

    EventConfig getByEventCode(String eventCode);

    List<EventConfig> getByEventSource(String eventSource);
}
