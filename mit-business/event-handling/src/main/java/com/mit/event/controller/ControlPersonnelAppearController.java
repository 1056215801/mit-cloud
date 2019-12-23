package com.mit.event.controller;

import com.mit.common.web.Result;
import com.mit.event.dto.ControlPersonnelDTO;
import com.mit.event.service.IControlPersonnelAppearService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * @Description 布控人员出现事件controller
 */
@Slf4j
@Api(tags = "布控人员出现事件")
@RestController
@RequestMapping("/event/control-personnel")
public class ControlPersonnelAppearController {

    @Autowired
    private IControlPersonnelAppearService controlPersonnelAppearService;

    @ApiOperation(value = "生成布控人员出现事件")
    @PostMapping
    public Result genControlPersonnelEvent(@RequestBody @Valid ControlPersonnelDTO controlPersonnelDTO) {
        if (controlPersonnelDTO.getHappenedTime() == null) {
            controlPersonnelDTO.setHappenedTime(new Date());
        }
        try {
            controlPersonnelAppearService.process(controlPersonnelDTO);
        } catch (Exception e) {
            log.error("failed to generate event, info: {}, {}", controlPersonnelDTO.toString(), e.getMessage());
            return Result.failed(e.getMessage());
        }
        return Result.succeed();
    }
}
