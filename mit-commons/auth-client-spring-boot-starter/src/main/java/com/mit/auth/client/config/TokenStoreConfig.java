package com.mit.auth.client.config;

import com.mit.auth.client.token.RedisTemplateTokenStore;
import com.mit.auth.client.token.ResJwtAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/** 
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2018年4月5日 下午19:52:21
* 类说明 
* redis存储token
*/
@Configuration
public class TokenStoreConfig {

	//@Autowired
	//private TokenStore tokenStore;

	@Bean(name = "tokenStore")
	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
		return new RedisTokenStore(redisConnectionFactory);
	}

	/*@Resource
	private DataSource dataSource ;
	
	@Autowired(required=false)
	private RedisTemplate<String, Object> redisTemplate ;
	
	
	@Bean
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="jdbc" ,matchIfMissing=false)
	public JdbcTokenStore jdbcTokenStore(){
 
//		oauth_access_token oauth_refresh_token 创建两张表
//		return new JdbcTokenStore( dataSource ) ;
		return new JdbcTokenStore( dataSource ) ;

	}
	
	@Bean
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="redis" ,matchIfMissing=true)
	public RedisTemplateTokenStore redisTokenStore(){
//		return new RedisTokenStore( redisTemplate.getConnectionFactory() ) ; //单台redis服务器
		

		RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore()  ;
		redisTemplateStore.setRedisTemplate(redisTemplate);
		return redisTemplateStore ;
		 

	}
	
	//使用jwt替换原有的uuid生成token方式
	@Configuration
	@ConditionalOnProperty(prefix="security.oauth2.token.store",name="type" ,havingValue="jwt" ,matchIfMissing=false)
	public static class JWTTokenConfig {
		@Bean
		public JwtTokenStore jwtTokenStore(){
			return new JwtTokenStore( jwtAccessTokenConverter() ) ;
		}
		
		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			JwtAccessTokenConverter accessTokenConverter = new ResJwtAccessTokenConverter();
			accessTokenConverter.setSigningKey("ocp");
			return accessTokenConverter ;
		}
	}
	*/
}
