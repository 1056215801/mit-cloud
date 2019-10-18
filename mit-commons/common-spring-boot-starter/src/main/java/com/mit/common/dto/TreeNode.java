package com.mit.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 树结构
 */
@Data
public class TreeNode {
	protected long id;
	protected long parentId;
	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public void add(TreeNode node) {
		children.add(node);
	}
}
