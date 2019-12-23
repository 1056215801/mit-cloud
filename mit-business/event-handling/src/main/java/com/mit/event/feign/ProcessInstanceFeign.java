package com.mit.event.feign;

import com.mit.common.constant.ServiceNameConstant;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description 调用流程实例接口
 */
@FeignClient(value = ServiceNameConstant.WORKFLOW_SERVICE)
public interface ProcessInstanceFeign {

    @PostMapping(value = "/process-instance/start")
    Result startInstance(@RequestParam("procDefKey") String procDefKey, String formData);
}
