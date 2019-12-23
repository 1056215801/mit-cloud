package com.mit.event.model;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description 事件基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "event_base_info")
public class EventBaseInfo extends Model<EventBaseInfo> {
    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件标识
     */
    private String eventCode;
    /**
     * 事件触发时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date happenedTime;
    /**
     * 事件紧急程度
     */
    private Integer emergencyLevel;
    /**
     * 事件严重程度
     */
    private Integer severity;
    /**
     * 事件发生所在小区code
     */
    private String communityCode;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 事件处置状态
     */
    private Integer status;
    /**
     * 流程实例id
     */
    private String processInstanceId;

}
