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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.product.vo.AttrValueVo;

/**
 * 产品属性值
 * 
 * 
 * @author Aaron 2014-8-11
 * 
 */
@Entity
@Table(name = "TBL_ATTRVALUE")
public class AttrValue implements ITransfer<AttrValueVo>, Serializable {
	private static final long serialVersionUID = -8780035396279108548L;
	private int attrValueID; // id
	private AttrName attrName; // 属性名称表主键
	@FieldTitle(name = "属性值", fieldType = FieldType.SELECTABLE_VALUE)
	private String valueName; // 属性值名称
	@FieldTitle(name = "属性值编码", fieldType = FieldType.SELECTABLE_VALUE)
	private String valueCode; // 属性值代码
	private String comment; // 备注

	private List<ProductDynamicAttr> dynamicAttrs;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getAttrValueID() {
		return attrValueID;
	}

	public void setAttrValueID(int attrValueID) {
		this.attrValueID = attrValueID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attrID", referencedColumnName = "attrID")
	public AttrName getAttrName() {
		return attrName;
	}

	public void setAttrName(AttrName attrName) {
		this.attrName = attrName;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "attrValueID")
	public List<ProductDynamicAttr> getDynamicAttrs() {
		return dynamicAttrs;
	}

	public void setDynamicAttrs(List<ProductDynamicAttr> dynamicAttrs) {
		this.dynamicAttrs = dynamicAttrs;
	}

	public void transferPo2Vo(AttrValueVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setAttrValueID(getAttrValueID());
			vo.setValueCode(getValueCode());
			vo.setValueName(getValueName());
			vo.setComment(getComment());
		} else {
			if(vo.getAttrValueID()>0){
				setAttrValueID(vo.getAttrValueID());
			}
			setValueCode(vo.getValueCode());
			setValueName(vo.getValueName());
			setComment(vo.getComment());
		}

	}

}
