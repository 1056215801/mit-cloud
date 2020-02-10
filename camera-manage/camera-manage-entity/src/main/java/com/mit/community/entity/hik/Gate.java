package com.mit.community.entity.hik;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mit.community.entity.BaseEntity;
import com.mit.community.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("gate")
public class Gate extends BaseEntity {
    private String name;
    private String number;
    private String remarks;

    @TableField("community_code")
    private String communityCode;

    @TableField("community_name")
    private String communityName;

    @TableField("zone_id")
    private Integer zoneId;

    @TableField("zone_name")
    private String zoneName;

    private String location;

    @TableField("device_num_out")
    private String deviceNumOut;

    @TableField("device_num_in")
    private String deviceNumIn;

    @TableField("care_parkno_out")
    private String careParkNoOut;

    @TableField("care_parkno_in")
    private String careParkNoIn;

    private String camera;

   /* @TableField(exist = false)
    private List<Device> deviceOutList;

    @TableField(exist = false)
    private List<Device> deviceInList;

    @TableField(exist = false)
    private List<DaoZha> daoZhaOutList;

    @TableField(exist = false)
    private List<DaoZha> daoZhaInList;

    @TableField(exist = false)
    private List<DeviceInfo> cameraList;*/
}
