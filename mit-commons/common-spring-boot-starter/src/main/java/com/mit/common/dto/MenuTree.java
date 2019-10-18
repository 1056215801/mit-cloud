package com.mit.common.dto;

import com.mit.common.model.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 菜单权限树
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {
	private String name;
	private Integer type;
	private String path;
	private String permissionCode;
	private String icon;
	private Integer sort;
	private Boolean hidden;

	public MenuTree(SysMenu sysMenu) {
		this.id = sysMenu.getId();
		this.parentId = sysMenu.getParentId();
		this.name = sysMenu.getName();
		this.type = sysMenu.getType();
		this.path = sysMenu.getPath();
		this.permissionCode = sysMenu.getPermissionCode();
		this.icon = sysMenu.getIcon();
		this.sort = sysMenu.getSort();
		this.hidden = sysMenu.getHidden();
	}
}
