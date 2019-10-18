package com.mit.common.utils;

import com.mit.common.constant.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

	public static String getToken (){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String header = request.getHeader(SecurityConstants.Authorization) ;
		String token = StringUtils.isBlank(StringUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE+" ")) ? request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) :  StringUtils.substringAfter(header, OAuth2AccessToken.BEARER_TYPE +" ");
		
		token = StringUtils.isBlank(request.getHeader(SecurityConstants.TOKEN_HEADER)) ? token : request.getHeader(SecurityConstants.TOKEN_HEADER) ;
		
		
		return token ;

	}
	
}
