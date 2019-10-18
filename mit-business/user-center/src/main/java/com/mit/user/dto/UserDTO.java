package com.mit.user.dto;

import com.mit.common.model.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description 前台创建修改用户传参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

    /**
     * 角色ID
     */
    private List<Long> role;
}
