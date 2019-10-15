
#### 核心依赖
依赖 | 版本
---|---
Spring Boot |  2.1.8.RELEASE  
Spring Cloud | Greenwich.SR3   
Spring Security OAuth2 | 2.3.5
Spring Boot Admin | 2.1.5
Mybatis Plus | 3.2.0
druid starter | 1.1.10
hutool | 4.6.10
swagger | 2.9.2

#### 模块说明
```lua
cloud-platform
├── mit-auth-server -- 授权服务中心[8766]
└── mit-business -- 系统业务模块
     └── user-center -- 用户权限管理服务[8770]
└── mit-commons -- 系统公共starter模块
     ├── auth-client-spring-boot-starter -- 认证客户端
     ├── common-spring-boot-starter -- 公共核心
     ├── db-spring-boot-starter -- 关系型数据库配置
     ├── log-spring-boot-starter -- 日志starter
     ├── redis-spring-boot-starter -- redis配置
     └── swagger-spring-boot-starter -- api文档
├── mit-gateway -- Spring Cloud Gateway网关[8765]
├── mit-monitor -- spring admin服务监控[8711]
├── mit-register -- 服务注册与发现[8761]
└── sql -- 数据库文件
```

#### 开发与启动
##### 启动顺序
1) 注册中心 MitRegisterApplication
2) 网关 MitGatewayApplication
3) 授权中心 MitAuthServerApplication
4) 业务模块，如：用户权限管理服务 UserCenterApplication

SpringBootAdmin 监控服务开发可不启动。

本地开发可不启动如注册中心、网关、授权中心等服务，可使用服务器上的服务，
需修改相应服务的注册中心地址 eureka.client.server-url中的eureka地址和端口。


#### *提交代码前请确保编译通过
