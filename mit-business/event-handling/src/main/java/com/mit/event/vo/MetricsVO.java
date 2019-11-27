package com.mit.event.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description 统计返回类
 */
@Data
public class MetricsVO {
    private String key;
    private String name;
    private String value;
    private List<MetricsVO> children;
}
