package com.mit.iot.dto.hkwifi;

import com.mit.iot.protocol.hkwifi.DeviceStatusStruct;
import com.mit.iot.util.ByteUtils;
import lombok.Data;

import java.util.Date;

/**
 * @Description 海康wifi探针设备状态转换类
 */
@Data
public class WiFiDeviceStatusDTO {

    public WiFiDeviceStatusDTO(DeviceStatusStruct deviceStatusStruct) {
        this.indexCode = ByteUtils.byte2String(ByteUtils.unsignedByte2Bytes(deviceStatusStruct.getIndexCode())).trim();
        this.status = deviceStatusStruct.getStatus();
        this.mac = ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(deviceStatusStruct.getSourceMacAddr()));
        this.acquisitionTime = new Date(deviceStatusStruct.getAcquisitionTime() * 1000);
    }

    private String indexCode;
    private int status;
    private String mac;
    private Date acquisitionTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------解析后的设备状态信息-------").append(System.lineSeparator());
        sb.append("|平台index code---").append(indexCode).append(System.lineSeparator());
        sb.append("|设备状态----").append(status).append(System.lineSeparator());
        sb.append("|MAC地址-----").append(mac).append(System.lineSeparator());
        sb.append("|获取时间----").append(acquisitionTime).append(System.lineSeparator());
        return sb.toString();
    }
}
