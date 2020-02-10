package com.mit.community.feign;

import com.mit.common.web.Result;
import com.mit.community.entity.hik.EventInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author qishengjun
 * @Date Created in 16:54 2020/1/8
 * @Company: mitesofor </p>
 * @Description:~
 */
@FeignClient(value = "EVENT-HANDLING")
public interface EventFeign {
    @PostMapping("/event/gen")
    public Result handing(@RequestBody EventInfoDTO eventInfoDTO);
}
