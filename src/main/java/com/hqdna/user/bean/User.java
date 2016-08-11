package com.hqdna.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.user.vo.UserInfo;

/**
 * 用户实体类
 * 
 */
@Entity
@Table(name = "TBL_USER")
public class User implements ITransfer<UserInfo>, Serializable {
	private static final long serialVersionUID = -6090893971016144296L;

	private String userID;
	@FieldTitle(name = "登录名", fieldType = FieldType.SELECTABLE_VALUE)
	private String loginID;
	@FieldTitle(name = "用户名", fieldType = FieldType.SELECTABLE_VALUE)
	private String userCnName;
	@FieldTitle(name = "英文名", fieldType = FieldType.SELECTABLE_VALUE)
	private String userEnName;
	@FieldTitle(name = "性别", fieldType = FieldType.SELECTABLE_VALUE, values = {
			"男", "女" })
	private byte gender;
	@FieldTitle(name = "年龄", fieldType = FieldType.CUSTOM_NUMBER)
	private int age;
	@FieldTitle(name = "身份证", fieldType = FieldType.CUSTOM_STRING)
	private String idCard;
	private String address;
	private String homeAddr;
	@FieldTitle(name = "手机号", fieldType = FieldType.CUSTOM_NUMBER)
	private String telephone;
	@FieldTitle(name = "邮箱", fieldType = FieldType.CUSTOM_STRING)
	private String email;
	private String password;
	private String lastPassw;
	private Timestamp loginTime;
	@FieldTitle(name = "部门", fieldType = FieldType.CUSTOM_STRING)
	private Department department;
	@FieldTitle(name = "职位", fieldType = FieldType.CUSTOM_STRING)
	private String departJob;
	@FieldTitle(name = "是否启用", fieldType = FieldType.SELECTABLE_VALUE, values = {
			"是", "否" })
	private byte isEnable;
	private byte type;

	@FieldTitle(name = "创建人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User creatorUser;
	// 创建时间
	@FieldTitle(name = "创建时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp createDt;

	@FieldTitle(name = "最后操作人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User operatorUser;
	// 操作时间
	@FieldTitle(name = "最后操作时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp operateDt;

	@Id
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getLoginID() {
		return loginID;
	}

	public void setLoginID(String loginID) {
		this.loginID = loginID;
	}

	public String getUserCnName() {
		return userCnName;
	}

	public void setUserCnName(String userCnName) {
		this.userCnName = userCnName;
	}

	public String getUserEnName() {
		return userEnName;
	}

	public void setUserEnName(String userEnName) {
		this.userEnName = userEnName;
	}

	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Column(updatable = false)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHomeAddr() {
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "passw")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastPassw() {
		return lastPassw;
	}

	public void setLastPassw(String lastPassw) {
		this.lastPassw = lastPassw;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "departID")
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDepartJob() {
		return departJob;
	}

	public void setDepartJob(String departJob) {
		this.departJob = departJob;
	}

	public byte getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(byte isEnable) {
		this.isEnable = isEnable;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "creator", updatable = false)
	public User getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(User creatorUser) {
		this.creatorUser = creatorUser;
	}

	@Column(updatable = false)
	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	// 单向关联操作用户
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
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

	
	public void transferPo2Vo(UserInfo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setUserID(getUserID());
			vo.setUserCnName(getUserCnName());
			vo.setUserEnName(getUserEnName());
			vo.setAddress(getAddress());
			vo.setAge(getAge());
			vo.setDepartJob(getDepartJob());
			vo.setTelephone(getTelephone());
			vo.setEmail(getEmail());
			vo.setGender(getGender());
			vo.setHomeAddr(getHomeAddr());
			vo.setIdCard(getIdCard());
			vo.setIsEnable(getIsEnable());
			vo.setLastPassw(getLastPassw());
			vo.setLoginID(getLoginID());
			vo.setLoginTime(getLoginTime());
			if (getOperatorUser() != null) {
				vo.setOperateDt(getOperateDt());
				vo.setOperatorID(getOperatorUser().getUserID());
				vo.setOperatorName(getOperatorUser().getUserCnName());
			}
			vo.setDepartmentID(getDepartment().getDepartID());
			vo.setDepartmentName(getDepartment().getDepartName());
			vo.setType(getType());
			vo.setCreatorID(getCreatorUser().getUserID());
			vo.setCreatorName(getCreatorUser().getUserCnName());
			vo.setCreateDt(getCreateDt());
			vo.setPassword(getPassword());
		} else {
			setUserID(vo.getUserID());
			if (vo.getUserCnName() != null) {
				setUserCnName(vo.getUserCnName());
			}
			if (vo.getUserEnName() != null) {
			}
			setUserEnName(vo.getUserEnName());
			if (vo.getAddress() != null) {
				setAddress(vo.getAddress());
			}
			if (vo.getAge() > 0) {
				setAge(vo.getAge());
			}
			if (vo.getDepartJob() != null) {

				setDepartJob(vo.getDepartJob());
			}
			if (vo.getEmail() != null) {

				setEmail(vo.getEmail());
			}
			if (vo.getGender() >= 0) {

				setGender(vo.getGender());
			}
			if (vo.getHomeAddr() != null) {

				setHomeAddr(vo.getHomeAddr());
			}
			if (vo.getTelephone() != null) {
				setTelephone(vo.getTelephone());
			}
			if (vo.getIdCard() != null) {

				setIdCard(vo.getIdCard());
			}
			if (vo.getIsEnable() >= 0) {

				setIsEnable(vo.getIsEnable());
			}
			if (vo.getLastPassw() != null) {

				setLastPassw(vo.getLastPassw());
			}
			if (vo.getLoginID() != null) {

				setLoginID(vo.getLoginID());
			}
			if (vo.getLoginTime() != null) {

				setLoginTime(vo.getLoginTime());
			}
			if (vo.getOperateDt() != null) {

				setOperateDt(vo.getOperateDt());
			}
			if (vo.getType() >= 0) {

				setType(vo.getType());
			}
			if (vo.getCreateDt() != null) {

				setCreateDt(vo.getCreateDt());
			}
			if (vo.getPassword() != null) {

				setPassword(vo.getPassword());
			}
		}

	}

}
