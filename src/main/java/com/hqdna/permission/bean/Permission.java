package com.hqdna.permission.bean;

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
import com.hqdna.permission.vo.PermItem;
import com.hqdna.user.bean.User;


@Entity
@Table(name = "TBL_PERMISSION")
public class Permission implements ITransfer<PermItem>,Serializable {
	private static final long serialVersionUID = 6369459850454363159L;
	private String permID; // 权限项ID
	private String code; // 权限编码
	private String parentCode; // 父节点编码
	private String permName; // 权限项名称
	private String permType; // 权限项类型
	private String module;	//关联模块
	private String refObjID; // 关联对象
	private byte isEnable; // 是否启用
	private String nodeUrl; // 节点对应url
	private byte enableDataPermission;//启用数据权限
	private String comment; // 备注
	private User operatorUser; // 操作人员
	private Timestamp operateDt; // 操作时间

	@Id
	public String getPermID() {
		return permID;
	}

	public void setPermID(String permID) {
		this.permID = permID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getPermName() {
		return permName;
	}

	public void setPermName(String permName) {
		this.permName = permName;
	}

	public String getPermType() {
		return permType;
	}

	public void setPermType(String permType) {
		this.permType = permType;
	}
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRefObjID() {
		return refObjID;
	}

	public void setRefObjID(String refObjID) {
		this.refObjID = refObjID;
	}

	public byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(byte isEnable) {
		this.isEnable = isEnable;
	}

	public String getNodeUrl() {
		return nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public byte getEnableDataPermission() {
		return enableDataPermission;
	}

	public void setEnableDataPermission(byte enableDataPermission) {
		this.enableDataPermission = enableDataPermission;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public void transferPo2Vo(PermItem vo, boolean direction) {
		if ( vo == null) {
			return;
		}
		if (direction) {
			vo.setCode(getCode());
			vo.setParentCode(getParentCode());
			vo.setComment(getComment());
			vo.setEnableDataPermission(getEnableDataPermission());
			vo.setNodeUrl(getNodeUrl());
			vo.setOperateDt(getOperateDt());
			vo.setOperatorName(getOperatorUser().getUserCnName());
			vo.setPermID(getPermID());
			vo.setPermName(getPermName());
			vo.setPermType(getPermType());
			vo.setRefObjID(getRefObjID());
		} else {
			setCode(vo.getCode());
			setParentCode(vo.getParentCode());
			setComment(vo.getComment());
			setEnableDataPermission(vo.getEnableDataPermission());
			setNodeUrl(vo.getNodeUrl());
			setOperateDt(vo.getOperateDt());
			setPermID(vo.getPermID());
			setPermName(vo.getPermName());
			setPermType(vo.getPermType());
			setRefObjID(vo.getRefObjID());
		}
		
	}

}
