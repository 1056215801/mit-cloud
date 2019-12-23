package com.mit.iot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基类
 * @author shuyy
 * @date 2018/11/19
 * @company mitesofor
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEntity extends Model<BaseEntity> implements Serializable {

    @TableId(type = IdType.AUTO)
    protected Integer id;

    /**修改时间*/
    protected String gmtModified;

}
