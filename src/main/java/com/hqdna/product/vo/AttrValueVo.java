package com.hqdna.product.vo;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 产品属性值
 * 
 * 
 * @author Aaron 2014-8-11
 * 
 */
public class AttrValueVo extends AbstractBizObject{
	private static final long serialVersionUID = -12560327827025771L;
	private int attrValueID; // id
	private int attrID;
	private String valueName; // 属性值名称
	private String valueCode; // 属性值代码
	private String comment; // 备注

	public int getAttrValueID() {
		return attrValueID;
	}

	public void setAttrValueID(int attrValueID) {
		this.attrValueID = attrValueID;
	}

	public int getAttrID() {
		return attrID;
	}

	public void setAttrID(int attrID) {
		this.attrID = attrID;
	}

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
