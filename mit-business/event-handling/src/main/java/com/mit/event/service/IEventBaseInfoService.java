package com.mit.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.model.EventBaseInfo;

import java.util.List;

public interface IEventBaseInfoService extends IService<EventBaseInfo> {

    boolean saveWithAutoUuid(EventBaseInfo eventBaseInfo);

    List<EventBaseInfo> getBaseInfoByCondition(EventBaseInfoQueryDTO baseInfoQueryDTO);

    List<EventBaseInfo> getBaseInfoByEventSourceAndStatusIn(String eventSource, List<Integer> status);
}
