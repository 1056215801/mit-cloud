package com.mit.community.entity.hik.Vo;

import com.mit.community.entity.hik.SnapFaceDataHik;
import lombok.Data;

import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 15:50 2019/12/2
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class CameraVo {

    private String deviceName;

    private String installationLocation;

    private String direction;

    private String ipAdress;

    private String channel;

    private String playbackAddress;

    private String communityCode;
    List<SnapFaceDataHik> snapFaceDataHikList;
}
