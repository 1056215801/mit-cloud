package com.mit.event.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.enums.EventSourceEnum;
import com.mit.event.enums.EventStatusEnum;
import com.mit.event.enums.EventTypeEnum;
import com.mit.event.mapper.EventBaseInfoMapper;
import com.mit.event.model.EventBaseInfo;
import com.mit.event.service.IEventBaseInfoService;
import com.mit.event.vo.MetricsVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description event基础信息service
 */
@Service
public class EventBaseInfoServiceImpl extends ServiceImpl<EventBaseInfoMapper, EventBaseInfo>
        implements IEventBaseInfoService {

    @Override
    public boolean saveWithAutoUuid(EventBaseInfo eventBaseInfo) {
        return this.retBool(this.baseMapper.insertWithAutoUuid(eventBaseInfo));
    }

    @Override
    public List<EventBaseInfo> getBaseInfoByCondition(EventBaseInfoQueryDTO baseInfoQueryDTO) {
        return this.baseMapper.getBaseInfoByCondition(baseInfoQueryDTO);
    }

    @Override
    public List<EventBaseInfo> getBaseInfoByEventSourceAndStatusIn(String eventSource, List<Integer> status) {
        if (CollectionUtils.isEmpty(status)) {
            return this.baseMapper.getBaseInfoByEventSource(eventSource);
        }
        return this.baseMapper.getBaseInfoByEventSourceAndStatusIn(eventSource, status);
    }

    @Override
    public List<MetricsVO> metricsByStatus() {
        List<Map<String, Object>> list = this.baseMapper.metricsByStatus();
        List<MetricsVO> voList = new ArrayList<>();
        list.forEach(map -> {
            MetricsVO metricsVO = new MetricsVO();
            map.forEach((key, value) -> {
                switch (key) {
                    case "key":
                        metricsVO.setName(EnumUtil.likeValueOf(EventStatusEnum.class, value).getName());
                        metricsVO.setKey(String.valueOf(value));
                        break;
                    case "value":
                        metricsVO.setValue(String.valueOf(value));
                        break;
                }
            });
            voList.add(metricsVO);
        });
        return voList;
    }

    @Override
    public List<MetricsVO> metricsByType() {
        List<Map<String, Object>> list = this.baseMapper.metricsByType();
        List<MetricsVO> voList = new ArrayList<>();
        list.forEach(map -> {
            MetricsVO metricsVO = new MetricsVO();
            map.forEach((key, value) -> {
                switch (key) {
                    case "key":
                        metricsVO.setName(EnumUtil.likeValueOf(EventTypeEnum.class, value).getName());
                        metricsVO.setKey(String.valueOf(value));
                        break;
                    case "value":
                        metricsVO.setValue(String.valueOf(value));
                        break;
                }
            });
            voList.add(metricsVO);
        });
        return voList;
    }

    @Override
    public List<MetricsVO> metricsBySourceAndEmergencyLevel() {
        List<Map<String, Object>> list = this.baseMapper.metricsBySourceAndEmergencyLevel();
        Map<String, List<Map<String, Object>>> tempMap = new HashMap<>();
        list.forEach(map -> {
            map.forEach((key, value) -> {
                // source为mapper中查询语句定义的字段名
                if ("source".equals(key)) {
                    List<Map<String, Object>> tempList = tempMap.containsKey(value)
                            ? tempMap.get(value) : new ArrayList<>();
                    tempList.add(map);
                    tempMap.put(String.valueOf(value), tempList);
                }
            });
        });
        List<MetricsVO> voList = new ArrayList<>();
        tempMap.forEach((sourceCode, sourceList) -> {
            MetricsVO metricsVO = new MetricsVO();
            metricsVO.setName(EnumUtil.likeValueOf(EventSourceEnum.class, sourceCode).getName());
            metricsVO.setKey(sourceCode);
            AtomicInteger count = new AtomicInteger();
            List<MetricsVO> childrenList = new ArrayList<>();
            sourceList.forEach(map -> {
                MetricsVO child = new MetricsVO();
                map.forEach((key, value) -> {
                    switch (key) {
                        case "emergencyLevel":
                            child.setKey(String.valueOf(value));
                            break;
                        case "value":
                            child.setValue(String.valueOf(value));
                            count.addAndGet(Integer.parseInt(String.valueOf(value)));
                            break;
                    }
                });
                childrenList.add(child);
            });
            metricsVO.setValue(count.toString());
            metricsVO.setChildren(childrenList);
            voList.add(metricsVO);
        });
        return voList;
    }

    @Override
    public List<MetricsVO> metricsBySourceAndStatusBetweenTime(String startTime, String endTime) {
        List<Map<String, Object>> list = this.baseMapper.metricsBySourceAndStatusBetweenTime(startTime, endTime);
        Map<String, List<Map<String, Object>>> tempMap = new HashMap<>();
        list.forEach(map -> {
            map.forEach((key, value) -> {
                // source为mapper中查询语句定义的字段名
                if ("source".equals(key)) {
                    List<Map<String, Object>> tempList = tempMap.containsKey(value)
                            ? tempMap.get(value) : new ArrayList<>();
                    tempList.add(map);
                    tempMap.put(String.valueOf(value), tempList);
                }
            });
        });
        List<MetricsVO> voList = new ArrayList<>();
        tempMap.forEach((sourceCode, sourceList) -> {
            MetricsVO metricsVO = new MetricsVO();
            metricsVO.setName(EnumUtil.likeValueOf(EventSourceEnum.class, sourceCode).getName());
            metricsVO.setKey(sourceCode);
            AtomicInteger count = new AtomicInteger();
            List<MetricsVO> childrenList = new ArrayList<>();
            sourceList.forEach(map -> {
                MetricsVO child = new MetricsVO();
                map.forEach((key, value) -> {
                    switch (key) {
                        case "status":
                            child.setKey(String.valueOf(value));
                            child.setName(EnumUtil.likeValueOf(EventStatusEnum.class, value).getName());
                            break;
                        case "value":
                            child.setValue(String.valueOf(value));
                            count.addAndGet(Integer.parseInt(String.valueOf(value)));
                            break;
                    }
                });
                childrenList.add(child);
            });
            metricsVO.setValue(count.toString());
            metricsVO.setChildren(childrenList);
            voList.add(metricsVO);
        });
        return voList;
    }

    @Override
    public List<MetricsVO> metricsByTypeBetweenTime(String startTime, String endTime) {
        List<Map<String, Object>> list = this.baseMapper.metricsByTypeBetweenTime(startTime, endTime);
        Map<String, List<Map<String, Object>>> tempMap = new HashMap<>();
        list.forEach(map -> {
            map.forEach((key, value) -> {
                // type为mapper中查询语句定义的字段名
                if ("type".equals(key)) {
                    List<Map<String, Object>> tempList = tempMap.containsKey(value)
                            ? tempMap.get(value) : new ArrayList<>();
                    tempList.add(map);
                    tempMap.put(String.valueOf(value), tempList);
                }
            });
        });
        List<MetricsVO> voList = new ArrayList<>();
        tempMap.forEach((typeCode, sourceList) -> {
            MetricsVO metricsVO = new MetricsVO();
            metricsVO.setName(EnumUtil.likeValueOf(EventTypeEnum.class, typeCode).getName());
            metricsVO.setKey(typeCode);
            AtomicInteger count = new AtomicInteger();
            List<MetricsVO> childrenList = new ArrayList<>();
            sourceList.forEach(map -> {
                MetricsVO child = new MetricsVO();
                map.forEach((key, value) -> {
                    switch (key) {
                        case "time":
                            child.setKey(String.valueOf(value));
                            break;
                        case "value":
                            child.setValue(String.valueOf(value));
                            count.addAndGet(Integer.parseInt(String.valueOf(value)));
                            break;
                    }
                });
                childrenList.add(child);
            });
            metricsVO.setValue(count.toString());
            metricsVO.setChildren(childrenList);
            voList.add(metricsVO);
        });
        return voList;
    }

}
