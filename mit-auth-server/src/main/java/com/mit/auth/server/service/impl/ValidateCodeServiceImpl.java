package com.mit.auth.server.service.impl;

import com.mit.auth.server.service.IValidateCodeService;
import com.mit.common.constant.UaaConstant;
//import com.mit.common.redis.template.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class ValidateCodeServiceImpl implements IValidateCodeService {

    //@Autowired
    //private RedisRepository redisRepository;

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param deviceId 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String deviceId, String imageCode) {
        //redisRepository.setExpire(buildKey(deviceId), imageCode, UaaConstant.DEFAULT_IMAGE_EXPIRE);
    }

    /**
     * 获取验证码
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public String getCode(String deviceId) {
        //return (String)redisRepository.get(buildKey(deviceId));
        return null;
    }

    /**
     * 删除验证码
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public void remove(String deviceId) {
        //redisRepository.del(buildKey(deviceId));
    }

    /**
     * 验证验证码
     */
    @Override
    public void validate(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new AuthenticationException("请在请求参数中携带deviceId参数"){};
        }
        String code = this.getCode(deviceId);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
        } catch (ServletRequestBindingException e) {
            throw new AuthenticationException ("获取验证码的值失败"){};
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new AuthenticationException ("请填写验证码"){};
        }

        if (code == null) {
            throw new AuthenticationException ("验证码不存在或已过期"){};
        }

        if (!StringUtils.equalsIgnoreCase(code, codeInRequest)) {
            throw new AuthenticationException ("验证码不正确"){};
        }

        this.remove(deviceId);
    }

    private String buildKey(String deviceId) {
        return UaaConstant.DEFAULT_CODE_KEY + ":" + deviceId;
    }
}
