package com.mit.event.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.mapper.EventBaseInfoMapper;
import com.mit.event.model.EventBaseInfo;
import com.mit.event.service.IEventBaseInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

}
