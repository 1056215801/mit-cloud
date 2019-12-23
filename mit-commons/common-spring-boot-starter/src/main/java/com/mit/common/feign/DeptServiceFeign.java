package com.mit.common.feign;

import com.mit.common.constant.ServiceNameConstant;
import com.mit.common.feign.fallback.DeptServiceFallbackFactory;
import com.mit.common.model.SysDept;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 部门外部调用
 */
@FeignClient(value = ServiceNameConstant.USER_SERVICE, fallbackFactory = DeptServiceFallbackFactory.class)
public interface DeptServiceFeign {

    @GetMapping(value = "/dept/filter")
    Result<List<SysDept>> filterByDeptName(@RequestParam(value = "name", required = false) String name);
}
