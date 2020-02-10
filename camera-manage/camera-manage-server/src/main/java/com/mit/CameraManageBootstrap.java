package com.mit;

import com.alibaba.fastjson.JSONObject;
import com.mit.community.util.ArtemisConfig;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动类
 * EnableFeignClients({"com.mit.auth.client.feign"})：uth-client模块，会定时发送查询哪些服务可以访问这个服务，所以需要把client的feign路径包含进来
 * @author shuyy
 * @date 2018年11月9日
 * @company mitesofor
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableSwagger2Doc
@ServletComponentScan
@MapperScan(basePackages = {"com.mit.community.mapper", "com.mit.community.*.*.mapper"})
@EnableResourceServer
//如果web监听器 ServletContextListener没有和启动类下一个包下@ServletComponentScan(value= "com.smp.listener")这样才能扫到对应包下类，如果是多个包就和@ComponentScan一样处理加{}
@ComponentScan(value = "com.mit")
public class CameraManageBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CameraManageBootstrap.class).web(WebApplicationType.SERVLET).run(args);

        ArtemisConfig.host = "192.168.1.235:4430";
        // 秘钥Appkey
        ArtemisConfig.appKey = "22641784";
        // 秘钥AppSecret
        ArtemisConfig.appSecret = "fAff9NiLJMd4NL2Mt0zj";

        String ARTEMIS_PATH = "/artemis";
        String getRootApi = ARTEMIS_PATH + "/api/eventService/v1/eventSubscriptionByEventTypes";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";
        /**
         * STEP5：组装请求参数
       /*  */
      /*  JSONObject jsonBody = new JSONObject();
        int[] arr = new int[]{131614};
        jsonBody.put("eventTypes", arr);
        jsonBody.put("eventDest", "http://192.168.1.194:8010/cloud-service/hkFace/snapCallBack");
        String body = jsonBody.toJSONString();*/

        JSONObject jsonBody1 = new JSONObject();
        int[] arr1 = new int[]{1644175361};
        jsonBody1.put("eventTypes", arr1);
        jsonBody1.put("eventDest", "http://192.168.1.212:8010/cloud-service/hkFace/keyPersonRecognitionCallBack");
        String body1 = jsonBody1.toJSONString();
        JSONObject jsonObject2=new JSONObject();
        int[] arr2=new int[]{1644171265};
        jsonObject2.put("eventTypes", arr2);
        jsonObject2.put("eventDest", "http://192.168.1.212:8010/cloud-service/hkFace/StrangerRecognitionBack");
        String body2 = jsonObject2.toJSONString();
       /* JSONObject jsonObject3=new JSONObject();
        int[] arr3=new int[]{131659};
        jsonObject2.put("eventTypes", arr3);
        jsonObject2.put("eventDest", "http://192.168.1.194:8010/cloud-service/hkFace/FaceComparisonCallBack");
        String body3 = jsonObject2.toJSONString();*/
        /**
         * STEP6：调用接口
         */

      /*  String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        System.out.println("请求路径："+ getRootApi + ",请求参数："+ body + ",返回结果：" + result);*/
      /*  String result1 = ArtemisHttpUtil.doPostStringArtemis(path, body1, null, null, contentType);
        System.out.println("请求路径1："+ getRootApi + ",请求参数1："+ body1 + ",返回结果1：" + result1);
        String result2 = ArtemisHttpUtil.doPostStringArtemis(path, body2, null, null, contentType);
        System.out.println("请求路径2："+ getRootApi + ",请求参数2："+ body2 + ",返回结果2：" + result2);*/
       /* String result3 = ArtemisHttpUtil.doPostStringArtemis(path, body3, null, null, contentType);
        System.out.println("请求路径3："+ getRootApi + ",请求参数3："+ body3 + ",返回结果3：" + result3);*/

    }
}
