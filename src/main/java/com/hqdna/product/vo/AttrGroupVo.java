package com.hqdna.product.vo;

import java.util.List;
import java.util.Map;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 产品属性组
 * 
 * 
 */
public class AttrGroupVo extends AbstractBizObject {
	private static final long serialVersionUID = 8067652657564026226L;
	private int attrGroupID; // id
	private String attrGroupName; // 属性组名称
	private String attrGroupCode; // 属性组代码
	private String comment; // 备注
	//属性名：属性值列表
	private Map<AttrNameVo, List<AttrValueVo>> attrNameMap;

	public int getAttrGroupID() {
		return attrGroupID;
	}

	public void setAttrGroupID(int attrGroupID) {
		this.attrGroupID = attrGroupID;
	}

	public String getAttrGroupName() {
		return attrGroupName;
	}

	public void setAttrGroupName(String attrGroupName) {
		this.attrGroupName = attrGroupName;
	}

	public String getAttrGroupCode() {
		return attrGroupCode;
	}

	public void setAttrGroupCode(String attrGroupCode) {
		this.attrGroupCode = attrGroupCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Map<AttrNameVo, List<AttrValueVo>> getAttrNameMap() {
		return attrNameMap;
	}

	public void setAttrNameMap(Map<AttrNameVo, List<AttrValueVo>> attrNameMap) {
		this.attrNameMap = attrNameMap;
	}


}
