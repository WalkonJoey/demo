package com.hqdna.permission.vo;

import java.io.Serializable;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 权限项VO类
 * 
 * 
 */
public class PermItem extends AbstractBizObject implements Comparable<PermItem>,Serializable {
	private static final long serialVersionUID = 4667970419079691734L;
	private String permID; // 权限项ID
	private String code; // 权限编码
	private String parentCode; // 父节点编码
	private String permName; // 权限项名称
	private String permType; // 权限项类型
	private String module;	//模块
	private String refObjID; // 关联对象
	private byte isEnable; // 是否启用
	private String nodeUrl; // 节点对应url
	private byte enableDataPermission;// 启用数据权限
	private String comment; // 备注
	private String whereSql;
	

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

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public int compareTo(PermItem target) {
		String targetCode = target.getCode();
		String[] targetC = targetCode.split("\\.");
		String[] sourceC = this.code.split("\\.");

		int minLenth = Math.min(sourceC.length, targetC.length);
		for (int i = 0; i < minLenth; i++) {
			if (Integer.parseInt(sourceC[i]) < Integer.parseInt(targetC[i])) {
				return -1;
			} else if (Integer.parseInt(sourceC[i]) > Integer
					.parseInt(targetC[i])) {
				return 1;
			}
		}

		if (sourceC.length < targetC.length) {
			return -1;
		} else if (sourceC.length > targetC.length) {
			return 1;
		} else {
			return 0;
		}
	}

	
}
