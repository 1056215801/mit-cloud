package com.mit.user.vo;

import com.mit.common.model.SysRole;
import com.mit.common.model.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description 返回给前台对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVO extends SysUser {
    /**
     * 部门名称
     */
    String deptName;

    /**
     * 角色列表
     */
    List<SysRole> roleList;

}
