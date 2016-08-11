package com.hqdna.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.user.vo.RoleVo;


@Entity
@Table(name = "TBL_ROLE")
public class Role implements ITransfer<RoleVo>, Serializable {
	private static final long serialVersionUID = -4341743554207700467L;
	private String roleID; // 角色ID
	@FieldTitle(name = "角色代码", fieldType = FieldType.SELECTABLE_VALUE)
	private String roleCode; // 角色代码
	@FieldTitle(name = "角色名称", fieldType = FieldType.SELECTABLE_VALUE)
	private String roleName; // 角色名称
	private byte isEnable; // 是否启用
	private String comment; // 备注
	@FieldTitle(name = "最后操作人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User operatorUser;// 操作用户
	@FieldTitle(name = "最后操作时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp operateDt;// 操作时间

	@Id
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "operator")
	public User getOperatorUser() {
		return operatorUser;
	}

	public void setOperatorUser(User operatorUser) {
		this.operatorUser = operatorUser;
	}

	public Timestamp getOperateDt() {
		return operateDt;
	}

	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
	}

	
	public void transferPo2Vo(RoleVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setRoleID(getRoleID());
			vo.setRoleName(getRoleName());
			vo.setRoleCode(getRoleCode());
			vo.setOperatorID(getOperatorUser().getUserID());
			vo.setOperatorName(getOperatorUser().getUserCnName());
			vo.setOperateDt(getOperateDt());
			vo.setIsEnable(getIsEnable());
			vo.setComment(getComment());
		} else {
			setRoleID(vo.getRoleID());
			setRoleName(vo.getRoleName());
			setRoleCode(vo.getRoleCode());
			setOperateDt(vo.getOperateDt());
			setIsEnable(vo.getIsEnable());
			setComment(vo.getComment());
		}

	}

}
