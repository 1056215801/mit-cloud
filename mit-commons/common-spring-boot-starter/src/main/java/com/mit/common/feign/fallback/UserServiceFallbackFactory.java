package com.mit.common.feign.fallback;

import com.mit.common.feign.UserServiceFeign;
import com.mit.common.model.LoginAppUser;
import com.mit.common.model.SysUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * userService降级工场
 *
 * @author zlt
 * @date 2019/1/18
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
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常:{}", openId, throwable);
                return new LoginAppUser();
            }
        };
    }
}
