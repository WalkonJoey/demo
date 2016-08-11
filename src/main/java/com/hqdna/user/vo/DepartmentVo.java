package com.hqdna.user.vo;

import java.sql.Timestamp;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 部门表
 * 
 * @author Aaron 2014-8-14
 * 
 */
public class DepartmentVo extends AbstractBizObject {

	private static final long serialVersionUID = 4196510210812149907L;
	private int departID; // ID
	private String departCode; // 部门代码
	private String departName; // 部门名称
	private String operatorID;
	private String operatorName;
	private Timestamp operateDt;
	public int getDepartID() {
		return departID;
	}
	public void setDepartID(int departID) {
		this.departID = departID;
	}
	public String getDepartCode() {
		return departCode;
	}
	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public String getOperatorID() {
		return operatorID;
	}
	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Timestamp getOperateDt() {
		return operateDt;
	}
	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
	}


}
