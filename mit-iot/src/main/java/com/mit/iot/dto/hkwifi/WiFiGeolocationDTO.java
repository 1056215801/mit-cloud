package com.mit.iot.dto.hkwifi;

import com.mit.iot.protocol.hkwifi.GeolocationStruct;
import com.mit.iot.util.ByteUtils;
import lombok.Data;

import java.util.Date;

/**
 * @Description wifi探针地理位置转换
 */
@Data
public class WiFiGeolocationDTO {

    public WiFiGeolocationDTO(GeolocationStruct geolocationStruct) {
        this.indexCode = ByteUtils.byte2String(geolocationStruct.getIndexCode());
        this.longitude = ByteUtils.byte2String(geolocationStruct.getLongitude());
        this.latitude = ByteUtils.byte2String(geolocationStruct.getLatitude());
        this.siteCode = ByteUtils.byte2String(geolocationStruct.getSiteCode());
        this.acquisitionTime = new Date(geolocationStruct.getAcquisitionTime() * 1000);
    }

    private String indexCode;
    private String longitude;
    private String latitude;
    private String siteCode;
    private Date acquisitionTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------解析后的地理位置信息-------").append(System.lineSeparator());
        sb.append("|平台index code---").append(indexCode).append(System.lineSeparator());
        sb.append("|经度-------").append(longitude).append(System.lineSeparator());
        sb.append("|纬度-------").append(latitude).append(System.lineSeparator());
        sb.append("|site code--").append(siteCode).append(System.lineSeparator());
        sb.append("|获取时间----").append(acquisitionTime).append(System.lineSeparator());
        return sb.toString();
    }
}
