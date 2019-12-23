package com.mit.community.entity.hik;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author qsj
 * @since 2019-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HistoricalWarn对象", description="")
public class HistoricalWarn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty(value = "设备id")
    private Integer deviceId;

    @ApiModelProperty(value = "人脸分类")
    private String faceClassification;

    @ApiModelProperty(value = "进出方向:1进,2出,3无")
    private Integer direction;

    @ApiModelProperty(value = "处理结果:1已处理,2未处理")
    private Integer processResult;

    @ApiModelProperty(value = "报警时间")
    private String alarmTime;


}
