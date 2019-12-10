package com.mit.user.feign.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.dto.ClusterCommunity;
import com.mit.common.model.SysUser;
import com.mit.common.web.Result;
import com.mit.user.feign.CommunityFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description
 */
@Slf4j
@Component
public class CommunityFallbackFactory implements FallbackFactory<CommunityFeign> {
    @Override
    public CommunityFeign create(Throwable throwable) {
        return new CommunityFeign() {
            @Override
            public Result<Page> getCommunityList(String communityName, String communityCode, String username, String provinceName, String cityName, String areaName, String streetName, String committee, String communityType, Integer pageNum, Integer pageSize) {
                log.error("查询小区异常:{}", username, throwable);
                return Result.succeed(new Page());
            }
        };
    }
}
