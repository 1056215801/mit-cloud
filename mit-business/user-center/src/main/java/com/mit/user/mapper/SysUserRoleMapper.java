package com.mit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.common.model.SysRole;
import com.mit.user.model.SysRoleUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zlt
 * 用户角色关系
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysRoleUser> {
    int deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Insert("insert into sys_role_user(user_id, role_id) values(#{userId}, #{roleId})")
    int saveUserRoles(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 根据用户id获取角色
     *
     * @param userId
     * @return
     */
    List<SysRole> findRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ids 获取
     *
     * @param userIds
     * @return
     */
    @Select("<script>select r.*,ru.user_id from sys_role_user ru inner join sys_role r on r.id = ru.role_id where ru.user_id IN " +
            " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> " +
            " #{item} " +
            " </foreach>" +
            "</script>")
    List<SysRole> findRolesByUserIds(List<Long> userIds);


}
