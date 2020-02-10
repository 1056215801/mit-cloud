package com.mit.community.module.hik.face.controller;


import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.web.Result;
import com.mit.community.entity.User;
import com.mit.community.entity.hik.*;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.feign.EventFeign;
import com.mit.community.feign.FileUploadFeign;
import com.mit.community.feign.SysDeptFeign;
import com.mit.community.service.ConfigInfoService;
import com.mit.community.service.com.mit.community.service.hik.*;
import com.mit.community.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import sun.misc.BASE64Encoder;
import util.Base64Util;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
@Api(tags = "海康人脸")
@RequestMapping(value = "/hkFace", method = RequestMethod.POST)
public class HKFaceController {
    @Autowired
    private SnapFaceDataHikService snapFaceDataHikService;
    @Autowired
    private ConfigInfoService configInfoService;
    @Autowired
    private FaceDataHikService faceDataHikService;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private StrangerService strangerService;
    @Autowired
    private SysDeptFeign sysDeptFeign;
    @Autowired
    private CommunityFeign communityFeign;
    @Autowired
    private FileUploadFeign fileUploadFeign;
    @Autowired
    private EventFeign eventFeign;
    @Autowired
    private PersonFaceImagesService personFaceImagesService;
    private HKFaceController hkFaceController;
    @PostConstruct
    public void init() {
        hkFaceController = this;
        hkFaceController.personFaceImagesService = this.personFaceImagesService;

    }
    @Autowired
    private HistoricalWarnService historicalWarnService;
    @Value("${hik-manager.ip}")
    private String ip;
    @Value("${hik-manager.port}")
    private String port;
    @Value("${hik-manager.appkey}")
    private String appkey;
    @Value("${hik-manager.appsecret}")
    private String appsecret;
    private String faceUrlGroup;
    private Integer snapFaceDataHikId;

    String ARTEMIS_PATH = "/artemis";


    public void config() {
        ArtemisConfig.host = ip + ":" + port;
        // 秘钥Appkey
        ArtemisConfig.appKey = appkey;
        // 秘钥AppSecret
        ArtemisConfig.appSecret = appsecret;
    }

    @PostMapping("/getFaceList")
    @ApiOperation("获取人脸列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communitycodes",value = "小区code的集合",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "姓名",paramType = "query"),
            @ApiImplicitParam(name = "idNo",value = "身份证号",paramType = "query"),
            @ApiImplicitParam(name = "sex",value = "性别",paramType = "query"),
            @ApiImplicitParam(name = "date",value = "入库时间",paramType = "query"),
            @ApiImplicitParam(name = "faceGroupName",value = "人脸库名",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",value = "当前页",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页的数量",paramType = "query")
    })
    public Result getFaceList(String communitycodes, String name, String idNo,
                              String sex, String date, String faceGroupName, Integer pageNum, Integer pageSize) {
        if (StringUtils.isEmpty(communitycodes)) {
            return Result.failed("查询失败");
        }
        IPage<PersonFaceImages> iPage = null;
        try {
            String[] split = communitycodes.split(",");
            List<String> list = Arrays.asList(split);
            IPage<PersonFaceImages> page = new Page<>(pageNum, pageSize);
            QueryWrapper<PersonFaceImages> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(name)) {
                wrapper.eq("name", name);
            }
            if (StringUtils.isNotEmpty(idNo)) {
                wrapper.eq("idNo", idNo);
            }
            if (sex != null) {
                wrapper.eq("sex", sex);
            }
            if (StringUtils.isNotEmpty(date)) {
                SimpleDateFormat format = new SimpleDateFormat("yy-mm-hh");
                Date parse = format.parse(date);
                wrapper.eq("create_time", parse);
            }
            if (StringUtils.isNotEmpty(faceGroupName)) {
                wrapper.eq("faceGroupName", faceGroupName);
            }
            wrapper.orderByDesc("create_time");
            iPage = personFaceImagesService.page(page, wrapper);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        return Result.succeed(iPage);
    }
    @RequestMapping("/pictureOneToManySearch")
    @ApiOperation(value = "人脸分组1VN检索", notes = "minSimilarity required 指定检索的最小相似度 ，searchNum 指定所有识别资源搜索张数的总和的最大值 ")
    public Result pictureOneToManySearch(String toBase64Byte,String imageUrl,List<String> faceGroupNames,
                                         Integer minSimilarity, Integer searchNum,
                                         Integer pageSize, Integer pageNo,
                                         String communityCode,Integer id,String communityName,String geographicCoordinates,Integer zoneId,String zoneName,String installationLocation) {
        config();
        if (minSimilarity == null) {
            minSimilarity = 75;
        }
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/application/oneToMany";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("searchNum", searchNum);
        jsonBody.put("minSimilarity", minSimilarity);
        // jsonBody.put("facePicUrl", faceInfo.getFaceUrl());
      /*  String faceUrl = faceInfo.getFaceUrl();
        if (faceUrl == null || "".equals(faceUrl)) {
            return Result.failed("faceUrl为空!");
        }*/
        List<String> faceGroupIndexCodes = new ArrayList<String>();
        for (String faceGroupName : faceGroupNames) {
            String faceGroupIndexCode = queryFaceGroup(null, faceGroupName);
            faceGroupIndexCodes.add(faceGroupIndexCode);
        }

     /*   byte[] bytes;
        String faceUrl1 = null;
        try {
            bytes = ImgCompass.convertImageToByteArray(faceUrl, 200);
            faceUrl1 = Base64Util.encode(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        jsonBody.put("facePicBinaryData", toBase64Byte);
       /* jsonBody.put("name", faceInfo.getName());
        jsonBody.put("sex", faceInfo.getSex());
        jsonBody.put("certificateType", faceInfo.getCertificateType());
        jsonBody.put("certificateNum", faceInfo.getCertificateNum());*/
        jsonBody.put("faceGroupIndexCodes", faceGroupIndexCodes);
        String body = jsonBody.toJSONString();
        String result = null;
            result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        JSONObject jsonObject=(JSONObject)jsonToken.get("data");
        Integer similarity=0;
        String certificateNum="";
        if (jsonObject!=null){
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            JSONObject object=null;
            if (jsonArray.size()!=0){
                for (int i = 0; i < jsonArray.size(); i++) {
                    object = (JSONObject) jsonArray.get(i);
                    similarity=(Integer)object.get("similarity");
                    JSONObject faceInfo = (JSONObject) object.get("faceInfo");
                    certificateNum = (String) faceInfo.get("certificateNum");
                    if (StringUtils.isNotBlank(certificateNum)){
                        break;
                    }
                }
            }
        }
        List<PersonFaceImages> faceImagesList=new ArrayList<>();
        PersonFaceImages personFaceImages=null;
        QueryWrapper<PersonFaceImages> wrapper=new QueryWrapper<>();
        if (StringUtils.isNotEmpty(certificateNum)) {
            wrapper.eq("idNo",certificateNum);
            personFaceImages = personFaceImagesService.getOne(wrapper);
        }
        QueryWrapper<SnapFaceDataHik> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        SnapFaceDataHik snapFaceDataHik = snapFaceDataHikService.getOne(queryWrapper);
        String faceDatabaseName="";
        String faceClassification="";
        String uncode="";
        String code="";
        String idNo="";
        EventInfoDTO eventInfoDTO=new EventInfoDTO();
        eventInfoDTO.setCommunityCode(communityCode);
        eventInfoDTO.setEmergencyLevel(1);
        if (personFaceImages==null) {
            Result res = addFaceToHK(faceGroupNames.get(4), "", "", "", faceGroupNames.get(4), "", "", 1, toBase64Byte, imageUrl, communityCode);
            PersonFaceImages faceImages = (PersonFaceImages)res.getDatas();
            if (faceImages!=null) {
                faceDatabaseName = faceImages.getFaceDatabaseName();
                faceClassification = faceImages.getFaceClassification();
                uncode = faceImages.getUncode();
            }
        }else{
            faceDatabaseName = personFaceImages.getFaceDatabaseName();
            faceClassification = personFaceImages.getFaceClassification();
            uncode = personFaceImages.getUncode();
            idNo = personFaceImages.getIdNo();
            code = personFaceImages.getCommunityCode();
            snapFaceDataHik.setIdNo(idNo);
        }
        String[] split = faceDatabaseName.split("-");
        if ("重点关注".equalsIgnoreCase(split[1])){
            eventInfoDTO.setEventCode("FOCUS_PEOPLE");
            eventInfoDTO.setHappenedTime(new Date());
            eventInfoDTO.setEmergencyLevel(1);
            eventInfoDTO.setCommunityName(communityName);
            Result res = communityFeign.getHouseHoldInfoByCardNum(idNo);
            Object datas = res.getDatas();
            String json = JSON.toJSONString(datas);
            eventInfoDTO.setExtension(json);
            eventInfoDTO.setZoneId(String.valueOf(zoneId));
            eventInfoDTO.setLocation(installationLocation);
            eventInfoDTO.setZoneName(zoneName);
            if (StringUtils.isNotBlank(geographicCoordinates)) {
                String[] arr = geographicCoordinates.split(",");
                eventInfoDTO.setLongitude(arr[0]);
                eventInfoDTO.setLatitude(arr[1]);
            }
            eventFeign.handing(eventInfoDTO);
        }
        snapFaceDataHik.setFaceDatabaseName(faceDatabaseName);
        snapFaceDataHik.setFaceClassification(faceClassification);
        snapFaceDataHik.setUncode(uncode);
        snapFaceDataHik.setSimilarity(similarity);
        snapFaceDataHikService.updateById(snapFaceDataHik);
        List<Map> list = new ArrayList<>();
        if ("0".equals(jsonToken.getString("code"))) {
            Map data = (Map) jsonToken.get("data");
            list = (List<Map>) data.get("list");
            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed(list);
    }
    @PostMapping("/addFaceInfoToHK")
    @ApiOperation(value = "把人脸信息加入到HK人脸库并插入自己的数据库", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "faceGroupName", value = "人脸库名", paramType = "query"),
            @ApiImplicitParam(name = "sex", value = "性别", paramType = "query"),
            @ApiImplicitParam(name = "iDType", value = "身份证类型", paramType = "query"),
            @ApiImplicitParam(name = "idNo", value = "身份证编号", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "人脸名称", paramType = "query"),
            @ApiImplicitParam(name = "faceClassification", value = "人脸分类", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "电话号码", paramType = "query"),
            @ApiImplicitParam(name = "numberType", value = "号码类型", paramType = "query"),
            @ApiImplicitParam(name = "imageUrl", value = "图片路径", paramType = "query"),
            @ApiImplicitParam(name = "communityCode", value = "小区code", paramType = "query"),
    })
    public Result addFaceToHK(String faceGroupName, String sex, String iDType, String idNo, String name, String faceClassification, String phone, Integer numberType,@RequestBody String base64,String imageUrl,String communityCode) {
        Result result1 = fileUploadFeign.base64(base64);
        String img=(String)result1.getDatas();
        QueryWrapper<PersonFaceImages> wrapper=new QueryWrapper<>();
        PersonFaceImages person=null;
        if (StringUtils.isNotEmpty(idNo)) {
            wrapper.eq("idNo",idNo);
            person = personFaceImagesService.getOne(wrapper);
        }
        if (person!=null){
            personFaceImagesService.removeById(person.getId());
            String[] ar={person.getUncode()};
            deleteFace(ar,person.getFaceGroupIndexCode());
        }
        String faceGroupIndexCode = null;
        try {
            faceGroupIndexCode = queryFaceGroup(null, faceGroupName);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succeed();
        } finally {
        }
        FaceInfo faceInfo = new FaceInfo();
        String getRootApi1 = ARTEMIS_PATH + "/api/frs/v1/face/single/addition";
        Map<String, String> path1 = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi1);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        String saveSex = sex;
        String saveCertificateType = iDType;
        Map<String, String> faceInfoParam = new HashMap<String, String>(4);
        faceInfoParam.put("name", name);
        if ("0".equals(sex)) {
            sex = "UNKNOWN";
        }
        if ("1".equals(iDType)) {
            iDType = "111";
        } else {
            iDType = "OTHER";
        }
        faceInfoParam.put("sex", sex);
        faceInfoParam.put("certificateType", iDType);
        faceInfoParam.put("certificateNum", idNo);
        Map<String, String> facePicParam = new HashMap<String, String>(1);
        if (StringUtils.isEmpty(base64)) {
           /* File excelFile = null;
            try {
                String originalFilename = file.getOriginalFilename();
                String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
                excelFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15), prefix);
                file.transferTo(excelFile);
                String faceBinaryData = ImgCompass.convertFlieImageToBase64Byte(excelFile, 200);
                facePicParam.put("faceBinaryData", faceBinaryData);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.failed("图片转换失败！");
            }*/
        }else {
            facePicParam.put("faceBinaryData", base64);
        }
        jsonBody.put("faceGroupIndexCode", faceGroupIndexCode);
        jsonBody.put("faceInfo", faceInfoParam);
        jsonBody.put("facePic", facePicParam);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path1, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Map data1 = null;
        if (jsonToken!=null) {
            if ("0".equals(jsonToken.getString("code"))) {
                data1 = (Map) jsonToken.get("data");
            } else {
                return Result.failed(null);
            }
        }
        //3.插入本地的人脸库
        PersonFaceImages personFaceImages = new PersonFaceImages();
        personFaceImages.setCameraCompany(2);
        personFaceImages.setUncode((String) data1.get("indexCode"));
        personFaceImages.setCommunityCode(communityCode);
        personFaceImages.setImgUrl(imageUrl);
        personFaceImages.setFaceClassification(faceClassification);
        personFaceImages.setName(name);
        personFaceImages.setSex(saveSex);
        personFaceImages.setPhone(phone);
        personFaceImages.setNumberType(numberType);
        personFaceImages.setCreateTime(new Date());
        personFaceImages.setModifiedTime(new Date());
        personFaceImages.setIdNo(idNo);
        personFaceImages.setFaceDatabaseName(faceGroupName);
        personFaceImages.setIDType(saveCertificateType);
        personFaceImages.setFaceGroupIndexCode((String) data1.get("faceGroupIndexCode"));
        try {
            personFaceImagesService.save(personFaceImages);
        } catch (Exception e) {
            //要回滚海康添加人脸的信息
            String[] str = new String[5];
            str[0] = (String) data1.get("indexCode");
            deleteFace(str, faceGroupIndexCode);
            return Result.failed("添加失败");
        }
        return Result.succeed(personFaceImages);
    }
    @PostMapping("/blacklistControl")
    @ApiOperation(value = "添加黑名单人脸布控", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sex", value = "性别", paramType = "query"),
            @ApiImplicitParam(name = "iDType", value = "身份证类型", paramType = "query"),
            @ApiImplicitParam(name = "idNo", value = "身份证编号", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "人脸名称", paramType = "query"),
            @ApiImplicitParam(name = "faceClassification", value = "人脸分类", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "电话号码", paramType = "query"),
            @ApiImplicitParam(name = "numberType", value = "号码类型", paramType = "query"),
            @ApiImplicitParam(name = "controlInstructions", value = "布控说明", paramType = "query")
    })
    public Result blacklistControl( String sex, String iDType, String idNo, String name, String faceClassification, String phone, Integer numberType,
                                    String imageUrl,String controlInstructions) {
        /*File toFile=null;
        try {
            toFile = multipartFileToFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }*/

        String faceGroupIndexCode = queryFaceGroup(null, "布控-黑名单");
        FaceInfo faceInfo = new FaceInfo();
        String getRootApi1 = ARTEMIS_PATH + "/api/frs/v1/face/single/addition";
        Map<String, String> path1 = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi1);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        String saveSex = sex;
        String saveCertificateType = iDType;
        Map<String, String> faceInfoParam = new HashMap<String, String>(4);
        faceInfoParam.put("name", name);
        if ("0".equals(sex)) {
            sex = "UNKNOWN";
        }
        if ("1".equals(iDType)) {
            iDType = "111";
        } else {
            iDType = "OTHER";
        }
        faceInfoParam.put("sex", sex);
        faceInfoParam.put("certificateType", iDType);
        faceInfoParam.put("certificateNum", idNo);
        Map<String, String> facePicParam = new HashMap<String, String>(1);
        try {
//            String faceBinaryData = ImgCompass.convertFlieImageToBase64Byte(toFile, 200);
            String faceBinaryData =ImgCompass.imageUrlTobase64Byte(imageUrl,200);
            facePicParam.put("faceBinaryData", faceBinaryData);
           /* Result upload = fileUploadFeign.base64(faceBinaryData);
            imageUrl=(String)upload.getDatas();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        jsonBody.put("faceGroupIndexCode", faceGroupIndexCode);
        jsonBody.put("faceInfo", faceInfoParam);
        jsonBody.put("facePic", facePicParam);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path1, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Map data1 = null;
        if ("0".equals(jsonToken.getString("code"))) {
            data1 = (Map) jsonToken.get("data");
        } else {
            return null;
        }
        //3.插入本地的人脸库
        PersonFaceImages personFaceImages = new PersonFaceImages();
        personFaceImages.setCameraCompany(2);
        personFaceImages.setUncode((String) data1.get("indexCode"));
        personFaceImages.setImgUrl(imageUrl);
        personFaceImages.setFaceClassification(faceClassification);
        personFaceImages.setName(name);
        personFaceImages.setSex(saveSex);
        personFaceImages.setPhone(phone);
        personFaceImages.setNumberType(numberType);
        personFaceImages.setCreateTime(new Date());
        personFaceImages.setModifiedTime(new Date());
        personFaceImages.setIdNo(idNo);
        personFaceImages.setFaceDatabaseName("布控-黑名单");
        personFaceImages.setIDType(saveCertificateType);
        personFaceImages.setControlInstructions(controlInstructions);
        personFaceImages.setFaceGroupIndexCode((String) data1.get("faceGroupIndexCode"));
        try {
            personFaceImagesService.save(personFaceImages);
        } catch (Exception e) {
            //要回滚海康添加人脸的信息
            String[] str = {};
            str[0] = personFaceImages.getUncode();
            deleteFace(str, faceGroupIndexCode);
        }
        return Result.succeed("新增布控成功");
    }
    @RequestMapping(value = "/addSingleFace", method = RequestMethod.POST)
    @ApiOperation(value = "单个添加人脸到海康人脸库", notes = "")
    public Result addSingleFace(FaceInfo faceInfo) {
        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/single/addition";
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
         */
        JSONObject jsonBody = new JSONObject();
        Map<String, String> faceInfoParam = new HashMap<String, String>(4);
        faceInfoParam.put("name", faceInfo.getName());
        if ("0".equals(faceInfo.getSex())) {
            faceInfoParam.put("sex", "UNKNOWN");
        } else {
            //UNKNOWN 	未知    1 	男性     2 	女性
            faceInfoParam.put("sex", faceInfo.getSex());
        }
        faceInfoParam.put("certificateType", faceInfo.getCertificateType());
        faceInfoParam.put("certificateNum", faceInfo.getCertificateNum());
        Map<String, String> facePic = new HashMap<String, String>(1);
        String faceUrl1 = null;
        try {
            faceUrl1 = ImgCompass.convertLocalImageToBase64Byte(faceInfo.getFaceUrl(), 200);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("图片转换失败！");
        }
        facePic.put("faceBinaryData", faceUrl1);
        //System.out.println("打印输出添加人脸到海康人脸库的图片url="+faceInfo.getFaceUrl());
        /*URL url = null;
        try {
            url = new URL(faceInfo.getFaceUrl());
            int size=ImgCompass.showUrlLens(faceInfo.getFaceUrl(),200);
            if (size<=200*1024){
                facePic.put("faceUrl",faceInfo.getFaceUrl());
            }else{
                byte[] bytes= ImgCompass.convertImageToByteArray(faceInfo.getFaceUrl(),200);
                String faceUrl=UploadUtil.uploadWithByte(bytes);
                facePic.put("faceUrl",faceUrl);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String indexCode = queryFaceGroup(null, faceInfo.getFaceGroupName());
        jsonBody.put("faceGroupIndexCode", indexCode);
        jsonBody.put("faceInfo", faceInfoParam);
        jsonBody.put("facePic", facePic);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if ("0".equals(jsonToken.getString("code"))) {
            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed(jsonToken);
    }
    @PostMapping("/updateFace")
    @ApiOperation("修改黑名单布控人脸")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",paramType = "query"),
            @ApiImplicitParam(name = "sex",value = "性别",paramType = "query"),
            @ApiImplicitParam(name = "iDType",value = "身份证类型",paramType = "query"),
            @ApiImplicitParam(name = "idNo",value = "身份证号",paramType = "query"),
            @ApiImplicitParam(name = "name",value = "姓名",paramType = "query"),
            @ApiImplicitParam(name = "faceClassification",value = "人脸分类",paramType = "query"),
            @ApiImplicitParam(name = "phone",value = "电话号码",paramType = "query"),
            @ApiImplicitParam(name = "numberType",value = "号码类型",paramType = "query"),
            @ApiImplicitParam(name = "uncode",value = "人脸唯一标识",paramType = "query"),
            @ApiImplicitParam(name = "faceGroupIndexCode",value = "人脸库唯一标识",paramType = "query"),
            @ApiImplicitParam(name = "controlInstructions",value = "布控说明",paramType = "query")
    })
    public Result updateFace(Long id, String sex, String iDType, String idNo, String name, String faceClassification, String phone,
                             Integer numberType,String uncode,String faceGroupIndexCode,String imgUrl,String controlInstructions) {
//        String groupIndexCode = queryFaceGroup(null, faceGroupName);
        String[] arr = new String[3];
        arr[0] = uncode;
        deleteFace(arr, faceGroupIndexCode);
        FaceInfo faceInfo = new FaceInfo();
        // 2.添加到指定人脸库
        String getRootApi1 = ARTEMIS_PATH + "/api/frs/v1/face/single/addition";
        Map<String, String> path1 = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi1);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        String saveSex = sex;
        String saveIdType=iDType;
        Map<String, String> faceInfoParam = new HashMap<String, String>(4);
        faceInfoParam.put("name", name);
        if ("0".equals(sex)) {
            sex = "UNKNOWN";
        }
        if ("1".equals(iDType)) {
            iDType = "111";
        } else {
            iDType = "OTHER";
        }
        faceInfoParam.put("sex", sex);
        faceInfoParam.put("certificateType", iDType);
        faceInfoParam.put("certificateNum", idNo);
        Map<String, String> facePicParam = new HashMap<String, String>(1);
        File excelFile = null;
        String faceUrl;
        try {
           /* String originalFilename = file.getOriginalFilename();
            String prefix = originalFilename.substring(originalFilename.lastIndexOf("."));
            excelFile = File.createTempFile(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15), prefix);
            file.transferTo(excelFile);*/
            faceUrl =ImgCompass.imageUrlTobase64Byte(imgUrl,200);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("图片转换失败！");
        }
        facePicParam.put("faceBinaryData", faceUrl);
        jsonBody.put("faceGroupIndexCode", faceGroupIndexCode);
        jsonBody.put("faceInfo", faceInfoParam);
        jsonBody.put("facePic", facePicParam);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path1, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Map data1 = null;
        if ("0".equals(jsonToken.getString("code"))) {
            data1 = (Map) jsonToken.get("data");
        } else {
            return Result.failed("单个添加人脸接口出错！");
        }
        QueryWrapper<SnapFaceDataHik> wrapper=new QueryWrapper<>();
        wrapper.eq("uncode",uncode);
        List<SnapFaceDataHik> list = snapFaceDataHikService.list(wrapper);
        for (SnapFaceDataHik snapFaceDataHik : list) {
            snapFaceDataHik.setUncode((String) data1.get("indexCode"));
            snapFaceDataHikService.saveOrUpdate(snapFaceDataHik);
        }
        //3.插入本地的人脸库
        PersonFaceImages personFaceImages = new PersonFaceImages();
        personFaceImages.setId(id);
        personFaceImages.setCameraCompany(2);
        personFaceImages.setUncode((String) data1.get("indexCode"));
        personFaceImages.setCommunityCode("");
        personFaceImages.setImgUrl(imgUrl);
        personFaceImages.setFaceClassification(faceClassification);
        personFaceImages.setName(name);
        personFaceImages.setSex(saveSex);
        personFaceImages.setPhone(phone);
        personFaceImages.setNumberType(numberType);
        personFaceImages.setCreateTime(new Date());
        personFaceImages.setModifiedTime(new Date());
        personFaceImages.setIdNo(idNo);
        personFaceImages.setIDType(saveIdType);
        personFaceImages.setControlInstructions(controlInstructions);
        personFaceImages.setFaceGroupIndexCode(faceGroupIndexCode);
        try {
            personFaceImagesService.saveOrUpdate(personFaceImages);
        } catch (Exception e) {
            //要回滚海康添加人脸的信息
            String[] str = {};
            str[0] = faceInfo.getIndexCode();
            deleteFace(str, faceGroupIndexCode);
        }
        return Result.succeed("");
    }
    @RequestMapping(value = "/snapCallBack", method = RequestMethod.POST)
    @ApiOperation(value = "抓拍的回调方法", notes = "")
    public Result snapCallBack(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        config();
        System.out.println("触发抓拍的回调函数");
        Map params = (Map) map.get("params");
        System.out.println("---------params=" + params);
        List<Map> events = (List<Map>) params.get("events");
        Map data = (Map) events.get(0).get("data");
        List<Map> captureLibResult = (List<Map>) data.get("captureLibResult");
        List<Map> faces = (List<Map>) captureLibResult.get(0).get("faces");
        String url = (String) faces.get(0).get("URL");
        System.out.println("---------------------打印输出抓拍回调函数的url=" + url);
        faceUrlGroup = url;
        Map age = (Map) faces.get(0).get("age");
        String ageStr = age.get("value").toString();
        Map gender = (Map) faces.get(0).get("gender");

        String genderStr = (String) gender.get("value");
        int genderStr2;
        if ("male".equals(genderStr)) {
            genderStr2 = 1;
        } else if ("female".equals(genderStr)) {
            genderStr2 = 2;
        } else {
            genderStr2 = 0;
        }
        Map glass = (Map) faces.get(0).get("glass");
        String glassStr = glass.get("value").toString();
        int glassStr2;
        if ("no".equals(glassStr)) {
            glassStr2 = 1;
        } else {
            glassStr2 = 0; //0为有眼镜
        }
        Map targetAttrs = (Map) captureLibResult.get(0).get("targetAttrs");
        String deviceId = (String) targetAttrs.get("deviceId");
        String sendTime = (String) params.get("sendTime");
        String[] strs = sendTime.split("T");
        String shootTime = strs[0] + " " + strs[1].substring(0, 8);
        SnapFaceDataHik snapFaceDataHik = new SnapFaceDataHik();
        snapFaceDataHik.setAge(Integer.parseInt(ageStr));
        snapFaceDataHik.setGlass(glassStr2);
        snapFaceDataHik.setSex(genderStr2);
        snapFaceDataHik.setImageUrl(url);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date ldt = sdf.parse(shootTime);
            snapFaceDataHik.setShootTime(ldt);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
     /*   boolean b = snapFaceDataHikService.save(snapFaceDataHik);
        snapFaceDataHikId = snapFaceDataHik.getId();*/
        //TODO(插入

        return Result.succeed(snapFaceDataHik);


    }
    @RequestMapping("/keyPersonRecognitionCallBac")
    @ApiOperation(value = "重点人识别的回调函数", notes = "")
    public Result keyPersonRecognitionCallBack(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) throws ParseException {
        config();
        System.out.println("触发了重点人识别的回调函数");
        Map params = (Map) map.get("params");
        System.out.println("----------params=" + params);
        List<Map> events = (List<Map>) params.get("events");
        Map data = (Map) events.get(0).get("data");
        Map faceRecognitionResult = (Map) data.get("faceRecognitionResult");
        List<Map> faceMatch = (List<Map>) faceRecognitionResult.get("faceMatch");
        //相似度
        List<Map> resInfo = (List<Map>) data.get("resInfo");
        String deviceCode = resInfo.get(0).get("indexCode").toString();
        QueryWrapper<DeviceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_number", deviceCode);
        DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
        String communityCode = deviceInfo.getCommunityCode();
        Integer direction = deviceInfo.getDirection();
        String communityName = deviceInfo.getCommunityName();
        boolean flag = true;
        int j = 0;
        boolean sign = false;
        for (int i = 0; i < faceMatch.size(); i++) {
            String faceGroupName1 = (String) faceMatch.get(i).get("faceGroupName");
            String[] split = faceGroupName1.split("-");
            if (communityName.equals(split[1])) {
                flag = false;
                j = i;
            }
            if ("陌生人".equals(split[0])) {
                sign = true;
            }
        }
        String faceGroupName = (String) faceMatch.get(j).get("faceGroupName");    // 人脸分组的名称
        String faceInfoCode = (String) faceMatch.get(j).get("faceInfoCode");  // 人脸唯一标识
        String faceInfoName = (String) faceMatch.get(j).get("faceInfoName");  // 人脸对应的名称
        String faceInfoSex = (String) faceMatch.get(j).get("faceInfoSex");        // 性别
        String facePicUrl = (String) faceMatch.get(j).get("facePicUrl");          //超脑返回的照片地址
        String certificate = (String) faceMatch.get(j).get("certificate");          //超脑返回的证件号码
        if (flag) {
            FaceInfo faceInfo = new FaceInfo();
            faceInfo.setFaceGroupName("陌生人" + "-" + communityName);
            faceInfo.setFaceUrl(facePicUrl);
            if ("male".equals(faceInfoSex)) {
                faceInfo.setSex("1");
            } else if ("female".equals(faceInfoSex)) {
                faceInfo.setSex("2");
            } else {
                faceInfo.setSex("0");
            }
            faceInfo.setCertificateNum(certificate);
            faceInfo.setCertificateType("111");
            Result result = addSingleFace(faceInfo);
            JSONObject jsonToken = (JSONObject) result.getDatas();
            String code = jsonToken.getString("indexCode");
            Stranger stranger = new Stranger();
            stranger.setShootTime(LocalDateTime.now());
            stranger.setSerialNumber(deviceCode);
            stranger.setUncode(code);
            stranger.setCommunityCode(communityCode);
            strangerService.save(stranger);
        }
        if (sign) {
            Stranger stranger = new Stranger();
            stranger.setShootTime(LocalDateTime.now());
            stranger.setSerialNumber(deviceCode);
            stranger.setUncode(faceInfoCode);
            stranger.setCommunityCode(communityCode);
            strangerService.save(stranger);
        }
        Map snap = (Map) faceRecognitionResult.get("snap");
        String faceTime = (String) snap.get("faceTime");
        String faceUrl = (String) snap.get("faceUrl");
        System.out.println("超脑返回的照片地址facePicUrl=" + facePicUrl);
        QueryWrapper<PersonFaceImages> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("uncode", faceInfoCode);
        PersonFaceImages personFaceImages = personFaceImagesService.getOne(entityWrapper);
        String faceClassification = "";
        if (personFaceImages != null) {
            faceClassification = personFaceImages.getFaceClassification();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(faceTime);
        SnapFaceDataHik snapFaceDataHik = new SnapFaceDataHik();
        snapFaceDataHik.setShootTime(new Date());
        snapFaceDataHik.setUncode(faceInfoCode);
        snapFaceDataHik.setImageUrl(faceUrl);
        snapFaceDataHik.setSerialNumber(deviceCode);
        snapFaceDataHik.setCommunityCode(communityCode);
        snapFaceDataHik.setFaceClassification(faceClassification);
        snapFaceDataHik.setFaceDatabaseName(faceGroupName);
        snapFaceDataHik.setDirection(direction);
        if ("male".equals(faceInfoSex)) {
            snapFaceDataHik.setSex(1);
        } else if ("female".equals(faceInfoSex)) {
            snapFaceDataHik.setSex(2);
        } else {
            snapFaceDataHik.setSex(0);
        }
        snapFaceDataHik.setFaceName(faceInfoName);
       /* QueryWrapper<SnapFaceDataHik> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",snapFaceDataHikId);
        snapFaceDataHikService.remove(queryWrapper);*/
        snapFaceDataHikService.save(snapFaceDataHik);
        HistoricalWarn historicalWarn = new HistoricalWarn();
        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/cameras/indexCode";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", deviceCode);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if ("0".equals(jsonToken.getString("code"))) {
            System.out.println("----------" + jsonToken.getString("data"));

        }
        if ("黑名单".equals(faceGroupName)) {
            // 报警
        }
        if ("陌生人".equals(faceGroupName)) {
            //加入到陌生人访问记录里
        }
        return null;
    }

    @RequestMapping("/FaceComparisonCallBack")
    @ApiOperation(value = "人脸比对的回调函数", notes = "")
    public Result FaceComparisonCallBack(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map) {
        Map params = (Map) map.get("params");
        System.out.println("---------params=" + params);
        List<Map> events = (List<Map>) params.get("events");
        Map data = (Map) events.get(0).get("data");
        System.out.println("人脸比对的回调函数" + params);
        return Result.succeed("");
    }

    @ApiOperation(value = "陌生人识别回调函数")
    @PostMapping("/StrangerRecognitionBack")
    public Result StrangerRecognitionBack(@RequestBody Map map) {
        config();
        SnapFaceDataHik snapFaceDataHik = new SnapFaceDataHik();
        PersonFaceImages personFaceImages = new PersonFaceImages();
        System.out.println("触发了陌生人回调函数");
        Stranger stranger = new Stranger();
        Map params = (Map) map.get("params");
        System.out.println("---------params=" + params);
        List<Map> events = (List<Map>) params.get("events");
        Map data = (Map) events.get(0).get("data");
        List<Map> resInfo = (List<Map>) data.get("resInfo");
        Map faceRecognitionResult = (Map) data.get("faceRecognitionResult");
        String cn = (String) resInfo.get(0).get("cn");
        String indexCode = (String) resInfo.get(0).get("indexCode");
        Map snap = (Map) faceRecognitionResult.get("snap");
        String ageGroup = (String) snap.get("ageGroup");
        String faceTime = (String) snap.get("faceTime");
        String faceUrl = (String) snap.get("faceUrl");
        System.out.println("-------------打印输出陌生人识别回调函数图片url=" + faceUrl);
        String gender = (String) snap.get("gender");
        String glass = (String) snap.get("glass");
        FaceInfo faceInfo = new FaceInfo();
        if ("yes".equalsIgnoreCase(glass)) {
            snapFaceDataHik.setGlass(1);
        } else if ("no".equalsIgnoreCase(glass)) {
            snapFaceDataHik.setGlass(2);
        } else {
            snapFaceDataHik.setGlass(0);
        }
        if ("male".equals(gender)) {
            faceInfo.setSex("1");
            snapFaceDataHik.setSex(1);
            personFaceImages.setSex("1");
        } else if ("female".equals(gender)) {
            faceInfo.setSex("2");
            snapFaceDataHik.setSex(2);
            personFaceImages.setSex("2");
        } else {
            faceInfo.setSex("0");
            snapFaceDataHik.setSex(0);
            personFaceImages.setSex("0");
        }
        QueryWrapper<DeviceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("serial_number", indexCode);
        DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
        String communityCode = deviceInfo.getCommunityCode();
        String communityName = deviceInfo.getCommunityName();
        Integer direction = deviceInfo.getDirection();
        snapFaceDataHik.setShootTime(new Date());
        snapFaceDataHik.setFaceDatabaseName("陌生人" + "-" + communityName);
        snapFaceDataHik.setDirection(direction);
        snapFaceDataHik.setImageUrl(faceUrl);
        snapFaceDataHik.setCommunityCode(communityCode);
        snapFaceDataHik.setSerialNumber(indexCode);
        String localUrl = downloadPicture(faceUrl);
        faceInfo.setFaceUrl(localUrl);
        String s = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15);
        faceInfo.setName(s);
        faceInfo.setFaceGroupName("陌生人" + "-" + communityName);
        Result result = addSingleFace(faceInfo);
        JSONObject jsonToken = (JSONObject) result.getDatas();
        Map faceData = (Map) jsonToken.get("data");
        String code = (String) faceData.get("indexCode");
        stranger.setCommunityCode(communityCode);
        stranger.setUncode(code);
        stranger.setShootTime(LocalDateTime.now());
        stranger.setSerialNumber(indexCode);
        strangerService.save(stranger);
        snapFaceDataHikService.save(snapFaceDataHik);
        personFaceImages.setFaceDatabaseName("陌生人" + "-" + communityName);
        personFaceImages.setImgUrl(faceUrl);
        personFaceImages.setUncode(code);
        personFaceImages.setCameraCompany(2);
        personFaceImages.setName(s);
        personFaceImages.setCommunityCode(communityCode);
        personFaceImages.setCreateTime(new Date());
        personFaceImages.setModifiedTime(new Date());
        personFaceImagesService.save(personFaceImages);
        return Result.succeed(faceUrl);
    }
    @PostMapping("/deleteHkFace")
    public Result deleteHkFace(String idNo){
        QueryWrapper<PersonFaceImages> wrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(idNo)) {
            wrapper.eq("idNo",idNo);
        }
        PersonFaceImages personFaceImages = personFaceImagesService.getOne(wrapper);
        String uncode="";
        String faceGroupIndexCode ="";
        if (personFaceImages!=null){
           uncode = personFaceImages.getUncode();
           faceGroupIndexCode = personFaceImages.getFaceGroupIndexCode();
        }
        String[] arr=new String[5];
        arr[0]=uncode;
        boolean remove = personFaceImagesService.remove(wrapper);
        deleteFace(arr, faceGroupIndexCode);
//        if (result){
//            Result res = deleteFace(arr, faceGroupIndexCode);
//            Integer resp_code = res.getResp_code();
//            if (resp_code==1){
//                return Result.succeed("删除成功");
//            }
//        }
        return Result.succeed("删除成功");
    }
    @RequestMapping("/deleteFace")
    @ApiOperation(value = "按条件删除人脸", notes = "")
    public Result deleteFace(String[] indexCodes, String faceGroupIndexCode) {
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
//        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/deletion";
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
         *
         */
        JSONObject jsonBody = new JSONObject();

        List<String> list = new ArrayList<String>();
        if (indexCodes != null && indexCodes.length > 0) {
            for (int i = 0; i < indexCodes.length; i++) {
                list.add(indexCodes[i]);
            }
        } else {

        }
        jsonBody.put("indexCodes", list);
        jsonBody.put("faceGroupIndexCode", faceGroupIndexCode);

        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if (jsonToken!=null){
            if ("0".equals(jsonToken.getString("code"))) {
                return Result.succeed();
//            JSONArray array = JSONArray.parseArray(jsonToken.getString("data"));
            }
        }
        return Result.failed();


    }

    @RequestMapping("/queryFace")
    @ApiOperation(value = "按条件查询人脸", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexCodes", value = "indexCodes 通过人脸的唯一标识集合查询指定的人脸集合", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certificateNum", value = "certificateNum 人脸的证件号码模糊查询", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certificateType", value = "certificateType 人脸的证件类型搜索", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "faceGroupIndexCode", value = "faceGroupIndexCode 根据人脸所属的分组搜索该分组下符合条件的人脸", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certificateType", value = "certificateType 人脸的证件类型搜索", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name 人脸名称模糊查询", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNo", value = "pageNo 分页查询条件，页码，为空时，等价于1，页码不能小于1或大于1000", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize 分页查询条件，页尺，为空时，等价于1000，页尺不能小于1或大于1000", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sex", value = "sex 性别搜索,1代表男性、2代表女性、UNKNOWN代表未知", paramType = "query", required = false, dataType = "String"),

    })
    public Result queryFace(String[] indexCodes, FaceInfo faceInfo, int pageSize, int pageNo) {
        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        int len = 0;
        if (indexCodes != null) {
            len = indexCodes.length;
        }
        String[] arr = new String[len];
        if (indexCodes != null && indexCodes.length > 0) {
            for (int i = 0; i < indexCodes.length; i++) {
                arr[i] = indexCodes[i];
            }
        } else {

        }
        String faceGroupName = faceInfo.getFaceGroupName();
        String faceGroupIndexCode = queryFaceGroup(null, faceGroupName);
        jsonBody.put("certificateNum", faceInfo.getCertificateNum());
        jsonBody.put("certificateType", faceInfo.getCertificateType());
        jsonBody.put("faceGroupIndexCode", faceGroupIndexCode);
        jsonBody.put("indexCodes", arr);
        jsonBody.put("name", faceInfo.getName());
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String sex = faceInfo.getSex();
        if ("3".equals(sex)) {
            sex = "UNKNOWN";
        }
        jsonBody.put("sex", sex);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        //JSONArray jsonArray=null;
        JSONObject data = null;
        List<Map> list = new ArrayList<>();
        if ("0".equals(jsonToken.getString("code"))) {
            data = (JSONObject) jsonToken.get("data");
            list = (List) data.get("list");
            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed(data);
    }

    @RequestMapping("/updateSingleFace")
    @ApiOperation(value = "单个修改人脸", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexCode", value = "indexCode 人脸的唯一标识", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name 人脸的名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certificateType", value = "certificateType 人脸的证件类型搜索", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "faceGroupIndexCode", value = "faceGroupIndexCode 根据人脸所属的分组搜索该分组下符合条件的人脸", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certificateType", value = "certificateType 人脸的证件类型搜索", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "name", value = "name 人脸名称模糊查询", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sex", value = "sex 性别搜索,1代表男性、2代表女性、UNKNOWN代表未知", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "faceUrl", value = "faceUrl 图片地址", paramType = "query", required = false, dataType = "String")

    })
    public Result updateSingleFace(String indexCode, String name, String sex, Integer iDType, String idNo, String imageUrl, String faceGroupName, String faceGroupIndexCode) {
        config();
        String groupIndexCode = queryFaceGroup(null, faceGroupName);
        String[] arr = new String[3];
        arr[0] = indexCode;
        deleteFace(arr, faceGroupIndexCode);
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group/single/update";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        Map<String, String> faceInfo1 = new HashMap<String, String>();
        if (StringUtils.isNotEmpty(name)) {
            faceInfo1.put("name", name);
        }
        if (sex != null) {
            faceInfo1.put("sex", sex.toString());
        }
        if (iDType == 1) {
            faceInfo1.put("certificateType", "111");
        } else {
            faceInfo1.put("certificateType", "OTHER");
        }
        if (StringUtils.isNotEmpty(idNo)) {
            faceInfo1.put("certificateNum", idNo);
        }
        jsonBody.put("indexCode", indexCode);
        jsonBody.put("faceInfo", faceInfo1);
        if (StringUtils.isNotEmpty(imageUrl)) {
            Map<String, String> facePic = new HashMap<String, String>();
            facePic.put("faceUrl", imageUrl);
            jsonBody.put("facePic", facePic);
        }
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if ("0".equals(jsonToken.getString("code"))) {
            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed("修改成功");


    }

    @RequestMapping("/oneToOne")
    @ApiOperation(value = "人脸图片1V1比对", notes = "srcFacePicBinaryData 原始图（Base64编码） srcFacePicUrl 原始图（url） ，distFacePicBinaryData 目标图（Base64编码）， distFacePicUrl 目标图（url） （url，base64）二选一")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "srcFacePicBinaryData", value = "srcFacePicBinaryData 原始图（Base64编码）", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "srcFacePicUrl", value = "srcFacePicUrl 原始图（url）", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "distFacePicBinaryData", value = "distFacePicBinaryData 目标图（Base64编码）", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "distFacePicUrl", value = "distFacePicUrl 目标图（url）", paramType = "query", required = false, dataType = "String")


    })
    public Result oneToOne(String srcFacePicBinaryData, String srcFacePicUrl, String distFacePicBinaryData, String distFacePicUrl) {
        config();
      /*  if(minSimilarity==null ||"".equals(minSimilarity)){
            minSimilarity="75";
        }*/
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/application/oneToOne";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();

        int srclen = 0;
        int distlen = 0;
        byte[] srcbytes;
        byte[] distbytes;
        String base64SrcFace = "";
        String base64distFace = "";
        String faceUrl1 = null;
        try {
            srclen = ImgCompass.showUrlLens(srcFacePicUrl, 200);
            if (srclen <= 4 * 1024 * 1024) {
                srcbytes = ImgCompass.showUrlBtyes(srcFacePicUrl);
            } else {
                srcbytes = ImgCompass.convertImageToByteArray(srcFacePicUrl, 4 * 1024 * 1024);
            }
            distlen = ImgCompass.showUrlLens(distFacePicUrl, 200);
            if (distlen <= 4 * 1024 * 1024) {
                distbytes = ImgCompass.showUrlBtyes(srcFacePicUrl);
            } else {
                distbytes = ImgCompass.convertImageToByteArray(srcFacePicUrl, 4 * 1024 * 1024);
            }
            base64SrcFace = Base64Util.encode(srcbytes);
            base64distFace = Base64Util.encode(distbytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonBody.put("srcFacePicBinaryData", base64SrcFace);
        jsonBody.put("srcFacePicUrl", srcFacePicUrl);

        jsonBody.put("distFacePicBinaryData", base64distFace);
        jsonBody.put("distFacePicUrl", distFacePicUrl);
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        Map data = null;
        if ("0".equals(jsonToken.getString("code"))) {
            data = (Map) jsonToken.get("data");
            //  list= (List<Map>) data.get("list");

            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed(data);
    }

    @RequestMapping("/downloadPicture")
    @ApiOperation(value = "图片下载", notes = "url 图片地址  ")
    public String downloadPicture(String url) {
        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/application/picture";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("url", "http://192.168.1.240/picture/Streaming/tracks/203/?name=ch0002_02000000019004693964800248873&size=248873");
        jsonBody.put("url", url);
        String body = jsonBody.toJSONString();
        HttpResponse result = ArtemisHttpUtil.doPostStringImgArtemis(path, body, null, null, contentType, null);
        System.out.println(result);
        String imageurl = "d:/downloadimage/";
        try {
            HttpResponse resp = result;
            if (200 == resp.getStatusLine().getStatusCode()) {
                HttpEntity entity = resp.getEntity();
                InputStream in = entity.getContent();
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime());
                String filePath = time + ".jpg";
                Tools.savePicToDisk(in, "d:/downloadimage/", filePath);
                imageurl = imageurl + filePath;
                System.out.println("下载成功");
            } else {
                System.out.println("下载出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageurl;
    }

    @ApiOperation(value = "/captureSearch")
    public Result captureSearch(String url) {

        return null;
    }

    @RequestMapping("/imgCompass")
    @ApiOperation(value = "图片压缩", notes = "")
    public Result imgCompass(String faceUrl) {

        byte[] bytes;
        String faceUrl1 = null;

        try {
            bytes = ImgCompass.convertImageToByteArray(faceUrl, 200);

            faceUrl1 = Base64Util.encode(bytes);

            // faceUrl1=UploadUtil.uploadWithByte(bytes);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("图片上传失败！");
        }


        return Result.succeed(faceUrl1, "成功!");
    }


    public String queryFaceGroup(String[] indexCodes, String name) {
        config();
        String getRootApi = ARTEMIS_PATH + "/api/frs/v1/face/group";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        List<String> arr = new ArrayList<>();
        if (indexCodes != null && indexCodes.length > 0) {
            for (int i = 0; i < indexCodes.length; i++) {
                arr.add(indexCodes[i]);
            }
        } else {

        }
        String faceGroupIndexCode = "";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("indexCodes", arr);
        jsonBody.put("name", name);
        String body = jsonBody.toJSONString();
        String result = null;
        result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        if (jsonToken!=null) {
            List<Map> list = new ArrayList<>();
            if ("0".equals(jsonToken.getString("code"))) {
                list = (List) jsonToken.get("data");
                if (list != null && list.size() > 0) {
                    faceGroupIndexCode = list.get(0).get("indexCode").toString();
                }
            }
        }
        return faceGroupIndexCode;

    }


    @RequestMapping("/previewURLs")
    @ApiOperation(value = "获取监控点回放取流URL", notes = "获取监控点回放取流URL")
    public Result previewURLs(String cameraIndexCode, String recordLocation, String protocol, String transmode, String beginTime, String endTime) {
        config();
        String getRootApi = ARTEMIS_PATH + "/api/video/v1/cameras/playbackURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };
        String contentType = "application/json";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        //存储类型：
        //0：中心存储
        //1：设备存储
        //默认为中心存储
        if (recordLocation != null && !"".equals(recordLocation)) {

        }
   /*
   取流协议（应用层协议)：
“rtsp”:RTSP协议
“rtmp”:RTMP协议
“hls”:HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码；
云存储版本要求v2.2.4及以上的2.x版本，或v3.0.5及以上的3.x版本；ISC版本要求v1.2.0版本及以上，
需在运管中心-视频联网共享中切换成启动平台内置VOD）
参数不填，默认为RTSP协议
   * */
        if (protocol != null && !"".equals(protocol)) {
            jsonBody.put("protocol", protocol);
        }

        if (transmode != null && !"".equals(transmode)) {
            jsonBody.put("transmode", transmode);
        }
        jsonBody.put("beginTime", beginTime);
        jsonBody.put("endTime", endTime);
        jsonBody.put("expand", "streamform=rtp");
        String body = jsonBody.toJSONString();
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);

        JSONObject jsonToken = (JSONObject) JSONObject.parse(result);
        List<Map> list = new ArrayList<>();
        if ("0".equals(jsonToken.getString("code"))) {

            Map data = (Map) jsonToken.get("data");
            list = (List<Map>) data.get("list");

            System.out.println("----------" + jsonToken.getString("data"));
        }
        return Result.succeed("成功");
    }
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}




