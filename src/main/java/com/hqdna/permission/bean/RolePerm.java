package com.hqdna.permission.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hqdna.user.bean.Role;
import com.hqdna.user.bean.User;


@Entity
@Table(name = "TBL_ROLE_PERM")
public class RolePerm implements Serializable {
	private static final long serialVersionUID = 9103913049740572766L;
	private long ID; // ID
	private Role role; // 角色ID
	private Permission perm; // 权限ID
	private String whereSQL; // 行级权限SQL
	private User operatorUser; // 操作人员
	private Timestamp operateDt; // 操作时间

	private List<Rule> rules; // 规则

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
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
	@JoinColumn(name = "permID")
	public Permission getPerm() {
		return perm;
	}

	public void setPerm(Permission perm) {
		this.perm = perm;
	}

	public String getWhereSQL() {
		return whereSQL;
	}

	public void setWhereSQL(String whereSQL) {
		this.whereSQL = whereSQL;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "rolePermID", referencedColumnName = "ID")
	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

}
