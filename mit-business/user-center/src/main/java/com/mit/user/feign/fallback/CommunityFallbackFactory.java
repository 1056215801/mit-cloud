package com.mit.user.feign.fallback;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.user.feign.CommunityFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
            public Result<List> getCommunityList() {
                log.error("查询小区异常", throwable);
                return Result.succeed(new ArrayList());
            }
        };
    }
}
