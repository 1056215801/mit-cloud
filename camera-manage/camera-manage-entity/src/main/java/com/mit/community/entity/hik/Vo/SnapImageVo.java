package com.mit.community.entity.hik.Vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 10:01 2019/12/2
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class SnapImageVo {

    private Integer id;
    private String deviceName;

    private String deviceNumber;

    private String place;

    private String direction;

    private String detailedAddress;

    private String geographicCoordinates;

    private Date shootTime;

    private String installationLocation;

    private String communityCode;

    private String zoneName;

    private String imageUrl;
}
