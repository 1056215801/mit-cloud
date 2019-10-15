package com.mit.log.aop;

import com.alibaba.fastjson.JSON;
import com.mit.common.constant.TraceConstant;
import com.mit.common.model.LoginAppUser;
import com.mit.log.model.SysLog;
import com.mit.common.utils.SysUserUtil;
import com.mit.log.annotation.LogAnnotation;
import com.mit.log.service.LogService;
import com.mit.log.util.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 日志AOP,标准日志格式logback-spring.xml
 * 如果开启日志记录，需要多数据配置
 */
@Slf4j
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
public class LogAnnotationAOP {

	@Autowired(required = false)
	private LogService logService;

	@Autowired
    BeanFactory beanFactory;

	@Around("@annotation(ds)")
	public Object logSave(ProceedingJoinPoint joinPoint, LogAnnotation ds) throws Throwable {
		// 请求流水号
		String transid = StringUtils.defaultString(TraceUtil.getTrace(), MDC.get(TraceConstant.LOG_TRACE_ID));

		// 记录开始时间
		long start = System.currentTimeMillis();

		SysLog sysLog = new SysLog();
		sysLog.setCreateTime(new Date());

		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
				RequestContextHolder.getRequestAttributes())).getRequest();
		String url = request.getRequestURI();
		String httpMethod = request.getMethod();
		String ip = request.getRemoteAddr();

		sysLog.setUrl(url);
		sysLog.setHttpMethod(httpMethod);
		sysLog.setIp(ip);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			sysLog.setUsername(authentication.getName());
		}
		//LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
		//if (loginAppUser != null) {
		//	sysLog.setUsername(loginAppUser.getUsername());
		//}

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		LogAnnotation logAnnotation = methodSignature.getMethod().getDeclaredAnnotation(LogAnnotation.class);
		sysLog.setModule(logAnnotation.module());
		String params = "";
		if (logAnnotation.recordRequestParam()) {
			params = Arrays.toString(joinPoint.getArgs());
			sysLog.setParams(params);
		}

		log.info("开始请求，transid={},  url={} , httpMethod={}, reqData={} ", transid, url, httpMethod, params);

		Object result = null;
		try {
			// 调用原来的方法
			result = joinPoint.proceed();
			sysLog.setFlag(Boolean.TRUE);
		} catch (Exception e) {
			sysLog.setFlag(Boolean.FALSE);
			sysLog.setRemark(e.getMessage());
			log.error("请求报错，transid={}, url={}, httpMethod={}, reqData={}, error={}", transid, url, httpMethod,
					params, e.getMessage());
			throw e;
		} finally {
			long time = (System.currentTimeMillis() - start);
			sysLog.setTime(time);
			// 获取回执报文及耗时
			log.info("请求完成, transid={}, 耗时={}, resp={}:", transid, time,
					result == null ? null : JSON.toJSONString(result));
			CompletableFuture.runAsync(() -> {
				try {
					log.trace("日志落库开始：{}", sysLog);
					if(logService != null){
						logService.save(sysLog);
					}
					log.trace("开始落库结束：{}", sysLog);
				} catch (Exception e) {
					log.error("落库失败：{}", e.getMessage());
				}
			}, new TraceableExecutorService(beanFactory, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) ,
			        // 'calculateTax' explicitly names the span - this param is optional
			        "logAop"));
		}
		return result;
	}

	
	/**
	 * 生成日志随机数
	 */
	public String getRandom() {
		int i = 0;
		StringBuilder st = new StringBuilder();
		while (i < 5) {
			i++;
			st.append(ThreadLocalRandom.current().nextInt(10));
		}
		return st.toString() + System.currentTimeMillis();
	}

}