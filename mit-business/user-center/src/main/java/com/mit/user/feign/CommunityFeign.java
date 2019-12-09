package com.mit.user.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.constant.ServiceNameConstant;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 小区
 */
@FeignClient(value = ServiceNameConstant.COMMUNITY_SERVICE, path = "/api/web/communitywanli/")
public interface CommunityFeign {

    @PostMapping(value = "/clusterCommunity/communityList")
    Result<Page> getCommunityList(@RequestParam("communityName") String communityName,
                                                    @RequestParam("communityCode") String communityCode,
                                                    @RequestParam("username") String username,
                                                    @RequestParam("provinceName") String provinceName,
                                                    @RequestParam("cityName") String cityName,
                                                    @RequestParam("areaName") String areaName,
                                                    @RequestParam("streetName") String streetName,
                                                    @RequestParam("committee") String committee,
                                                    @RequestParam("communityType") String communityType,
                                                    @RequestParam("pageNum") Integer pageNum,
                                                    @RequestParam("pageSize") Integer pageSize);
}
