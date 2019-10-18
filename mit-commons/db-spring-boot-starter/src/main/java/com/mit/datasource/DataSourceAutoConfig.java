package com.mit.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.mit.datasource.aop.DataSourceAOP;
import com.mit.datasource.constant.DataSourceKey;
import com.mit.datasource.handler.MyMetaObjectHandler;
import com.mit.datasource.util.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 数据源配置
 * 对一个模块有其主库，所有模块共享一个日志库
 * 在设置了spring.datasource.enable.dynamic 等于 true 是开启多数据源
 */
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(name = {"spring.datasource.dynamic.enable"}, matchIfMissing = false, havingValue = "true")
@Import(DataSourceAOP.class)
public class DataSourceAutoConfig {

	private static final String MAPPER_LOCATION = "classpath*:/mapper/*.xml";

	@Resource
	private PaginationInterceptor paginationInterceptor;

	/**
	 * 模块核心库
	 * 所有引入db-spring-boot-starter的模块都需要一个核心库，前缀spring.datasource.druid.core
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.core")
	public DataSource dataSourceCore(){
	    return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 日志存储库
	 * 所有的核心模块共享一个日志存储库，该模块不采用mysql中的innodb引擎，采用归档引擎
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.log")
	public DataSource dataSourceLog(){
	    return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 纳入动态数据源到spring容器
	 */
	@Primary
    @Bean
    public DataSource dataSource() {
        DynamicDataSource dataSource = new DynamicDataSource();
        DataSource coreDataSource = dataSourceCore() ;
        DataSource logDataSource = dataSourceLog();
        dataSource.addDataSource(DataSourceKey.core, coreDataSource);
        dataSource.addDataSource(DataSourceKey.log, logDataSource);
        dataSource.setDefaultTargetDataSource(coreDataSource);
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
    	MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);

		sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));

		MybatisConfiguration configuration = new MybatisConfiguration();
		// configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCacheEnabled(false);
		sqlSessionFactory.setConfiguration(configuration);

		//全局配置
		GlobalConfig globalConfig  = new GlobalConfig();
		//配置填充器
		globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
		sqlSessionFactory.setGlobalConfig(globalConfig);

		sqlSessionFactory.setPlugins(paginationInterceptor);

		return sqlSessionFactory.getObject();
    }

	/**
	 * 将数据源纳入spring事物管理
	 */
    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource")  DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
   
}
