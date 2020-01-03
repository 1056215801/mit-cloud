package com.mit.community.module.device.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mit.common.web.Result;
import com.mit.community.entity.ClusterCommunity;
import com.mit.community.entity.hik.SnapVehicle;
import com.mit.community.entity.hik.Vo.SnapImageVo;
import com.mit.community.entity.hik.Vo.SnapVehicleVo;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.service.com.mit.community.service.hik.SnapVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * @Author qishengjun
 * @Date Created in 16:31 2019/12/23
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "车辆抓拍")
@RequestMapping(value = "/snapVehicle")
public class SnapVehicleController {
    @Autowired
    private SnapVehicleService snapVehicleService;
    @Autowired
    private CommunityFeign communityFeign;
    @PostMapping("/getSnapVehicleList")
    @ApiOperation("获取抓拍车辆列表")
    public Result getSnapVehicleList(String communityCodes,String startTime,String endTime,
                                     String snapshotSite,String plateNo,String vehicleColor,
                                     String plateType,String plateColor,Integer pageSize,Integer pageNum){
        if (StringUtils.isEmpty(communityCodes)) {
            return Result.failed("查询失败");
        }
        IPage<SnapVehicleVo> page=snapVehicleService.getSnapVehicleList(communityCodes,startTime,endTime,snapshotSite,plateNo,vehicleColor,plateType,plateColor,pageSize,pageNum);
        Result result = communityFeign.getCommunitylist();
        List<ClusterCommunity> clusterCommunityList = (List<ClusterCommunity>) result.getDatas();
        String jsonString = JSON.toJSONString(clusterCommunityList);
        List<ClusterCommunity> clusterCommunities = parseArray(jsonString, ClusterCommunity.class);
        List<SnapVehicleVo> snapVehicleVoList = page.getRecords();
        for (SnapVehicleVo snapVehicleVo : snapVehicleVoList) {
            for (ClusterCommunity clusterCommunity : clusterCommunities) {
                if (snapVehicleVo.getCommunityCode().equalsIgnoreCase(clusterCommunity.getCommunityCode())){
                    snapVehicleVo.setAddress(clusterCommunity.getProvinceName()+clusterCommunity.getCityName()+clusterCommunity.getAreaName()+"-"+clusterCommunity.getAddress()+snapVehicleVo.getPlace());
                }
            }
        }
       page.setRecords(snapVehicleVoList);
        return Result.succeed(page);
    }

}
