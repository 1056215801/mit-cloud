package com.mit.community.module.hik.face.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.community.entity.CameraInfo;
import com.mit.community.entity.ClusterCommunity;
import com.mit.community.entity.hik.DeviceInfo;
import com.mit.community.entity.hik.DataDictionary;
import com.mit.community.entity.hik.SnapFaceDataHik;
import com.mit.community.entity.hik.SnapVehicle;
import com.mit.community.entity.hik.Vo.*;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.service.com.mit.community.service.hik.DeviceInfoService;
import com.mit.community.service.com.mit.community.service.hik.DataDictionaryService;
import com.mit.community.service.com.mit.community.service.hik.SnapFaceDataHikService;
import com.mit.community.service.com.mit.community.service.hik.SnapVehicleService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private SnapFaceDataHikService snapFaceDataHikService;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private CommunityFeign communityFeign;
    @Autowired
    private SnapVehicleService snapVehicleService;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @PostMapping("/KeyBayonetPerception")
    @ApiOperation("重要卡口感知数据")
    public Result KeyBayonetPerception(@RequestParam("communityCodes") List<String> communityCodes){

            List<PerceptionVo> list=snapFaceDataHikService.getSnapData(communityCodes);


     return Result.succeed(list);
    }

    @PostMapping("/RealTimePerception")
    @ApiOperation("实时进出感知")
    public Result RealTimePerception(@RequestParam("communityCodes")List<String> communityCodes){

            List<RealTimeVo> list=snapFaceDataHikService.getRealTime(communityCodes);

        return Result.succeed(list);
    }

    @PostMapping("/numberType")
    @ApiOperation("抓拍人数类型感知")
    public Result numberType(@RequestParam("communityCodes")List<String> communityCodes,@RequestParam("dateType")Integer dateType){
        List<NumberType> list=null;
            if (dateType==1){
                list=snapFaceDataHikService.getNumberType(communityCodes);
            }else if (dateType==7){
                list=snapFaceDataHikService.getWeekNumber(communityCodes);
            }

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
    public Result informationOverview(@RequestParam("communityCodes") List<String> communityCodes){
          OverviewVo overviewVo=snapFaceDataHikService.getInformationOverview(communityCodes);
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
                               Integer sex,Integer glass){
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
                    pantsColor,jacket,pants,bag,things,hat,mask,hairstyle,sex,glass);
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
    public Result getImageUrl(String imageUrl,String serialNumber){
        log.debug("获取抓拍imageUrl="+imageUrl);
        log.debug("获取抓拍serialNumber"+serialNumber);
       /* System.out.println("imageUrl="+imageUrl);
        System.out.println("serialNumber"+serialNumber);*/
        QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
        wrapper.eq("serial_number",serialNumber);
        DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
        String installationLocation="";
        String communityCode="";
        if (deviceInfo!=null) {
            installationLocation = deviceInfo.getInstallationLocation();
            communityCode = deviceInfo.getCommunityCode();
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
        return Result.succeed("获取成功");
    }
    @PostMapping("/getImageInfo")
    @ApiOperation("获取抓拍图片详情")
    public Result getImageInfo(Integer id){
        SnapImageVo snapImageVo=snapFaceDataHikService.getImageInfo(id);
        /*System.out.println( snapImageVo.getShootTime());
        Result baseInfo = communityFeign.getBaseInfo(snapImageVo.getCommunityCode());
        Object datas = baseInfo.getDatas();
        JSONObject jsonObject = JSONObject.fromObject(datas);
        ClusterCommunity community = (ClusterCommunity)JSONObject.toBean(jsonObject, ClusterCommunity.class);*/
        ClusterCommunity community = communityFeign.getClusterCommunityByCommunityCode(snapImageVo.getCommunityCode());
        if (community!=null) {
            snapImageVo.setDetailedAddress(community.getProvinceName()
                    +community.getCityName()+community.getAreaName()+"-"+community.getStreetName()+"-"+snapImageVo.getPlace());
        }
        return Result.succeed(snapImageVo);
    }
    @PostMapping("/getJsonInfo")
    @ApiOperation("获取抓拍json数据")
    public void getJsonInfo(String json,String imageUrl){
        SnapVehicle snapVehicle=new SnapVehicle();
        SnapFaceDataHik snapFaceDataHik=new SnapFaceDataHik();
        System.out.println("获取抓拍json数据"+json);
        JSONObject jsonObject = JSONObject.fromObject(json);
        if (jsonObject!=null){
            JSONArray captureResult = jsonObject.getJSONArray("CaptureResult");
            String deviceID = (String)jsonObject.get("deviceID");
            snapFaceDataHik.setSerialNumber(deviceID);
            QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
            wrapper.eq("serial_number",deviceID);
            DeviceInfo deviceInfo = deviceInfoService.getOne(wrapper);
            if (deviceInfo!=null) {
                snapFaceDataHik.setCommunityCode(deviceInfo.getCommunityCode());
                snapFaceDataHik.setCommunityName(deviceInfo.getCommunityName());
                snapFaceDataHik.setSnapshotSite(deviceInfo.getInstallationLocation());
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
                    }
                    QueryWrapper<DataDictionary> queryWrapper2=new QueryWrapper<>();
                    queryWrapper2.eq("value",(String)plateColor.get("value"));
                    DataDictionary dataDictionary = dataDictionaryService.getOne(queryWrapper2);
                    if (dataDictionary!=null) {
                        snapVehicle.setPlateColor(dataDictionary.getValueImplication());
                    }
                    QueryWrapper<DataDictionary> queryWrapper1=new QueryWrapper<>();
                    queryWrapper1.eq("value",(String)vehicleType.get("value"));
                    DataDictionary one = dataDictionaryService.getOne(queryWrapper1);
                    if (one!=null) {
                        snapVehicle.setVehicleType(one.getValueImplication());
                    }
                    QueryWrapper<DataDictionary> queryWrapper3=new QueryWrapper<>();
                    queryWrapper3.eq("value",(String)vehicleColor.get("value"));
                    DataDictionary dictionary = dataDictionaryService.getOne(queryWrapper3);
                    if (dictionary!=null) {
                        snapVehicle.setVehicleColor(dictionary.getValueImplication());
                    }
                    if (plateNo!=null) {
                        snapVehicle.setPlateNo((String)plateNo.get("value"));
                    }
                    snapVehicle.setSerialNumber(deviceID);
                    snapVehicle.setShootTime(new Date());
                    snapVehicle.setCommunityCode(deviceInfo.getCommunityCode());
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
                    snapFaceDataHikService.save(snapFaceDataHik);
                }
            }

        }
    }
}
