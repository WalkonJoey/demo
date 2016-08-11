package com.hqdna.product.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.product.vo.AttrGroupVo;

/**
 * 产品属性组
 * 
 * 
 * @author Aaron 2014-8-11
 * 
 */
@Entity
@Table(name = "TBL_ATTRGROUP")
public class AttrGroup implements ITransfer<AttrGroupVo>, Serializable {
	private static final long serialVersionUID = -3603539855390833967L;

	private int attrGroupID; // id
	private String attrGroupName; // 属性组名称
	private String attrGroupCode; // 属性组代码
	private String comment; // 备注

	private Set<Category> categorys;

	private List<AttrName> attrNames;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "attrGroups")
	public Set<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(Set<Category> categorys) {
		this.categorys = categorys;
	}

	@ManyToMany(cascade = { CascadeType.REFRESH })
	@JoinTable(name = "TBL_ATTRGROUP_ATTRNAME", joinColumns = @JoinColumn(name = "attrGroupID"), inverseJoinColumns = @JoinColumn(name = "attrID"))
	public List<AttrName> getAttrNames() {
		return attrNames;
	}

	public void setAttrNames(List<AttrName> attrNames) {
		this.attrNames = attrNames;
	}

	public void transferPo2Vo(AttrGroupVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setAttrGroupCode(getAttrGroupCode());
			vo.setAttrGroupID(getAttrGroupID());
			vo.setAttrGroupName(getAttrGroupName());
			vo.setComment(getComment());
		} else {
			setAttrGroupCode(vo.getAttrGroupCode());
			if(vo.getAttrGroupID()>0){
				setAttrGroupID(vo.getAttrGroupID());
			}
			setAttrGroupName(vo.getAttrGroupName());
			setComment(vo.getComment());
		}
	}

}
