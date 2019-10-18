package com.mit.common.utils;

import cn.hutool.core.util.StrUtil;
import com.mit.common.constant.SecurityConstants;
import com.mit.common.dto.LoginAppUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 安全工具类
 */
@UtilityClass
public class SecurityUtils {
    /**
     * 获取Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public LoginAppUser getUser(Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // 刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();
            }
        }
        return null;
    }

    /**
     * 获取用户
     */
    public LoginAppUser getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 获取用户角色信息
     * @return 角色集合
     */
    public List<Long> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Long> roleIds = new ArrayList<>();
        authorities.stream()
                .filter(granted -> StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
                .forEach(granted -> {
                    String id = StrUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
                    roleIds.add(Long.parseLong(id));
                });
        return roleIds;
    }
}
