package com.mit.common.feign;

import com.mit.common.constant.ServiceNameConstant;
import com.mit.common.feign.fallback.UserServiceFallbackFactory;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.model.SysUser;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@FeignClient(value = ServiceNameConstant.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface UserServiceFeign {
    /**
     * feign rpc访问远程/users/{username}接口
     * 查询用户实体对象SysUser
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/users/name/{username}")
    SysUser selectByUsername(@PathVariable("username") String username);

    /**
     * feign rpc访问远程/users-anon/login接口
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/user/users-anon/login", params = "username")
    Result<LoginAppUser> findByUsername(@RequestParam("username") String username);

}
