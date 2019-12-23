package com.mit.workflow.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.lang.annotation.Target;

/**
 * @Description 添加修改模型的对象
 */
@Data
public class ModelDTO {
    private String modelId;
    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String name;
    /**
     * 模型key
     */
    @NotBlank(message = "key值不能为空")
    private String key;
    /**
     * 描述
     */
    private String description;
}
