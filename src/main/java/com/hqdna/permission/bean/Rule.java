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

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.bean.User;


/**
 * 权限规则，用来设置字段级别的权限
 * 
 */
@Entity
@Table(name = "TBL_RULE")
public class Rule implements ITransfer<RuleVo>, Serializable {
	private static final long serialVersionUID = 2643781126490378208L;
	private long ruleID; // ID
	private RolePerm rolePerm;
	private int innerNo; // 内部序号
	private String leftPar;// 左侧括号
	private String propertyTitle; // 属性中文名称
	private String propertyName; // 属性英文字段名
	private String midOperator; // 中间操作符
	private String propertyValue; // 属性值
	private String rightPar; // 右侧括号
	private String logical; // 逻辑符
	private User operatorUser; // 操作人员
	private Timestamp operateDt; // 操作时间

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getRuleID() {
		return ruleID;
	}

	public void setRuleID(long ruleID) {
		this.ruleID = ruleID;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "rolePermID", referencedColumnName = "ID")
	public RolePerm getRolePerm() {
		return rolePerm;
	}

	public void setRolePerm(RolePerm rolePerm) {
		this.rolePerm = rolePerm;
	}

	public int getInnerNo() {
		return innerNo;
	}

	public void setInnerNo(int innerNo) {
		this.innerNo = innerNo;
	}

	public String getLeftPar() {
		return leftPar;
	}

	public void setLeftPar(String leftPar) {
		this.leftPar = leftPar;
	}

	public String getPropertyTitle() {
		return propertyTitle;
	}

	public void setPropertyTitle(String propertyTitle) {
		this.propertyTitle = propertyTitle;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getMidOperator() {
		return midOperator;
	}

	public void setMidOperator(String midOperator) {
		this.midOperator = midOperator;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getRightPar() {
		return rightPar;
	}

	public void setRightPar(String rightPar) {
		this.rightPar = rightPar;
	}

	public String getLogical() {
		return logical;
	}

	public void setLogical(String logical) {
		this.logical = logical;
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

	public void transferPo2Vo(RuleVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setRuleID(getRuleID());
			vo.setInnerNo(getInnerNo());
			vo.setLeftParentheses(getLeftPar());
			vo.setPropertyTitle(getPropertyTitle());
			vo.setPropertyName(getPropertyName());
			vo.setPropertyValue(getPropertyValue());
			vo.setMidOperator(getMidOperator());
			vo.setRightParenthese(getRightPar());
			vo.setLogical(getLogical());
			vo.setOperatorName(getOperatorUser().getUserCnName());
			vo.setOperateDt(getOperateDt());
		} else {
			setRuleID(vo.getRuleID());
			setInnerNo(vo.getInnerNo());
			setLeftPar(vo.getLeftParentheses());
			setPropertyTitle(vo.getPropertyTitle());
			setPropertyName(vo.getPropertyName());
			setPropertyValue(vo.getPropertyValue());
			setMidOperator(vo.getMidOperator());
			setRightPar(vo.getRightParenthese());
			setLogical(vo.getLogical());
			setOperateDt(vo.getOperateDt());
		}

	}

}
