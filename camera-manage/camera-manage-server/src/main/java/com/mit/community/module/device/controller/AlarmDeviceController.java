package com.mit.community.module.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.community.entity.hik.AlarmDevice;
import com.mit.community.entity.hik.AlarmMessage;
import com.mit.community.entity.hik.EventInfoDTO;
import com.mit.community.feign.EventFeign;
import com.mit.community.service.com.mit.community.service.hik.AlarmDeviceService;
import com.mit.community.service.com.mit.community.service.hik.AlarmMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 11:53 2019/12/18
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "报警设备")
@RequestMapping(value = "/alarmDevice")
public class AlarmDeviceController {
    @Autowired
    private AlarmDeviceService alarmDeviceService;
    @Autowired
    private AlarmMessageService alarmMessageService;
    @Autowired
    private EventFeign eventFeign;
    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者更新")
    public Result saveOrUpdate(AlarmDevice alarmDevice){
        String serialNumber = alarmDevice.getSerialNumber();
        if (alarmDevice==null) {
            return Result.failed("新增或者更新失败");
        }
        alarmDevice.setModifiedTime(new Date());
        if (alarmDevice.getId()==null) {
            QueryWrapper<AlarmDevice> wrapper=new QueryWrapper<>();
            wrapper.eq("serial_number",serialNumber);
            List<AlarmDevice> list = alarmDeviceService.list(wrapper);
            if (list.size()>0) {
                return Result.failed("该设备已添加，请勿重复添加");
            }
            alarmDevice.setCreateTime(new Date());
            alarmDevice.setDeviceStatus(1);
        }
        boolean b = alarmDeviceService.saveOrUpdate(alarmDevice);
        if (b) {
            return Result.succeed("新增或者更新成功");
        }else {
            return Result.failed("新增或者更新失败");
        }
        }
        @PostMapping("/delete")
        @ApiOperation("删除")
        @ApiImplicitParam(name = "ids",value = "id",paramType = "query",dataType = "String")
        public Result delete(String ids){
            if (StringUtils.isNotEmpty(ids)) {
                String[] split = ids.split(",");
                List<String> list = Arrays.asList(split);
                boolean b = alarmDeviceService.removeByIds(list);
                if (b){
                    return Result.succeed("删除成功");
                }
            }
            return Result.failed("删除失败");
        }
        @PostMapping("/getAlarmDeviceList")
        public Result getAlarmDeviceList(String communityCodes,Integer zoneId,String deviceName,
                                         String deviceNumber,Integer deviceType,Integer deviceStatus,Integer pageNum,Integer pageSize){
            if (StringUtils.isEmpty(communityCodes)) {
                return Result.failed("查询失败");
            }
                String[] split = communityCodes.split(",");
            List<String> list = Arrays.asList(split);
            IPage<AlarmDevice> page=new Page<>(pageNum,pageSize);
            QueryWrapper<AlarmDevice> queryWrapper=new QueryWrapper<>();
            if (zoneId!=null) {
                queryWrapper.eq("zone_id",zoneId);
            }
            if (StringUtils.isNotEmpty(deviceName)) {
                queryWrapper.like("device_name",deviceName);
            }
            if (StringUtils.isNotEmpty(deviceNumber)) {
                queryWrapper.like("device_number",deviceNumber);
            }
            if (deviceType!=null) {
                queryWrapper.eq("device_type",deviceType);
            }
            if (deviceStatus!=null) {
                queryWrapper.eq("device_status",deviceStatus);
            }
            queryWrapper.in("community_code",list);
            queryWrapper.orderByDesc("create_time");
            IPage<AlarmDevice> deviceIPage = alarmDeviceService.page(page, queryWrapper);
            return Result.succeed(deviceIPage);
        }
        @PostMapping("/getAlarmInfo")
        public void getAlarmInfo(String serialNumber){
            System.out.println("获取报警信息序列号"+serialNumber);
            AlarmMessage alarmMessage=new AlarmMessage();
            QueryWrapper<AlarmDevice> wrapper=new QueryWrapper<>();
            wrapper.eq("serial_number",serialNumber);
            AlarmDevice alarmDevice = alarmDeviceService.getOne(wrapper);
            String communityCode = alarmDevice.getCommunityCode();
            String communityName = alarmDevice.getCommunityName();
            String zoneName = alarmDevice.getZoneName();
            Integer zoneId = alarmDevice.getZoneId();
            String deviceLocation = alarmDevice.getDeviceLocation();
            String geographicCoordinates = alarmDevice.getGeographicCoordinates();
            String longitude="";
            String latitude="";
            if (StringUtils.isNotBlank(geographicCoordinates)) {
                String[] split = geographicCoordinates.split(",");
                longitude=split[0];
                latitude=split[1];
            }
            EventInfoDTO eventInfoDTO=new EventInfoDTO(communityCode,communityName,1,"ALARM_BOX_ALARM","",new Date(),latitude,deviceLocation,longitude,1,String.valueOf(zoneId),zoneName);
            eventFeign.handing(eventInfoDTO);
            if (alarmDevice!=null) {
                alarmMessage.setAlarmLocation(alarmDevice.getDeviceLocation());
                alarmMessage.setDeviceName(alarmDevice.getDeviceName());
                alarmMessage.setDeviceNumber(alarmDevice.getDeviceNumber());
                alarmMessage.setAlarmTime(new Date());
                alarmMessage.setProcessState(1);
                alarmMessage.setSerialNumber(serialNumber);
                alarmMessageService.save(alarmMessage);
            }
        }
}
