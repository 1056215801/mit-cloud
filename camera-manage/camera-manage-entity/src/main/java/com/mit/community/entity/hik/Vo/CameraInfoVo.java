package com.mit.community.entity.hik.Vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 10:04 2019/11/18
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class CameraInfoVo {

    private String imageUrl;

    private Date shootTime;

    private String deviceName;

    private String installationLocation;

    private Integer peopleNumber;

    private String direction;
}
