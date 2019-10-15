package com.mit.log.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * log-spring-boot-starter 自动装配 
 */
public class LogImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] { 
				"com.mit.log.aop.LogAnnotationAOP",
				"com.mit.log.service.impl.LogServiceImpl",
				"com.mit.log.config.LogAutoConfig"
		};
	}

}