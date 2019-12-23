package com.mit.community.entity.hik.Vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 17:05 2019/11/15
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class RealTimeVo {

     private String installationLocation;
     private Date shootTime;
     private Integer snapNumber;
     private Integer peopleNumber;
}
