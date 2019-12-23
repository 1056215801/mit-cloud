package com.mit.community.entity.hik.Vo;

import lombok.Data;

/**
 * @Author qishengjun
 * @Date Created in 15:43 2019/11/18
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class OverviewVo {
    private Integer entryNumber;//进入人数

    private Integer leaveNumber;//离开人数

    private Integer ordinaryNumber;//普通人员

    private Integer specialNumber;//特殊人员

    private Integer residentNumber;//驻留人员
}
