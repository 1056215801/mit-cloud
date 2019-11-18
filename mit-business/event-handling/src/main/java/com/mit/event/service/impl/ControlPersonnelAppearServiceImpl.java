package com.mit.event.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.web.CodeEnum;
import com.mit.common.web.Result;
import com.mit.event.enums.EventDispositionTypeEnum;
import com.mit.event.enums.EventEnum;
import com.mit.event.dto.ControlPersonnelDTO;
import com.mit.event.enums.EventStatusEnum;
import com.mit.event.feign.ProcessInstanceFeign;
import com.mit.event.mapper.ControlPersonnelAppearMapper;
import com.mit.event.model.ControlPersonnelAppear;
import com.mit.event.model.EventBaseInfo;
import com.mit.event.model.EventConfig;
import com.mit.event.service.IControlPersonnelAppearService;
import com.mit.event.service.IEventBaseInfoService;
import com.mit.event.service.IEventConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 布控人员出现service
 */
@Service
public class ControlPersonnelAppearServiceImpl extends ServiceImpl<ControlPersonnelAppearMapper, ControlPersonnelAppear>
        implements IControlPersonnelAppearService {

    @Autowired
    private IEventBaseInfoService eventBaseInfoService;

    @Autowired
    private IEventConfigService eventConfigService;

    @Resource
    private ProcessInstanceFeign processInstanceFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process(ControlPersonnelDTO controlPersonnelDTO) throws Exception {
        EventConfig eventConfig = eventConfigService.getByEventCode(EventEnum.ControlPersonnelAppear.getCode());
        if (null == eventConfig) {
            throw new Exception("事件配置不存在，请检查事件标识");
        }
        // 保存事件信息
        EventBaseInfo eventBaseInfo = saveWithBaseInfo(controlPersonnelDTO, eventConfig);

        // 启动流程
        if (EventDispositionTypeEnum.Process.getCode().equals(eventConfig.getDispositionType())) {
            Result result = processInstanceFeign.startInstance(eventConfig.getProcessKey(), "");
            if (result.getResp_code().intValue() == CodeEnum.SUCCESS.getCode()) {
                eventBaseInfo.setProcessInstanceId((String) result.getDatas());
                // 事件状态变更为“处置中”
                eventBaseInfo.setStatus(EventStatusEnum.Disposal.getValue());
                eventBaseInfoService.updateById(eventBaseInfo);
            }
        }

        // 是否推送网格员
        if (eventConfig.isPush()) {

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EventBaseInfo saveWithBaseInfo(ControlPersonnelDTO controlPersonnelDTO, EventConfig eventConfig) {
        EventBaseInfo eventBaseInfo = new EventBaseInfo();
        BeanUtil.copyProperties(controlPersonnelDTO, eventBaseInfo);
        eventBaseInfo.setEventName(eventConfig.getEventName());
        eventBaseInfo.setEventCode(eventConfig.getEventCode());
        // 事件状态初始化为“未受理”
        eventBaseInfo.setStatus(EventStatusEnum.NotAccepted.getValue());
        eventBaseInfoService.saveWithAutoUuid(eventBaseInfo);

        String baseInfoId = eventBaseInfo.getId();

        ControlPersonnelAppear controlPersonnelAppear = new ControlPersonnelAppear(controlPersonnelDTO, baseInfoId);
        this.baseMapper.insert(controlPersonnelAppear);
        return eventBaseInfo;
    }

    @Override
    public ControlPersonnelAppear getByBaseInfoId(String baseInfoId) {
        QueryWrapper<ControlPersonnelAppear> wrapper = new QueryWrapper<>();
        wrapper.eq("event_base_info_id", baseInfoId);
        return this.baseMapper.selectOne(wrapper);
    }
}
