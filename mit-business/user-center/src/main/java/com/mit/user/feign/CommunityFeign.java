package com.mit.user.feign;

import com.mit.common.constant.ServiceNameConstant;
import com.mit.common.web.Result;
import com.mit.user.feign.fallback.CommunityFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Description 小区
 */
@FeignClient(value = ServiceNameConstant.COMMUNITY_SERVICE, path = "/api/web/communitywanli/", fallbackFactory = CommunityFallbackFactory.class)
public interface CommunityFeign {

    @PostMapping(value = "/personBaseInfo/getCommunitylist")
    Result<List> getCommunityList();
}
