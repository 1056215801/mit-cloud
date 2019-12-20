package com.mit.iot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mit.iot.dto.sensor.SensorSamplingDevice1To4Data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description 消防栓
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("fire_hydrant")
public class FireHydrant extends Model<FireHydrant> {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 关联的 BaseDeviceInfo 主键
     */
    private Long baseDeviceInfoId;
    /**
     * 15位IMEI号码
     */
    @TableField(value = "imei")
    private String IMEI;
    /**
     * 电池电量，百分比
     */
    private Integer batteryLevel;
    /**
     * 信号强度
     */
    private Integer signalStrength;
    /**
     * 数据状态
     */
    private Integer dataStatus;
    /**
     * 数据值
     */
    private String dataValue;
    /**
     * 计量单位
     */
    private String dataMeasurementUnit;

    public FireHydrant(SensorSamplingDevice1To4Data data) {
        this.IMEI = data.getIMEI();
        this.batteryLevel = data.getBatteryLevel();
        this.signalStrength = data.getSignalStrength();
        this.dataStatus = data.getDataStatus();
        this.dataValue = data.getValue();
        this.dataMeasurementUnit = data.getMeasurementUnitString();
    }

}
