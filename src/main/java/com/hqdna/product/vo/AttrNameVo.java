package com.hqdna.product.vo;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 产品属性
 * 
 * 
 */
public class AttrNameVo extends AbstractBizObject{
	private static final long serialVersionUID = -7344482203243853486L;
	private int attrID; // id
	private String attrName; // 属性名称
	private String attrCode; // 属性代码
	private String comment; // 备注

	public int getAttrID() {
		return attrID;
	}

	public void setAttrID(int attrID) {
		this.attrID = attrID;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrCode() {
		return attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
