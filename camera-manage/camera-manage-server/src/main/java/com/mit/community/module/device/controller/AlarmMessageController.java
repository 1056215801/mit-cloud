package com.mit.community.module.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.community.entity.hik.AlarmMessage;
import com.mit.community.service.com.mit.community.service.hik.AlarmMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 10:22 2019/12/19
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "报警信息")
@RequestMapping(value = "/alarmmessage")
public class AlarmMessageController {
     @Autowired
     private AlarmMessageService alarmMessageService;
    @PostMapping("/getAlarmMessageList")
    @ApiOperation("报警信息列表")
    public Result getAlarmMessageList(String serialNumber, Integer pageNum, Integer pageSize, String startTime,String endTime){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date start=null;
            Date end=null;
          try {
            if (StringUtils.isNotEmpty(startTime)) {
               start = sdf.parse(startTime);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                end = sdf.parse(endTime);
            }
          } catch (ParseException e) {
            e.printStackTrace();
          } finally {
          }
            IPage<AlarmMessage> page=new Page<>(pageNum,pageSize);
            QueryWrapper<AlarmMessage> wrapper=new QueryWrapper<>();
            if (StringUtils.isNotEmpty(serialNumber)) {
                wrapper.eq("serial_number",serialNumber);
            }
            if (start!=null) {
                wrapper.ge("alarm_time",start);
            }
            if (end!=null) {
                wrapper.le("alarm_time",end);
            }
            wrapper.orderByDesc("alarm_time");
            IPage<AlarmMessage> alarmMessageIPage = alarmMessageService.page(page, wrapper);
            return Result.succeed(alarmMessageIPage);
    }
     @PostMapping("/possess")
     @ApiOperation("报警信息处理")
     @ApiImplicitParam(name = "processInstructions",value = "信息处理说明",paramType = "query",dataType = "String")
     public Result possess(Integer id,String processInstructions){
         QueryWrapper<AlarmMessage> wrapper=new QueryWrapper<>();
         wrapper.eq("id",id);
         AlarmMessage alarmMessage = alarmMessageService.getOne(wrapper);
         alarmMessage.setProcessInstructions(processInstructions);
         alarmMessage.setProcessState(2);
         alarmMessageService.updateById(alarmMessage);
         return Result.succeed("处理成功");
     }
}
