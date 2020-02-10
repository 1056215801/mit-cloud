package com.mit.community.feign;

import com.mit.common.dto.LoginAppUser;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author qishengjun
 * @Date Created in 16:53 2019/10/18
 * @Company: mitesofor </p>
 * @Description:~
 */
@FeignClient(value = "user-center")
@Component
public interface SysUserFeign {

    @GetMapping("/info")
    public Result<LoginAppUser> info();
}
