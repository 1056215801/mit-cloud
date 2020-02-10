package com.mit.community.module.hik.face.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.community.entity.CameraInfo;
import com.mit.community.entity.ClusterCommunity;
import com.mit.community.entity.hik.*;
import com.mit.community.entity.hik.Vo.*;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.feign.FileUploadFeign;
import com.mit.community.service.com.mit.community.service.hik.DeviceInfoService;
import com.mit.community.service.com.mit.community.service.hik.DataDictionaryService;
import com.mit.community.service.com.mit.community.service.hik.SnapFaceDataHikService;
import com.mit.community.service.com.mit.community.service.hik.SnapVehicleService;
import com.mit.community.util.ImgCompass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 11:54 2019/11/15
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "人脸感知平台")
@RequestMapping(value = "/platform",method = RequestMethod.POST)
public class PlatformController {

    @Autowired
    public SnapFaceDataHikService snapFaceDataHikService;
    @Autowired
    public DeviceInfoService deviceInfoService;
    @Autowired
    public CommunityFeign communityFeign;
    @Autowired
    public SnapVehicleService snapVehicleService;
    @Autowired
    public DataDictionaryService dataDictionaryService;
    @Autowired
    public FileUploadFeign fileUploadFeign;
    @Autowired
    private HKFaceController hkFaceController;
    private static PlatformController platformController;
    @PostConstruct
    public void init(){
        platformController=this;
        platformController.fileUploadFeign=this.fileUploadFeign;
    }
    public String getUploadImageUrl(String base64Url){
        Result result = fileUploadFeign.base64(base64Url);
        String datas = (String)result.getDatas();
        return datas;
    }
    @PostMapping("/numberType")
    @ApiOperation("抓拍人数类型感知")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "communityCodes",value = "小区编号",paramType = "query"),
    @ApiImplicitParam(name = "dateType",value = "小区编号:1为当天数据,7为近七日数据",paramType = "query")
    })
    public Result numberType(String communityCodes,@RequestParam("dateType")Integer dateType){
        String[] split = communityCodes.split(",");
        List<String> asList = Arrays.asList(split);
        List<NumberType> list=null;
        if (dateType==1){
            list=snapFaceDataHikService.getNumberType(asList);
        }else if (dateType==7){
            list=snapFaceDataHikService.getWeekNumber(asList);
        }

        return Result.succeed(list);
    }

    @PostMapping("/KeyBayonetPerception")
    @ApiOperation("重要卡口感知数据")
    @ApiImplicitParam(name = "communityCodes",value = "小区编号",paramType = "query")
    public Result KeyBayonetPerception(String communityCodes){
        String communityCodeList=communityCodes;
        List<Gate> gateList = communityFeign.getGateList(communityCodeList);
        List<PerceptionVo> list=new ArrayList<>();
        for (Gate gate : gateList) {
            String camera = gate.getCamera();
            List<String> asList = Arrays.asList(camera.split(","));
            PerceptionVo perceptionVo=snapFaceDataHikService.getSnapData(asList);
            perceptionVo.setInstallationLocation(gate.getLocation());
            list.add(perceptionVo);
        }
     return Result.succeed(list);
    }
    @PostMapping("/RealTimePerception")
    @ApiImplicitParam(name = "communityCodes",value = "小区编号",paramType = "query")
    @ApiOperation("实时进出感知")
    public Result RealTimePerception(String communityCodes){
            String[] split = communityCodes.split(",");
            List<String> asList = Arrays.asList(split);
            List<RealTimeVo> list=snapFaceDataHikService.getRealTime(asList);

        return Result.succeed(list);
    }
    @PostMapping("/cameraInformation")
    @ApiOperation("获得摄像机信息")
    public Result cameraInformation(String serialNumber){
         List<CameraInfo> list=snapFaceDataHikService.getCameraInfo(serialNumber);
        return Result.succeed(list);
    }

    @PostMapping("/informationOverview")
    @ApiOperation("感知信息总览")
    @ApiImplicitParam(name = "communityCodes",value = "小区编号",paramType = "query")
    public Result informationOverview(String communityCodes){
        String[] split = communityCodes.split(",");
        List<String> asList = Arrays.asList(split);
        OverviewVo overviewVo=snapFaceDataHikService.getInformationOverview(asList);
        overviewVo.setResidentNumber(overviewVo.getEntryNumber()-overviewVo.getLeaveNumber());
        return Result.succeed(overviewVo);
    }
    @PostMapping("/getSnapImage")
    @ApiOperation("获取抓拍图片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "开始时间",dataType = "Date",paramType = "query"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",dataType = "Date",paramType = "query"),
            @ApiImplicitParam(name = "communityCodes",value = "小区编码",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "snapshotSite",value = "抓拍地点",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",value = "当前页",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的记录数",dataType = "Integer",paramType = "query")
    })
    public Result getSnapImage(String startTime,String endTime,String communityCodes,
                               String snapshotSite,Integer pageNum,Integer pageSize,
                               String jacketColor,String pantsColor,
                               Integer jacket,Integer pants,Integer bag,
                               Integer things,Integer hat,Integer mask,Integer hairstyle,
                               Integer sex,Integer glass,Integer ride){
        if (StringUtils.isEmpty(communityCodes)) {
            return Result.succeed("查询成功");
        }
        IPage<SnapFaceDataHik> snapFaceDataHikIPage = null;
        IPage<SnapImageVo> snapImageList=null;
        String[] split = communityCodes.split(",");
        List<String> communityCodeList = Arrays.asList(split);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start=null;
            Date end=null;
            if (StringUtils.isNotEmpty(startTime)){
                start = sdf.parse(startTime);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                end = sdf.parse(endTime);
            }
            Page<SnapFaceDataHik> page=new Page<>(pageNum,pageSize);
            QueryWrapper<SnapFaceDataHik> wrapper=new QueryWrapper<>();
            if (StringUtils.isNotEmpty(snapshotSite)){
                wrapper.like("snapshot_site",snapshotSite);
            }
            wrapper.in("communityCode",communityCodeList);
            if (start!=null){
                wrapper.ge("shoot_time",start);
            }
            if (end!=null) {
                wrapper.le("shoot_time",end);
            }
            wrapper.orderByDesc("shoot_time");
            snapImageList = snapFaceDataHikService.getSnapImageList(startTime, endTime,
                    communityCodes, snapshotSite, pageNum, pageSize,jacketColor,
                    pantsColor,jacket,pants,bag,things,hat,mask,hairstyle,sex,glass,ride);
            /*snapFaceDataHikIPage = snapFaceDataHikService.page(page, wrapper);
            List<SnapFaceDataHik> snapFaceDataHikList = snapFaceDataHikIPage.getRecords();
            List<DeviceInfo> deviceInfoList = deviceInfoService.list();
            for (SnapFaceDataHik snapFaceDataHik : snapFaceDataHikList) {
                snapFaceDataHik.setPlace(snapFaceDataHik.getCommunityName()+"-"+snapFaceDataHik.getSnapshotSite());
                for (DeviceInfo deviceInfo : deviceInfoList) {
                    if (snapFaceDataHik.getSerialNumber().equalsIgnoreCase(deviceInfo.getSerialNumber())){
                        snapFaceDataHik.setDeviceName(deviceInfo.getDeviceName());
                    }
                }

            }
            snapFaceDataHikIPage.setRecords(snapFaceDataHikList);*/
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        return Result.succeed(snapImageList);
    }
    @PostMapping("/getImageUrl")
    @ApiOperation("获取图片地址")
    public Result getImageUrl(String base64Url,String serialNumber,String toBase64Byte){
        Result result = fileUploadFeign.base64(base64Url);
        String imageUrl = (String)result.getDatas();
        log.debug("获取抓拍imageUrl="+imageUrl);
        log.debug("获取抓拍serialNumber"+serialNumber);
       /* System.out.println("imageUrl="+imageUrl);
        System.out.println("serialNumber"+serialNumber);*/
        QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("serial_number",serialNumber);
        DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
        String installationLocation="";
        String communityCode="";
        String geographicCoordinates="";
        Integer zoneId=0;
        String zoneName="";
        String communityName ="";
        if (deviceInfo!=null) {
            installationLocation = deviceInfo.getInstallationLocation();
            communityCode = deviceInfo.getCommunityCode();
            geographicCoordinates = deviceInfo.getGeographicCoordinates();
            zoneId = deviceInfo.getZoneId();
            zoneName = deviceInfo.getZoneName();
            communityName = deviceInfo.getCommunityName();
        }
        SnapFaceDataHik snapFaceDataHik=new SnapFaceDataHik();
        snapFaceDataHik.setSnapshotSite(installationLocation);
        snapFaceDataHik.setShootTime(new Date());
        snapFaceDataHik.setCommunityCode(communityCode);
        snapFaceDataHik.setImageUrl(imageUrl);
        snapFaceDataHik.setSerialNumber(serialNumber);
        snapFaceDataHik.setCommunityName(deviceInfo.getCommunityName());
        snapFaceDataHik.setJacketColor("unknown");
        snapFaceDataHik.setPantsColor("unknown");
        snapFaceDataHik.setGlass(0);
        snapFaceDataHik.setThings(0);
        snapFaceDataHik.setHat(0);
        snapFaceDataHik.setPants(0);
        snapFaceDataHik.setHairstyle(0);
        snapFaceDataHik.setBag(0);
        snapFaceDataHik.setSex(0);
        snapFaceDataHik.setMask(0);
        snapFaceDataHik.setJacket(0);
        snapFaceDataHikService.save(snapFaceDataHik);
        Integer id = snapFaceDataHik.getId();
        List<String> list=new ArrayList<>();
        list.add(snapFaceDataHik.getCommunityName()+"-"+"白名单");
        list.add(snapFaceDataHik.getCommunityName()+"-"+"重点关注");
        list.add(snapFaceDataHik.getCommunityName()+"-"+"特殊关爱");
        list.add(snapFaceDataHik.getCommunityName()+"-"+"黑名单");
        list.add(snapFaceDataHik.getCommunityName()+"-"+"陌生人");
        list.add("布控-黑名单");
       /* hkFaceController.pictureOneToManySearch(toBase64Byte,imageUrl,list,50,99,
                20,1,communityCode,id,communityName,geographicCoordinates,zoneId,zoneName,installationLocation);*/
        return Result.succeed("获取成功");
    }
    @PostMapping("/getImageInfo")
    @ApiOperation("获取抓拍图片详情")
    public Result getImageInfo(Integer id){
        SnapImageVo snapImageVo=snapFaceDataHikService.getImageInfo(id);
        String communityCode;
        if (snapImageVo!=null) {
          communityCode = snapImageVo.getCommunityCode();
        }
        ClusterCommunity community = communityFeign.getClusterCommunityByCommunityCode(snapImageVo.getCommunityCode());
        if (community!=null) {
            snapImageVo.setDetailedAddress(community.getProvinceName()
                    +community.getCityName()+community.getAreaName()+"-"+community.getStreetName()+"-"+snapImageVo.getPlace());
        }
        return Result.succeed(snapImageVo);
    }
    @PostMapping("/getJsonInfo")
    @ApiOperation("获取抓拍json数据")
    public void getJsonInfo(String json,String base64Url,String serialNumber){
        Result result = fileUploadFeign.base64(base64Url);
        String imageUrl = (String)result.getDatas();
        SnapVehicle snapVehicle=new SnapVehicle();
        SnapFaceDataHik snapFaceDataHik=new SnapFaceDataHik();
        System.out.println("获取抓拍json数据"+json);
        JSONObject jsonObject = JSONObject.fromObject(json);
        if (jsonObject!=null){
            JSONArray captureResult = jsonObject.getJSONArray("CaptureResult");
            String deviceID = (String)jsonObject.get("deviceID");
            snapFaceDataHik.setSerialNumber(serialNumber);
            QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
            wrapper.eq("serial_number",serialNumber);
            DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
            String installationLocation="";
            String communityCode="";
            String geographicCoordinates="";
            Integer zoneId=0;
            String zoneName="";
            String communityName ="";
            if (deviceInfo!=null) {
                installationLocation = deviceInfo.getInstallationLocation();
                communityCode = deviceInfo.getCommunityCode();
                geographicCoordinates = deviceInfo.getGeographicCoordinates();
                zoneId = deviceInfo.getZoneId();
                zoneName = deviceInfo.getZoneName();
                communityName = deviceInfo.getCommunityName();
                snapFaceDataHik.setCommunityCode(communityCode);
                snapFaceDataHik.setCommunityName(communityName);
                snapFaceDataHik.setSnapshotSite(installationLocation);
            }
            JSONObject object = captureResult.getJSONObject(0);
            if (object!=null){
                JSONObject human = (JSONObject)object.get("Human");
                JSONObject vehicle = (JSONObject)object.get("Vehicle");
                if (vehicle!=null) {
                    JSONArray property = vehicle.getJSONArray("Property");
                    JSONObject plateType=null;
                    JSONObject plateColor=null;
                    JSONObject vehicleType=null;
                    JSONObject vehicleColor=null;
                    JSONObject vehicleLogo=null;
                    JSONObject vehicleSublogo=null;
                    JSONObject vehicleModel=null;
                    JSONObject plateNo=null;
                    if (property.size()==8) {
                        plateType = property.getJSONObject(0);
                        plateColor = property.getJSONObject(1);
                        vehicleType = property.getJSONObject(2);
                        vehicleColor = property.getJSONObject(3);
                        vehicleLogo = property.getJSONObject(4);
                        vehicleSublogo = property.getJSONObject(5);
                        vehicleModel = property.getJSONObject(6);
                    }else {
                        plateType = property.getJSONObject(0);
                        plateColor = property.getJSONObject(1);
                        plateNo = property.getJSONObject(2);
                        vehicleType = property.getJSONObject(3);
                        vehicleColor = property.getJSONObject(4);
                        vehicleLogo = property.getJSONObject(5);
                        vehicleSublogo = property.getJSONObject(6);
                        vehicleModel = property.getJSONObject(7);
                    }
                    QueryWrapper<DataDictionary> queryWrapper=new QueryWrapper<>();
                    queryWrapper.eq("value",(String)plateType.get("value"));
                    DataDictionary type = dataDictionaryService.getOne(queryWrapper);
                    if (type!=null) {
                        snapVehicle.setPlateType(type.getValueImplication());
                    }else {
                        snapVehicle.setPlateType("未知");
                    }
                    QueryWrapper<DataDictionary> queryWrapper2=new QueryWrapper<>();
                    queryWrapper2.eq("value",(String)plateColor.get("value"));
                    DataDictionary dataDictionary = dataDictionaryService.getOne(queryWrapper2);
                    if (dataDictionary!=null) {
                        snapVehicle.setPlateColor(dataDictionary.getValueImplication());
                    }else {
                        snapVehicle.setPlateColor("其他颜色");
                    }
                    QueryWrapper<DataDictionary> queryWrapper1=new QueryWrapper<>();
                    queryWrapper1.eq("value",(String)vehicleType.get("value"));
                    DataDictionary one = dataDictionaryService.getOne(queryWrapper1);
                    if (one!=null) {
                        snapVehicle.setVehicleType(one.getValueImplication());
                    }else {
                        snapVehicle.setVehicleType("未知");
                    }
                    QueryWrapper<DataDictionary> queryWrapper3=new QueryWrapper<>();
                    queryWrapper3.eq("value",(String)vehicleColor.get("value"));
                    DataDictionary dictionary = dataDictionaryService.getOne(queryWrapper3);
                    if (dictionary!=null) {
                        snapVehicle.setVehicleColor(dictionary.getValueImplication());
                    }else {
                        snapVehicle.setVehicleColor("未知");
                    }
                    if (plateNo!=null) {
                        snapVehicle.setPlateNo((String)plateNo.get("value"));
                    }else {
                        snapVehicle.setPlateNo("未知");
                    }
                    snapVehicle.setSerialNumber(deviceID);
                    snapVehicle.setShootTime(new Date());
                    if (deviceInfo!=null) {
                        snapVehicle.setCommunityCode(deviceInfo.getCommunityCode());
                        snapVehicle.setSnapshotSite(deviceInfo.getInstallationLocation());
                    }
                    snapVehicle.setImageUrl(imageUrl);
                    snapVehicleService.save(snapVehicle);
                }
                if (human!=null) {
                    JSONArray property = human.getJSONArray("Property");
                    JSONObject jacketColor = property.getJSONObject(0);
                    JSONObject pantsColor = property.getJSONObject(1);
                    JSONObject glass = property.getJSONObject(2);
                    JSONObject gender = property.getJSONObject(3);
                    JSONObject bag = property.getJSONObject(4);
                    JSONObject things = property.getJSONObject(5);
                    JSONObject hat = property.getJSONObject(6);
                    JSONObject jacketType = property.getJSONObject(7);
                    JSONObject trousersType = property.getJSONObject(8);
                    JSONObject mask = property.getJSONObject(9);
                    JSONObject hairStyle = property.getJSONObject(10);
                    JSONObject ride = property.getJSONObject(12);
                    snapFaceDataHik.setJacketColor((String) jacketColor.get("value"));
                    snapFaceDataHik.setPantsColor((String) pantsColor.get("value"));
                    snapFaceDataHik.setImageUrl(imageUrl);
                    snapFaceDataHik.setShootTime(new Date());
                    if (((String)glass.get("value")).equalsIgnoreCase("yes")) {
                        snapFaceDataHik.setGlass(1);
                    }else if (((String)glass.get("value")).equalsIgnoreCase("no")){
                        snapFaceDataHik.setGlass(2);
                    }else {
                        snapFaceDataHik.setGlass(0);
                    }
                    if (((String)gender.get("value")).equalsIgnoreCase("male")) {
                        snapFaceDataHik.setSex(1);
                    }else if (((String)gender.get("value")).equalsIgnoreCase("female")){
                        snapFaceDataHik.setSex(2);
                    }else {
                        snapFaceDataHik.setSex(0);
                    }
                    if (((String)bag.get("value")).equalsIgnoreCase("yes")) {
                        snapFaceDataHik.setBag(1);
                    }else if (((String)bag.get("value")).equalsIgnoreCase("no")){
                        snapFaceDataHik.setBag(2);
                    }else {
                        snapFaceDataHik.setBag(0);
                    }
                    if (((String)things.get("value")).equalsIgnoreCase("yes")) {
                        snapFaceDataHik.setThings(1);
                    }else if (((String)things.get("value")).equalsIgnoreCase("no")){
                        snapFaceDataHik.setThings(2);
                    }else {
                        snapFaceDataHik.setThings(0);
                    }
                    if (((String)hat.get("value")).equalsIgnoreCase("yes")) {
                        snapFaceDataHik.setHat(1);
                    }else if (((String)hat.get("value")).equalsIgnoreCase("no")){
                        snapFaceDataHik.setHat(2);
                    }else {
                        snapFaceDataHik.setHat(0);
                    }
                    if (((String)jacketType.get("value")).equalsIgnoreCase("longSleeve")) {
                        snapFaceDataHik.setJacket(1);
                    }else if (((String)jacketType.get("value")).equalsIgnoreCase("shortSleeve")){
                        snapFaceDataHik.setJacket(2);
                    }else {
                        snapFaceDataHik.setJacket(0);
                    }
                    if (((String)trousersType.get("value")).equalsIgnoreCase("shortTrousers")) {
                        snapFaceDataHik.setPants(1);
                    }else if (((String)trousersType.get("value")).equalsIgnoreCase("longTrousers")){
                        snapFaceDataHik.setPants(2);
                    }else if (((String)trousersType.get("value")).equalsIgnoreCase("skirt")){
                        snapFaceDataHik.setPants(3);
                    }else {
                        snapFaceDataHik.setPants(0);
                    }
                    if (((String)mask.get("value")).equalsIgnoreCase("yes")) {
                        snapFaceDataHik.setMask(1);
                    }else if (((String)mask.get("value")).equalsIgnoreCase("no")){
                        snapFaceDataHik.setMask(2);
                    }else {
                        snapFaceDataHik.setMask(0);
                    }
                    if (((String)hairStyle.get("value")).equalsIgnoreCase("longHair")) {
                        snapFaceDataHik.setHairstyle(1);
                    }else if (((String)hairStyle.get("value")).equalsIgnoreCase("shortHair")){
                        snapFaceDataHik.setHairstyle(2);
                    }else {
                        snapFaceDataHik.setHairstyle(0);
                    }
                    if (((String)ride.get("description")).equalsIgnoreCase("ride")){
                        if (((String)ride.get("value")).equalsIgnoreCase("yes")) {
                            snapFaceDataHik.setRide(1);
                        }else if (((String)ride.get("value")).equalsIgnoreCase("no")){
                            snapFaceDataHik.setRide(2);
                        }else {
                            snapFaceDataHik.setRide(0);
                        }
                    }else {
                        snapFaceDataHik.setRide(0);
                    }
                    snapFaceDataHikService.save(snapFaceDataHik);
                    Integer id = snapFaceDataHik.getId();
                    List<String> list=new ArrayList<>();
                    list.add(snapFaceDataHik.getCommunityName()+"-"+"白名单");
                    list.add(snapFaceDataHik.getCommunityName()+"-"+"重点关注");
                    list.add(snapFaceDataHik.getCommunityName()+"-"+"特殊关爱");
                    list.add(snapFaceDataHik.getCommunityName()+"-"+"黑名单");
                    list.add(snapFaceDataHik.getCommunityName()+"-"+"陌生人");
                    list.add("布控-黑名单");
                /*    hkFaceController.pictureOneToManySearch(base64Url,imageUrl,list,50,99,
                            20,1,communityCode,id,communityName,geographicCoordinates,zoneId,zoneName,installationLocation);*/
                }
            }

        }
    }
}
