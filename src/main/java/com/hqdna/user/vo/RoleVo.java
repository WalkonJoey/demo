package com.hqdna.user.vo;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 角色对象类
 * 
 * 
 */
public class RoleVo extends AbstractBizObject {

	private static final long serialVersionUID = -148198275203846965L;
	private String roleID; // 角色ID
	private String roleCode; // 角色代码
	private String roleName; // 角色名称
	private byte isEnable; // 是否启用
	private String comment; // 备注

	public String getRoleID() {
		return roleID;
	}

	public void setRoleID(String roleID) {
		this.roleID = roleID;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(byte isEnable) {
		this.isEnable = isEnable;
	}

}
