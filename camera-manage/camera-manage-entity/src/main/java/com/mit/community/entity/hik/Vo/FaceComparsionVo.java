package com.mit.community.entity.hik.Vo;

import lombok.Data;

import java.util.Date;
@Data
public class FaceComparsionVo {

    private String faceUrl;
    private String snapUrl;
    private Date shootTime;
    private Integer similarity;
    private String name;
    private String sex;
    private String faceClassification;
    private String deviceName;
}
