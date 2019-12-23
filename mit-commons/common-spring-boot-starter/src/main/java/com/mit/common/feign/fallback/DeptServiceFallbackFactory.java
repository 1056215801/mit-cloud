package com.mit.common.feign.fallback;

import com.mit.common.feign.DeptServiceFeign;
import com.mit.common.model.SysDept;
import com.mit.common.web.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DeptServiceFallbackFactory implements FallbackFactory<DeptServiceFeign> {
    @Override
    public DeptServiceFeign create(Throwable throwable) {
        return new DeptServiceFeign() {
            @Override
            public Result<List<SysDept>> filterByDeptName(String name) {
                log.error("查询部门失败", throwable);
                return null;
            }
        };
    }
}
