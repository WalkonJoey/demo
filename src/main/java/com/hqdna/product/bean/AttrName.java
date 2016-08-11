package com.hqdna.product.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.product.vo.AttrNameVo;

/**
 * 产品属性
 * 
 * 
 */
@Entity
@Table(name = "TBL_ATTRNAME")
public class AttrName implements ITransfer<AttrNameVo>, Serializable {
	private static final long serialVersionUID = -8806541417134998097L;
	private int attrID; // id
	@FieldTitle(name = "属性名", fieldType = FieldType.SELECTABLE_VALUE)
	private String attrName; // 属性名称
	@FieldTitle(name = "属性编码", fieldType = FieldType.SELECTABLE_VALUE)
	private String attrCode; // 属性代码
	private String comment; // 备注

	private List<AttrGroup> attrGroups;

	private List<AttrValue> attrValues;

	private List<ProductDynamicAttr> dynamicAttrs;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@ManyToMany(cascade = { CascadeType.REFRESH })
	@JoinTable(name = "TBL_ATTRGROUP_ATTRNAME", joinColumns = @JoinColumn(name = "attrID"), inverseJoinColumns = @JoinColumn(name = "attrGroupID"))
	public List<AttrGroup> getAttrGroups() {
		return attrGroups;
	}

	public void setAttrGroups(List<AttrGroup> attrGroups) {
		this.attrGroups = attrGroups;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "attrID", referencedColumnName = "attrID")
	public List<AttrValue> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<AttrValue> attrValues) {
		this.attrValues = attrValues;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "ID",referencedColumnName="attrID")
	public List<ProductDynamicAttr> getDynamicAttrs() {
		return dynamicAttrs;
	}

	public void setDynamicAttrs(List<ProductDynamicAttr> dynamicAttrs) {
		this.dynamicAttrs = dynamicAttrs;
	}

	/**
	 * PO对象VO对象相互转换
	 * 
	 * @param _a
	 * @param _b
	 * @param direction
	 *            true:_a->_b;false:_b->_a
	 */
	public void transferPo2Vo(AttrNameVo vo, boolean direction) {

		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setAttrID(getAttrID());
			vo.setAttrCode(getAttrCode());
			vo.setAttrName(getAttrName());
			vo.setComment(getComment());
		} else {
			if(vo.getAttrID()>0){//这个判断很重要，不然在新增的时候设置了值就会报错
				setAttrID(vo.getAttrID());
			}
			setAttrCode(vo.getAttrCode());
			setAttrName(vo.getAttrName());
			setComment(vo.getComment());
		}
	}

}
