package com.mit.event.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 事件配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "event_config")
public class EventConfig extends Model<EventConfig> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件标识
     */
    private String eventCode;
    /**
     * 事件分类（公共管理、公共安全、公共服务）
     */
    private String classification;
    /**
     * 事件类型（设施管理、平安治理、道路交通等）
     */
    private String type;
    /**
     * 事件来源（主动发现、群众上报、设备感知等）
     */
    private String source;
    /**
     * 事件处置类型（流程、提醒）
     */
    private String dispositionType;
    /**
     * 是否推送网格
     */
    private boolean isPush;
    /**
     * 推送内容
     */
    private String pushContent;
    /**
     * 流程key
     */
    private String processKey;
}
