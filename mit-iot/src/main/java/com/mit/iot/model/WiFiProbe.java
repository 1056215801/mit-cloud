package com.mit.iot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.mit.iot.dto.hkwifi.WiFiProbeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description WIFI探针设备信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName("wifi_probe")
public class WiFiProbe extends Model<WiFiProbe> {
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
     * 平台indexCode
     */
    private String indexCode;
    /**
     * 设备mac地址
     */
    private String mac;

    public WiFiProbe(WiFiProbeDTO wiFiProbeDTO) {
        this.indexCode = wiFiProbeDTO.getIndexCode();
        this.mac = wiFiProbeDTO.getMac();
    }
}
