package com.mit.event.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.event.dto.ControlPersonnelDTO;
import com.mit.event.model.ControlPersonnelAppear;
import com.mit.event.model.EventBaseInfo;
import com.mit.event.model.EventConfig;

public interface IControlPersonnelAppearService extends IService<ControlPersonnelAppear> {

     void process(ControlPersonnelDTO controlPersonnelDTO) throws Exception;

     EventBaseInfo saveWithBaseInfo(ControlPersonnelDTO controlPersonnelDTO, EventConfig eventConfig);

     ControlPersonnelAppear getByBaseInfoId(String baseInfoId);

}
