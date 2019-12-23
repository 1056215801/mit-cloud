package com.mit.community.service.com.mit.community.service.hik;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.ClusterCommunity;
import com.mit.community.entity.hik.DeviceInfo;
import com.mit.community.entity.hik.IntranetPenetration;
import com.mit.community.entity.hik.Vo.EquipmentStatisticsVo;
import com.mit.community.entity.hik.Vo.SnapImageVo;
import com.mit.community.mapper.DeviceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qsj
 * @since 2019-11-14
 */
@Service
public class DeviceInfoService extends ServiceImpl<DeviceInfoMapper,DeviceInfo> {

    public IPage<DeviceInfo> getDeviceList(String[] codesArray, Integer zoneId, String deviceName, String deviceNumber, Integer deviceType, Integer deviceState, Integer pageNum, Integer pageSize) {
        Page<DeviceInfo> page=new Page<>(pageNum,pageSize);
        QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
        if (zoneId!=null){
            wrapper.eq("zone_id",zoneId);
        }
        if (StringUtils.isNotEmpty(deviceName)){
            wrapper.like("device_name",deviceName);
        }
        if (StringUtils.isNotEmpty(deviceNumber)){
            wrapper.like("device_number",deviceNumber);
        }
        if (deviceType!=null){
            wrapper.eq("device_type",deviceType);
        }
        if (deviceState!=null){
            wrapper.eq("device_state",deviceState);
        }
        wrapper.in("communityCode",codesArray);
        wrapper.orderByDesc("create_time");
        IPage<DeviceInfo> deviceInfoIPage = baseMapper.selectPage(page, wrapper);
        return deviceInfoIPage;
    }

    public SnapImageVo getImageInfo(Integer id) {
        return baseMapper.getImageInfo(id);
    }

    public void saveOrUpdateList(List<DeviceInfo> deviceInfoList) {
        for (DeviceInfo deviceInfo : deviceInfoList) {
            QueryWrapper<DeviceInfo> wrapper=new QueryWrapper<>();
            wrapper.eq("serial_number",deviceInfo.getSerialNumber());
            List<DeviceInfo> deviceInfos = baseMapper.selectList(wrapper);
            if (deviceInfos.size()>0) {
                baseMapper.update(deviceInfo,wrapper);
            }else {
                baseMapper.insert(deviceInfo);
            }
        }

    }

    public IntranetPenetration getInfo(String arr) {
        return baseMapper.getInfo(arr);
    }

    public List<EquipmentStatisticsVo> getDeviceTotal(List<String> communityCodeList) {
        return baseMapper.getDeviceTotal(communityCodeList);
    }
}
