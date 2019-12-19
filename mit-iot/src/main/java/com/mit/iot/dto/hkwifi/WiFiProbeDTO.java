package com.mit.iot.dto.hkwifi;

import com.mit.iot.dto.BaseDeviceInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @Description WIFI探针新增或修改转换类
 */
@ApiModel(description = "WIFI探针设备")
@EqualsAndHashCode(callSuper = true)
@Data
public class WiFiProbeDTO extends BaseDeviceInfoDTO {

    @ApiModelProperty(value = "平台indexCode", required = true)
    @NotBlank
    private String indexCode;

    @ApiModelProperty(value = "设备mac地址")
    private String mac;

}
