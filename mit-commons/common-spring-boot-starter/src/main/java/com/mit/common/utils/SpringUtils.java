package com.mit.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * spring获取bean工具类
 * 
* @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2018年3月20日 下午10:13:18 类说明
 *
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
		}
	}

	//获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(Class<T> cla) {
		return getApplicationContext().getBean(cla);
	}

	public static <T> T getBean(String name, Class<T> cal) {
		return getApplicationContext().getBean(name, cal);
	}

	public static String getProperty(String key) {
		return getApplicationContext().getBean(Environment.class).getProperty(key);
	}
}
