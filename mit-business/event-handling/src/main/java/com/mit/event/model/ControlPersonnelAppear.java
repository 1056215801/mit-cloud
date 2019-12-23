package com.mit.event.model;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mit.event.dto.ControlPersonnelDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 布控人员出现事件
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("event_control_personnel_appear")
public class ControlPersonnelAppear extends Model<ControlPersonnelAppear> {
    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 关联 BaseEvent 表的id
     */
    private String eventBaseInfoId;
    /**
     * 抓拍图片地址
     */
    private String captureImageUrl;
    /**
     * 抓拍时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date captureTime;
    /**
     * 姓名
     */
    private String username;
    /**
     * 性别
     */
    private String sex;
    /**
     * 证件类型
     */
    private String certificateType;
    /**
     * 证件号码
     */
    private String certificateNumber;
    /**
     * 人员类型
     */
    private String personType;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceCode;

    public ControlPersonnelAppear(ControlPersonnelDTO controlPersonnelDTO) {
        this.setCaptureImageUrl(controlPersonnelDTO.getCaptureImageUrl());
        this.setCaptureTime(controlPersonnelDTO.getCaptureTime());
        this.setUsername(controlPersonnelDTO.getUsername());
        this.setSex(controlPersonnelDTO.getSex());
        this.setCertificateType(controlPersonnelDTO.getCertificateType());
        this.setCertificateNumber(controlPersonnelDTO.getCertificateNumber());
        this.setPersonType(controlPersonnelDTO.getPersonType());
        this.setDeviceName(controlPersonnelDTO.getDeviceName());
        this.setDeviceCode(controlPersonnelDTO.getDeviceCode());
    }

    public ControlPersonnelAppear(ControlPersonnelDTO controlPersonnelDTO, String eventBaseInfoId) {
        this.setEventBaseInfoId(eventBaseInfoId);
        this.setCaptureImageUrl(controlPersonnelDTO.getCaptureImageUrl());
        this.setCaptureTime(controlPersonnelDTO.getCaptureTime());
        this.setUsername(controlPersonnelDTO.getUsername());
        this.setSex(controlPersonnelDTO.getSex());
        this.setCertificateType(controlPersonnelDTO.getCertificateType());
        this.setCertificateNumber(controlPersonnelDTO.getCertificateNumber());
        this.setPersonType(controlPersonnelDTO.getPersonType());
        this.setDeviceName(controlPersonnelDTO.getDeviceName());
        this.setDeviceCode(controlPersonnelDTO.getDeviceCode());
    }
}
