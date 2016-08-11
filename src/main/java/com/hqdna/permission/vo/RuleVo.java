package com.hqdna.permission.vo;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 用户自定义规则
 * 
 * 
 */
public final class RuleVo extends AbstractBizObject implements Comparable<RuleVo> {
	private static final long serialVersionUID = 5593053010770950459L;
	private long ruleID; // 规则ID
	private int innerNo; // 内部序号
	private String leftParentheses = null; // 左侧括号
	private String propertyTitle; // 选择的属性全路径名，如:最后操作人员.部门.最后操作人员.登录名
	private String propertyName; // 选择的属性字段全路径名，如:operatorUser.department.operatorUser.loginID
	private String midOperator; // 运算符 > = <
	private String propertyValue; // 属性值
	private String rightParenthese = null; // 右侧括号
	private String logical = null; // 逻辑关系符 and &&

	private String whereSql = null; // rule转换后的SQL
	private EntityProperty ep = null; // 该rule关联的实体属性对象

	public long getRuleID() {
		return ruleID;
	}

	public void setRuleID(long ruleID) {
		this.ruleID = ruleID;
	}

	public int getInnerNo() {
		return innerNo;
	}

	public void setInnerNo(int innerNo) {
		this.innerNo = innerNo;
	}

	public String getLeftParentheses() {
		return leftParentheses;
	}

	public void setLeftParentheses(String leftParentheses) {
		this.leftParentheses = leftParentheses;
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

	public String getRightParenthese() {
		return rightParenthese;
	}

	public void setRightParenthese(String rightParenthese) {
		this.rightParenthese = rightParenthese;
	}

	public String getLogical() {
		return logical;
	}

	public void setLogical(String logical) {
		this.logical = logical;
	}

	public int compareTo(RuleVo target) {
		return this.innerNo - target.getInnerNo();
	}

	public void setEp(EntityProperty ep) {
		this.ep = ep;
		setWhereSql(transfer2WhereSql());
	}

	public String getWhereSql() {
		return whereSql;
	}

	private void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	/**
	 * 转换为whereSQL语句
	 * 
	 * @return
	 */
	private String transfer2WhereSql() {
		if (ep == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if (this.leftParentheses != null) {
			sb.append(this.leftParentheses);
		}
		if (this.propertyName != null) {
			sb.append(this.propertyName);
		}
		if (this.midOperator != null) {
			sb.append(" ").append(this.midOperator).append(" ");
		}
		if (this.propertyValue != null) {
			if (ep.getPropType() == FieldType.CUSTOM_STRING) {
				sb.append(" '" + this.propertyValue + "' ");
			} else if (ep.getPropType() == FieldType.SELECTABLE_VALUE) {
				sb.append(" '" + this.propertyValue + "' ");
			} else if (ep.getPropType() == FieldType.CUSTOM_DATETIME) {
				sb.append(" '" + this.propertyValue + "' ");
			} else {
				sb.append(this.propertyValue);
			}
		}
		if (this.rightParenthese != null) {
			sb.append(this.rightParenthese);
		}
		if (this.logical != null) {
			sb.append(" ").append(this.logical).append(" ");
		}
		return sb.toString();
	}
}
