package com.mit.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.model.EventBaseInfo;
import com.mit.event.vo.MetricsVO;

import java.util.List;
import java.util.Map;

public interface IEventBaseInfoService extends IService<EventBaseInfo> {

    boolean saveWithAutoUuid(EventBaseInfo eventBaseInfo);

    List<EventBaseInfo> getBaseInfoByCondition(EventBaseInfoQueryDTO baseInfoQueryDTO);

    List<EventBaseInfo> getBaseInfoByEventSourceAndStatusIn(String eventSource, List<Integer> status);

    List<MetricsVO> metricsByStatus();

    List<MetricsVO> metricsByType();

    List<MetricsVO> metricsBySourceAndEmergencyLevel();

    List<MetricsVO> metricsBySourceAndStatusBetweenTime(String startTime, String endTime);

    List<MetricsVO> metricsByTypeBetweenTime(String startTime, String endTime);
}
