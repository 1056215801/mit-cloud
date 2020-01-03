package com.mit.community.entity.hik.Vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 17:34 2019/12/23
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class SnapVehicleVo {
    public Integer id;
    public String plateNo;
    public String vehicleType;
    public String vehicleColor;
    public String plateType;
    public String plateColor;
    public String deviceName;
    public String deviceNumber;
    public String place;
    public Date shootTime;
    public String imageUrl;
    public String geographicCoordinates;
    public String snapshotSite;
    public Integer direction;
    public String address;
    public String communityCode;
}
