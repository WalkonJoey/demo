package com.hqdna.permission.bean;

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

import com.hqdna.user.bean.Role;
import com.hqdna.user.bean.User;

@Entity
@Table(name = "TBL_USER_ROLE")
public class UserRole implements Serializable {
	private static final long serialVersionUID = -3620392943846028142L;
	private long ID; // ID
	private User user; // 用户ID
	private Role role; // 权限ID
	private User operatorUser; // 操作人员
	private Timestamp operateDt; // 操作时间

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "userID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "roleID")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

}
