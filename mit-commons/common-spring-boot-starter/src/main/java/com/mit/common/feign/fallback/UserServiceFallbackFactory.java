package com.mit.common.feign.fallback;

import com.mit.common.feign.UserServiceFeign;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.model.SysUser;
import com.mit.common.web.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * userService降级工场
 */
@Slf4j
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceFeign> {
    @Override
    public UserServiceFeign create(Throwable throwable) {
        return new UserServiceFeign() {
            @Override
            public SysUser selectByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new SysUser();
            }

            @Override
            public Result<LoginAppUser> findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return null;
            }
        };
    }
}
