package com.hqdna.product.vo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseVo.AbstractBizObject;

/**
 * 产品分类基础信息表
 * 
 * 
 */
public class CategoryVo extends AbstractBizObject {
	private static final long serialVersionUID = 4091020227657982742L;
	private int categoryID=-1;
	private int categoryParentID=0;
	private int categoryCode=0;
	private String categoryCnName=null;
	private String categoryEnName = null;//在前台页面展示的时候通过这个属性来辨别是否是二级节点
	private String comment=null;
	private String creatorID=null;
	private String creatorName=null;
	private Timestamp createDt=null;
	private byte level;//分类等级，一级还是二级
	private String declareName;//因为申报名称是通过分类名称得来的，所以在这里批量管理
	private Map<AttrNameVo, List<AttrValueVo>> attrMap;
	
	private List<Integer> attributesIds;
	
	private String ncCode;//分类在NC的编码

	public List<Integer> getAttributesIds() {
		return attributesIds;
	}

	public void setAttributesIds(List<Integer> attributesIds) {
		this.attributesIds = attributesIds;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public int getCategoryParentID() {
		return categoryParentID;
	}

	public void setCategoryParentID(int categoryParentID) {
		this.categoryParentID = categoryParentID;
	}

	public String getCategoryCnName() {
		return categoryCnName;
	}

	public void setCategoryCnName(String categoryCnName) {
		this.categoryCnName = categoryCnName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public Map<AttrNameVo, List<AttrValueVo>> getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(Map<AttrNameVo, List<AttrValueVo>> attrMap) {
		this.attrMap = attrMap;
	}

	public String getCategoryENName() {
		return categoryEnName;
	}

	public void setCategoryEnName(String categoryENName) {
		this.categoryEnName = categoryENName;
	}

	public String getDeclareName() {
		return declareName;
	}

	public void setDeclareName(String declareName) {
		this.declareName = declareName;
	}

	public byte getLevel() {
		return level;
	}

	public void setLevel(byte level) {
		this.level = level;
	}

	public String getNcCode() {
		return ncCode;
	}

	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}


}
