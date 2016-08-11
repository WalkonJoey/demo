package com.hqdna.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.user.vo.DepartmentVo;

/**
 * 部门表
 * 
 */
@Entity
@Table(name = "TBL_DEPARTMENT")
public class Department implements ITransfer<DepartmentVo>, Serializable {
	private static final long serialVersionUID = 5658619817068178041L;

	private int departID; // ID

	@FieldTitle(name = "部门代码", fieldType = FieldType.SELECTABLE_VALUE)
	private String departCode; // 部门代码
	@FieldTitle(name = "部门名称", fieldType = FieldType.CUSTOM_STRING)
	private String departName; // 部门名称

	@FieldTitle(name = "最后操作人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User operatorUser;
	// 操作时间
	@FieldTitle(name = "最后操作时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp operateDt;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	// 单向关联操作用户
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

	
	public void transferPo2Vo(DepartmentVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setDepartID(getDepartID());
			vo.setDepartCode(getDepartCode());
			vo.setDepartName(getDepartName());
			vo.setOperateDt(getOperateDt());
			vo.setOperatorID(getOperatorUser().getUserID());
			vo.setOperatorName(getOperatorUser().getUserCnName());
		} else {
			setDepartID(vo.getDepartID());
			setDepartCode(vo.getDepartCode());
			setDepartName(vo.getDepartName());
			setOperateDt(vo.getOperateDt());
		}

	}

}
