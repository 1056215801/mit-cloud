package com.mit.community.module.hik.face.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.utils.SecurityUtils;
import com.mit.common.web.Result;
import com.mit.community.entity.hik.FaceDatabase;
import com.mit.community.entity.hik.FaceGroup;
import com.mit.community.service.ConfigInfoService;
import com.mit.community.service.com.mit.community.service.hik.FaceDatabaseService;
import com.mit.community.util.ArtemisConfig;
import com.mit.community.util.ArtemisHttpUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@Slf4j
@Api(tags = "海康人脸分组")
@RequestMapping(value = "/hkFaceGroup")
public class HKFaceGroupController {
    @Autowired
    private  ConfigInfoService configInfoService;
    @Autowired
    private FaceDatabaseService faceDatabaseService;
    String  ARTEMIS_PATH = "/artemis";
   /* @ModelAttribute
    public void  config(){
        ConfigInfo configInfo = configInfoService.getConfig();
        ArtemisConfig.host = configInfo.getIp() + ":" + configInfo.getPort();
        // 秘钥Appkey
        ArtemisConfig.appKey = configInfo.getAppKey();
        // 秘钥AppSecret
        ArtemisConfig.appSecret = configInfo.getAppSecret();
    }*/
    static {
        ArtemisConfig.host = "192.168.1.230:80";// 代理API网关nginx服务器ip端口
        ArtemisConfig.appKey = "26234840";// 秘钥appkey
        ArtemisConfig.appSecret = "DCQUuXxOKd0pqZ3oa20y";// 秘钥appSecret
    }
    @RequestMapping(value = "/addSingleFaceGroup", method = RequestMethod.POST)
    @ApiOperation(value = "单个添加人脸分组", notes = "")
    @ResponseBody
    public Result addSingleFaceGroup(String name,String description) {
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group/single/addition";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        LoginAppUser user = SecurityUtils.getUser();
        String communityName = user.getCommunityName();
        String communityCode = user.getCommunityCode();
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", name+"-"+"南标小区");
        jsonBody.put("description",description);
        String body = jsonBody.toJSONString();
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json",null);//     post请求application/json类型参数
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Map data=null;
        if ("0".equals(jsonToken.getString("code"))) {
            data= (Map) jsonToken.get("data");
            String indexCode = (String) data.get("indexCode");
            FaceDatabase faceDatabase=new FaceDatabase();
            faceDatabase.setName((String) data.get("name"));
            faceDatabase.setDescription((String) data.get("description"));
            faceDatabase.setIndexCode(indexCode);
            faceDatabase.setCameraCompany(2);
            faceDatabase.setModifiedTime(LocalDateTime.now());
            faceDatabase.setCreateTime(LocalDateTime.now());
            faceDatabase.setCommunityCode(communityCode);
            faceDatabaseService.save(faceDatabase);
        }
        return Result.succeed(data);
    }

    @RequestMapping(value = "/deleteFaceGroup", method = RequestMethod.POST)
    @ApiOperation(value = "按条件删除人脸分组", notes = "")
    @ResponseBody
    public Result deleteFaceGroup(String[] indexCodes) {
//        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group/batch/deletion";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        List<String> arr=new ArrayList<>();
        if(indexCodes!=null && indexCodes.length>0) {
            for (int i=0;i<indexCodes.length;i++){
                arr.add(indexCodes[i]);
            }

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("indexCodes", arr);
            String body = jsonBody.toJSONString();
            String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
            JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
            Boolean data=null;
            if ("0".equals(jsonToken.getString("code"))) {
                data= (Boolean) jsonToken.get("data");
                for (String indexCode : indexCodes) {
                    QueryWrapper<FaceDatabase> wrapper=new QueryWrapper<>();
                    wrapper.eq("indexCode",indexCode);
                    faceDatabaseService.remove(wrapper);
                }
            }
            return Result.succeed(data);

        }else{
            return Result.failed("参数为空！");
        }
    }


    @RequestMapping(value = "/queryFaceGroup", method = RequestMethod.POST)
    @ApiOperation(value = "按条件查询人脸分组", notes = "")
    @ResponseBody
    public Result queryFaceGroup(String[] indexCodes, String name ) {
//        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        List<String> arr = new ArrayList<>();
        if (indexCodes != null && indexCodes.length > 0) {
            for (int i = 0; i < indexCodes.length; i++) {
                arr.add(indexCodes[i]);
            }
        }else{

        }
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("indexCodes", arr);
        jsonBody.put("name", name);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonObject=new JSONObject();
        JSONObject jsonToken= (JSONObject) JSONObject.parse(result);
        //net.sf.json.JSONObject jsonToken = net.sf.json.JSONObject.fromObject(result);
        List<Map> list = new ArrayList<>();
        if ("0".equals(jsonToken.getString("code"))) {
            list = (List) jsonToken.get("data");
        }
        return Result.succeed(list);



    }

    @RequestMapping(value = "/updateSingleFaceGroup", method = RequestMethod.POST)
    @ApiOperation(value = "单个修改人脸分组", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexCode", value = "indexCode", paramType = "query", required = true, dataType = "String")
    })

    public Result updateSingleFaceGroup(@ApiParam FaceGroup  faceGroup) {
//        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group/single/update";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        List<String> arr=new ArrayList<>();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("description", faceGroup.getDescription());
        jsonBody.put("name", faceGroup.getName());
        jsonBody.put("indexCode", faceGroup.getIndexCode());
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Boolean data=null;
        if ("0".equals(jsonToken.getString("code"))) {
            data= (Boolean) jsonToken.get("data");
            FaceDatabase faceDatabase=new FaceDatabase();
            faceDatabase.setDescription(faceGroup.getDescription());
            faceDatabase.setName(faceGroup.getName());
            QueryWrapper<FaceDatabase> wrapper=new QueryWrapper<>();
            wrapper.eq("indexCode",faceGroup.getIndexCode());
            faceDatabaseService.update(faceDatabase,wrapper);
        }
        return Result.succeed(data);


    }
    @PostMapping("/resourceCameras")
    @ApiOperation("获取设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "当前页",required = true,paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = " 每页显示记录数",required = true,paramType = "query")
    })
    public Result ResourceCameras (Integer pageNo,Integer pageSize){
        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/cameras";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        List<String> arr=new ArrayList<>();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if ("0".equals(jsonToken.getString("code"))){
            Map data = (Map)jsonToken.get("data");
            List<Map> list = (List<Map>)data.get("list");
            return Result.succeed(list);
        }else {
            return Result.failed("获取失败");
        }
    }

}




