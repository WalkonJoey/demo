package com.hqdna.user.vo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 用户信息类
 * 
 */

public class UserInfo extends AbstractBizObject implements Comparable<UserInfo> {
	private static final long serialVersionUID = -1976274589787987260L;
	private String userID;
	private String loginID;
	private String userCnName;
	private String userEnName;
	private byte gender;
	private int age;
	private String idCard;
	private String address;
	private String homeAddr;
	private String telephone;
	private String email;
	private String password;
	private String lastPassw;
	private Timestamp loginTime;
	private int departmentID;
	private String departmentName;// 部门名
	private String departJob;// 职务
	private byte isEnable;
	private byte type;
	private String ipAddress;
	private String creatorID;
	private String creatorName;
	private Timestamp createDt;

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

	public byte getType() {
		return type;
	}

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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

	public void setType(byte type) {
		this.type = type;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getCreatorID() {
		return creatorID;
	}

	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	/**
	 * 是否为系统管理员
	 * 
	 * @return
	 */
	public boolean isAdministrator() {
		return type == 127 ? true : false;
	}

	/**
	 * 是否为业务管理员
	 * 
	 * @return
	 */
	public boolean isBizAdministrator() {
		return type == 100 ? true : false;
	}

	
	public int compareTo(UserInfo o) {
		return this.getLoginID().compareTo(o.getLoginID());
	}
}
