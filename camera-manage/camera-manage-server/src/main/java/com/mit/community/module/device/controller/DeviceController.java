package com.mit.community.module.device.controller;

import cn.afterturn.easypoi.cache.manager.IFileLoader;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.dto.DeptTree;
import com.mit.common.web.Result;
import com.mit.community.entity.ClusterCommunity;
import com.mit.community.entity.Zone;
import com.mit.community.entity.hik.DeviceInfo;
import com.mit.community.entity.hik.SnapFaceDataHik;
import com.mit.community.entity.hik.Transcode;
import com.mit.community.entity.hik.Vo.CameraVo;
import com.mit.community.entity.hik.Vo.DeviceInfoVo;
import com.mit.community.entity.hik.Vo.EquipmentStatisticsVo;
import com.mit.community.entity.hik.Vo.SnapImageVo;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.feign.SysDeptFeign;
import com.mit.community.service.com.mit.community.service.hik.DeviceInfoService;
import com.mit.community.service.com.mit.community.service.hik.SnapFaceDataHikService;
import com.mit.community.service.com.mit.community.service.hik.TranscodeService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.MarshalException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * @Author qishengjun
 * @Date Created in 17:06 2019/11/28
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "设备管理")
@RequestMapping(value = "/device")
public class DeviceController {
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private SnapFaceDataHikService snapFaceDataHikService;
    @Autowired
    private CommunityFeign communityFeign;
    @Autowired
    private SysDeptFeign sysDeptFeign;
    @Autowired
    private TranscodeService transcodeService;
    @PostMapping("/addDevice")
    @ApiOperation("添加设备")
    public Result addDevice(DeviceInfoVo deviceInfoVo){
        DeviceInfo deviceInfo=new DeviceInfo();
        BeanUtils.copyProperties(deviceInfoVo,deviceInfo);
        deviceInfo.setCreateTime(new Date());
        deviceInfo.setModifiedTime(new Date());
        deviceInfo.setDeviceState(1);
        deviceInfoService.save(deviceInfo);
        return Result.succeed("添加成功");
    }
    @PostMapping("/saveOrUpdateDevice")
    @ApiOperation("新增或者修改设备")
    public Result updateDevice(DeviceInfoVo deviceInfoVo,Integer id){
        DeviceInfo deviceInfo=new DeviceInfo();
        BeanUtils.copyProperties(deviceInfoVo,deviceInfo);
        if (id==null){
            Result result = communityFeign.getCommunitylist();
            List<ClusterCommunity> clusterCommunityList = (List<ClusterCommunity>) result.getDatas();
            String jsonString = JSON.toJSONString(clusterCommunityList);
            List<ClusterCommunity> clusterCommunities = parseArray(jsonString, ClusterCommunity.class);
            String serialNumber = deviceInfoVo.getSerialNumber();
            String communityCode = deviceInfoVo.getCommunityCode();
            for (ClusterCommunity clusterCommunity : clusterCommunities) {
                if (communityCode.equals(clusterCommunity.getCommunityCode())) {
                      deviceInfo.setStreetName(clusterCommunity.getStreetName());
                }
            }
            QueryWrapper<DeviceInfo> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("serial_number",serialNumber);
            List<DeviceInfo> deviceInfos = deviceInfoService.list(queryWrapper);
            if (deviceInfos.size()>0) {
                return Result.failed("设备已经添加,请勿重复添加");
            }
            deviceInfo.setCreateTime(new Date());
            deviceInfo.setModifiedTime(new Date());
            deviceInfo.setDeviceState(1);
            deviceInfoService.save(deviceInfo);
            return Result.succeed("添加成功");
        }
        deviceInfo.setModifiedTime(new Date());
        UpdateWrapper<DeviceInfo> wrapper=new UpdateWrapper<>();
        wrapper.eq("id",id);
        boolean res = deviceInfoService.update(deviceInfo, wrapper);
        if (res){
            return Result.succeed("修改成功");
        }else {
            return Result.failed("修改失败");
        }
    }
    @PostMapping("/deleteDevice")
    @ApiOperation("删除设备")
    @ApiImplicitParam(name = "ids",value = "设备id",dataType = "String",paramType = "query")
    public Result deleteDevice(String ids){
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        boolean res = deviceInfoService.removeByIds(list);
        if (res) {
            return Result.succeed("删除成功");
        }else {
            return Result.failed("删除失败");
        }

    }
    @PostMapping("/getDeviceList")
    @ApiOperation("获取设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communityCodes",value = "小区code集合",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "zoneId",value = "分区id",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "deviceName",value = "设备名称",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "deviceNumber",value = "设备编号",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "deviceType",value = "设备类型",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "deviceState",value = "设备状态",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageNum",value = "当前页",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的记录数",dataType = "Integer",paramType = "query")
    })
    public Result getDeviceList(String communityCodes,Integer zoneId,String deviceName,
                                String deviceNumber,Integer deviceType,Integer deviceState,Integer pageNum,Integer pageSize){
        if (StringUtils.isEmpty(communityCodes)) {
            return Result.succeed("查询成功");
        }
        String[] codesArray = communityCodes.split(",");
        IPage<DeviceInfo> pageList=deviceInfoService.getDeviceList(codesArray,zoneId,deviceName,deviceNumber,deviceType,deviceState,pageNum,pageSize);
        List<DeviceInfo> records = pageList.getRecords();
        Result result = communityFeign.getCommunitylist();
        List<ClusterCommunity> clusterCommunityList = (List<ClusterCommunity>) result.getDatas();
        String jsonString = JSON.toJSONString(clusterCommunityList);
        List<ClusterCommunity> clusterCommunities = parseArray(jsonString, ClusterCommunity.class);
        for (DeviceInfo record : records) {
            for (ClusterCommunity clusterCommunity : clusterCommunities) {
                if (record.getCommunityCode().equals(clusterCommunity.getCommunityCode())){
                      record.setStreetName(clusterCommunity.getStreetName());
                }
            }
        }
        pageList.setRecords(records);
        return Result.succeed(pageList);
    }
      @PostMapping("/getDeviceInfo")
      @ApiOperation("获取设备信息")
      public Result getDeviceInfo(String serialNumber){
          CameraVo cameraVo=new CameraVo();
          QueryWrapper<SnapFaceDataHik> wrapper=new QueryWrapper<>();
          wrapper.eq("serial_number",serialNumber);
          List<SnapFaceDataHik> snapFaceDataHikList = snapFaceDataHikService.list(wrapper);
          QueryWrapper<DeviceInfo> queryWrapper=new QueryWrapper<>();
          queryWrapper.eq("serial_number",serialNumber);
          DeviceInfo deviceInfo = deviceInfoService.getOne(queryWrapper);
          String communityCode = deviceInfo.getCommunityCode();
          String ipAdress = deviceInfo.getIpAdress();
          cameraVo.setDeviceName(deviceInfo.getDeviceName());
          cameraVo.setInstallationLocation(deviceInfo.getInstallationLocation());
          if (deviceInfo.getDirection()==1){
              cameraVo.setDirection("进");
          }else if (deviceInfo.getDirection()==2){
              cameraVo.setDirection("出");
          }else {
              cameraVo.setDirection("无");
          }
          QueryWrapper<Transcode> transcodeQueryWrapper=new QueryWrapper<>();
          transcodeQueryWrapper.eq("community_code",communityCode);
          transcodeQueryWrapper.eq("ipAdress",ipAdress);
          Transcode transcode = transcodeService.getOne(transcodeQueryWrapper);
          if (transcode!=null){
              cameraVo.setChannel(transcode.getChannel());
              cameraVo.setIpAdress(transcode.getIpAdress());
          }
          cameraVo.setSnapFaceDataHikList(snapFaceDataHikList);
          cameraVo.setPlaybackAddress(deviceInfo.getPlaybackAddress());
          cameraVo.setCommunityCode(deviceInfo.getCommunityCode());
          return Result.succeed(cameraVo);
      }
      @PostMapping("/getDeviceTotal")
      @ApiOperation("统计设备数量")
      public List<EquipmentStatisticsVo> getDeviceTotal(@RequestParam("communityCodeList") List<String> communityCodeList){
        List<EquipmentStatisticsVo> equipmentStatisticsVoList=deviceInfoService.getDeviceTotal(communityCodeList);
        return equipmentStatisticsVoList;
      }
       @PostMapping("/importExcel")
       @ApiOperation("Excel数据导入")
       @Transactional(rollbackFor = Exception.class)
       public Result importExcel(@RequestParam("file") MultipartFile file){
           Result result = communityFeign.getCommunitylist();
           List<ClusterCommunity> clusterCommunityList = (List<ClusterCommunity>) result.getDatas();
           String jsonString = JSON.toJSONString(clusterCommunityList);
           List<ClusterCommunity> clusterCommunities = parseArray(jsonString, ClusterCommunity.class);
           //1 获取文件输入流
           StringBuilder stringBuilder=new StringBuilder();
           try {
               InputStream in = file.getInputStream();
               //2 创建workbook
               Workbook workbook = new HSSFWorkbook(in);
               //3 workbook获取sheet
               Sheet sheet = workbook.getSheetAt(0);
               //为了存储错误信息
               List<String> msg = new ArrayList<>();
               //4 sheet获取row
               //循环遍历获取行，从第二行开始获取数据
               int lastRowNum = sheet.getLastRowNum();
               // sheet.getRow(0)
               Row sheetRow = sheet.getRow(0);
               Cell cell1 = sheetRow.getCell(1);
               String[] strArray={"*小区名称","*分区名称","*设备名称","*设备编号","*设备类型","*设备序列号","*IP地址","*设备位置","经纬度","*进/出","*视频分辨率","*是否支持WIFI探针","播放地址"};
               List<String> list = Arrays.asList(strArray);
               for (int i = 0; i < list.size(); i++) {
                   if (!list.get(i).equalsIgnoreCase(sheetRow.getCell(i+1).getStringCellValue())){
                       return Result.failed("请使用正确的导入模板");
                   }
               }
               List<DeviceInfo> deviceInfoList=new ArrayList<>();
               for (int i = 1; i <= lastRowNum; i++) {
                   DeviceInfo deviceInfo=new DeviceInfo();
                   //得到excel每一行
                   Row row = sheet.getRow(i);
                   //行数据不为空
                   //5 row获取第一列
                   Cell cell = row.getCell(0);
                   cell.setCellType(CellType.STRING);
                   if (cell==null||row.getCell(0).getStringCellValue().equalsIgnoreCase("")){
                       break;
                   }
                   Cell cellOne = row.getCell(1);
                   //列不为空获取数据,第一列值
                   //一级分类值
                   for (int j = 0; j <14 ; j++) {
                       if (j==9||j==13){
                           continue;
                       }
                       int temp = checkCell(row.getCell(j), stringBuilder, i, j);
                       if (temp==-1){
                           return Result.failed(stringBuilder.toString());
                       }
                   }
                   String communityName = cellOne.getStringCellValue();
                   for (ClusterCommunity clusterCommunity : clusterCommunities) {
                       if (communityName.equals(clusterCommunity.getCommunityName())){
                           deviceInfo.setCommunityCode(clusterCommunity.getCommunityCode());
                           deviceInfo.setStreetName(clusterCommunity.getStreetName());
                       }
                   }
                   //5 row获取第二列
                   Cell cellTwo = row.getCell(2);
                   //不为空，获取第二列值
                   String zoneName = cellTwo.getStringCellValue();
                   Result result1 = communityFeign.getZonelist(deviceInfo.getCommunityCode());
                   List<Zone> zoneList=(List<Zone>)result1.getDatas();
                   String jsonstr = JSON.toJSONString(zoneList);
                   List<Zone> zones = parseArray(jsonstr, Zone.class);
                   for (Zone zone : zones) {
                       if (zoneName.equals(zone.getZoneName())){
                           deviceInfo.setZoneId(zone.getZoneId());
                       }
                   }
                   deviceInfo.setCommunityName(communityName);
                   deviceInfo.setZoneName(zoneName);
                   Cell cellThree = row.getCell(3);
                   String deviceName = cellThree.getStringCellValue();
                   deviceInfo.setDeviceName(deviceName);
                   Cell cellfour = row.getCell(4);
                   cellfour.setCellType(CellType.STRING);
                   String deviceNumber=cellfour.getStringCellValue();
                   deviceInfo.setDeviceNumber(deviceNumber);
                   Cell cell5 = row.getCell(5);
                   String deviceType = cell5.getStringCellValue();
                   if ("普通摄像机".equals(deviceType)){
                       deviceInfo.setDeviceType(1);
                   }else if ("人脸摄像机".equals(deviceType)){
                       deviceInfo.setDeviceType(2);
                   }else if ("全结构摄像机".equals(deviceType)){
                       deviceInfo.setDeviceType(3);
                   }
                   Cell cell6=row.getCell(6);
                   cell6.setCellType(CellType.STRING);
                   String serialNumber = cell6.getStringCellValue();
                   deviceInfo.setSerialNumber(serialNumber);
                   Cell cell7 = row.getCell(7);
                   String ipAdress = cell7.getStringCellValue();
                   deviceInfo.setIpAdress(ipAdress);
                   Cell cell8 = row.getCell(8);
                   String installationLocation = cell8.getStringCellValue();
                   deviceInfo.setInstallationLocation(installationLocation);
                   Cell cell9 = row.getCell(9);
                   if (cell.getCellType()==CellType.BLANK){
                       String geographicCoordinates = cell9.getStringCellValue();
                       deviceInfo.setGeographicCoordinates(geographicCoordinates);
                   }
                   Cell cell10 = row.getCell(10);
                   String direction = cell10.getStringCellValue();
                   if ("进".equals(direction)){
                       deviceInfo.setDirection(1);
                   }else if ("出".equals(direction)){
                       deviceInfo.setDirection(2);
                   }else {
                       deviceInfo.setDirection(0);
                   }
                   Cell cell11 = row.getCell(11);
                   cell11.setCellType(CellType.STRING);
                   String videoResolution = cell11.getStringCellValue();
                   deviceInfo.setVideoResolution(videoResolution);
                   Cell cell12 = row.getCell(12);
                   String wifiProbe = cell12.getStringCellValue();
                   if ("否".equals(wifiProbe)){
                       deviceInfo.setWifiProbe(0);
                   }else if ("是".equals(wifiProbe)){
                       deviceInfo.setWifiProbe(1);
                   }
                   Cell cell13 = row.getCell(13);
                   if (cell.getCellType()==CellType.BLANK){
                       String playbackAddress = cell13.getStringCellValue();
                       deviceInfo.setPlaybackAddress(playbackAddress);
                   }
                   deviceInfo.setDeviceState(1);
                   deviceInfo.setCreateTime(new Date());
                   deviceInfo.setModifiedTime(new Date());
                   deviceInfoList.add(deviceInfo);
               }
               deviceInfoService.saveOrUpdateList(deviceInfoList);
           } catch (Exception e) {
               return Result.failed("导入失败");
           } finally {
           }
              return Result.succeed("导入成功");
       }
      public int checkCell(Cell cell, StringBuilder stringBuilder,int i,int j){
          if(cell.getCellType()==CellType.BLANK){
              stringBuilder.append("出错啦！请检查第"+(i+1)+"行第"+(j+1)+"列。"+"如果您在该行没有数据，建议您选择删除该行，重试！");
              return -1;
          }
          return 0;
      }
       @Transactional(rollbackFor = Exception.class)
       @PostMapping("/exportExcel")
       @ApiOperation("excel导出")
       public Result exportExcel(){
           List<DeviceInfo> deviceInfoList = deviceInfoService.list();
           try {
               //1.在内存中创建同一个excel文件
               HSSFWorkbook workbook = new HSSFWorkbook();
               //2.创建工作簿
               HSSFSheet sheet = workbook.createSheet("摄像头数据表");
               sheet.autoSizeColumn(1, true);
               HSSFCellStyle style = workbook.createCellStyle();
               style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
               style.setAlignment(HorizontalAlignment.CENTER);// 水平
               style.setWrapText(true);
               Font font = workbook.createFont();
               font.setFontName("仿宋_GB2312");
               font.setFontHeightInPoints((short)11);
               style.setFont(font);
               HSSFRow titleRow = sheet.createRow(0);
               titleRow.setHeightInPoints(20);
               titleRow.createCell(0).setCellValue("序号");
               titleRow.createCell(1).setCellValue("小区名称");
               titleRow.createCell(2).setCellValue("分区名称");
               titleRow.createCell(3).setCellValue("设备名称");
               titleRow.createCell(4).setCellValue("设备编号");
               titleRow.createCell(5).setCellValue("设备类型");
               titleRow.createCell(6).setCellValue("设备序列号");
               titleRow.createCell(7).setCellValue("ip地址");
               titleRow.createCell(8).setCellValue("设备位置");
               titleRow.createCell(9).setCellValue("经纬度");
               titleRow.createCell(10).setCellValue("进/出");
               titleRow.createCell(11).setCellValue("视频分辨率");
               titleRow.createCell(12).setCellValue("播放地址");
               titleRow.createCell(13).setCellValue("是否支持WIFI探针");
               setStyle(titleRow,style);
               for (int i = 0; i < deviceInfoList.size(); i++) {
                   DeviceInfo deviceInfo = deviceInfoList.get(i);
                   //获取最后一行的行号
                   int lastRowNum = sheet.getLastRowNum();
                   HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
                   dataRow.setHeightInPoints(20);
                   dataRow.createCell(0).setCellValue(i+1);
                   dataRow.createCell(1).setCellValue(deviceInfo.getCommunityName());
                   dataRow.createCell(2).setCellValue(deviceInfo.getZoneName());
                   dataRow.createCell(3).setCellValue(deviceInfo.getDeviceName());
                   dataRow.createCell(4).setCellValue(deviceInfo.getDeviceNumber());
                   if (deviceInfo.getDeviceType()==1){
                       dataRow.createCell(5).setCellValue("普通摄像机");
                   }else if (deviceInfo.getDeviceType()==2){
                       dataRow.createCell(5).setCellValue("人脸摄像机");
                   }else if (deviceInfo.getDeviceType()==3){
                       dataRow.createCell(5).setCellValue("全结构摄像机");
                   }
                   dataRow.createCell(6).setCellValue(deviceInfo.getSerialNumber());
                   dataRow.createCell(7).setCellValue(deviceInfo.getIpAdress());
                   dataRow.createCell(8).setCellValue(deviceInfo.getInstallationLocation());
                   dataRow.createCell(9).setCellValue(deviceInfo.getGeographicCoordinates());
                   if (deviceInfo.getDirection()==1){
                       dataRow.createCell(10).setCellValue("进");
                   }else if (deviceInfo.getDirection()==2){
                       dataRow.createCell(10).setCellValue("出");
                   }else {
                       dataRow.createCell(10).setCellValue("未知");
                   }
                   dataRow.createCell(11).setCellValue(deviceInfo.getVideoResolution());
                   dataRow.createCell(12).setCellValue(deviceInfo.getPlaybackAddress());
                   if (deviceInfo.getWifiProbe()==0){
                       dataRow.createCell(13).setCellValue("否");
                   }else if (deviceInfo.getWifiProbe()==1){
                       dataRow.createCell(13).setCellValue("是");
                   }
                   setStyle(dataRow,style);
               }
               //让列宽随着导出的列长自动适应
               for (int colNum = 0; colNum < 14; colNum++) {
                   int columnWidth = sheet.getColumnWidth(colNum) / 256;
                   for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                       HSSFRow currentRow;
                       //获取当前行
                       currentRow = sheet.getRow(rowNum);
                       if (currentRow.getCell(colNum) != null) {
                           HSSFCell currentRowCell = currentRow.getCell(colNum);
                           if (currentRowCell.getCellType() == CellType.STRING) {
                               int length = currentRowCell.getStringCellValue().getBytes().length;
                               if (columnWidth < length) {
                                   columnWidth = length;
                               }
                           }
                       }
                   }
                       sheet.setColumnWidth(colNum, columnWidth * 256);
               }
               //5.创建文件名
               File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
               String desktopPath = desktopDir.getAbsolutePath();
               System.out.println(desktopPath);
               Date d = new Date();
               SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
               String time = sdf.format(d);
               String fileName = desktopPath+"\\摄像机数据"+time+".xls";
               File file = new File(fileName);
               //6.获取文件输出流
               FileOutputStream os = new FileOutputStream(file);
               workbook.write(os);
               //7.关闭流
               workbook.close();
               os.close();
               return Result.succeed("导出成功");
           } catch (IOException e) {
               e.printStackTrace();
           } finally {
           }
           return Result.failed("导出失败");
       }
       public void setStyle(HSSFRow dataRow,HSSFCellStyle style){
           for (int i = 0; i <14 ; i++) {
               dataRow.getCell(i).setCellStyle(style);
           }
       }
       @PostMapping("/tranCode")
       @ApiOperation("转码")
       public void transCode(String jsonList){
           List<Map> mapList = JSON.parseArray(jsonList, Map.class);
           for (Map map : mapList) {
               Transcode transcode=new Transcode();
               String ipStr = (String) map.get("ipStr");
               String channel = (String) map.get("channelNo");
               String communityCode = (String) map.get("communityCode");
               transcode.setIpAdress(ipStr);
               transcode.setCommunityCode(communityCode);
               transcode.setChannel(channel);
               QueryWrapper<Transcode> wrapper=new QueryWrapper<>();
               wrapper.eq("ipAdress",ipStr);
               wrapper.eq("community_code",communityCode);
               Transcode one = transcodeService.getOne(wrapper);
               if (one==null) {
                   transcodeService.save(transcode);
               }
           }
           System.out.println("转码"+mapList);
       }

}
