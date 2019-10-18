package com.mit.auth.server.service.impl;

import com.mit.common.feign.UserServiceFeign;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.web.CodeEnum;
import com.mit.common.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserServiceFeign userServiceFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<LoginAppUser> result = userServiceFeign.findByUsername(username);
        LoginAppUser loginAppUser = null;
        if (result.getResp_code().intValue() == CodeEnum.SUCCESS.getCode()) {
            loginAppUser = result.getDatas();
        }
        if (loginAppUser == null) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }
        return checkUser(loginAppUser);
    }

    private LoginAppUser checkUser(LoginAppUser loginAppUser) {
        if (loginAppUser != null && !loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }
}
