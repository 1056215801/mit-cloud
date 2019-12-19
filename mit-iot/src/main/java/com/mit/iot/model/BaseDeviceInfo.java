package com.mit.iot.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mit.common.model.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 设备基本信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("base_device_info")
public class BaseDeviceInfo extends SuperEntity {

    /**
     * 所属小区code
     */
    private String communityCode;
    /**
     * 所属小区名称
     */
    private String communityName;
    /**
     * 所属分区ID
     */
    private String zoneId;
    /**
     * 所属分区名称
     */
    private String zoneName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备编号
     */
    private String deviceNo;
    /**
     * 设备序列号
     */
    private String deviceSerialNo;
    /**
     * 设备位置
     */
    private String deviceLocation;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 设备状态，0 - 异常，1 - 正常
     */
    private Integer status;
    /**
     * 设备状态或地理位置上报时间
     */
    private Date acquisitionTime;

    @TableField(exist = false)
    private Object extension;

}
