package com.hqdna.product.bean;

import java.io.Serializable;

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
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.product.vo.ProductDynamicAttrVo;

/**
 * 产品动态属性表
 * 
 * 
 */
@Entity
@Table(name = "TBL_PRODUCT_DYNAMICATTR")
public class ProductDynamicAttr implements ITransfer<ProductDynamicAttrVo>,Serializable {
	private static final long serialVersionUID = 7163373872284504225L;
	private int ID;	//	id
	private Product productID;	//	产品表主键
	@FieldTitle(name = "属性名", fieldType = FieldType.SELECTABLE_VALUE)
	private AttrName attrNameID;	//	产品属性名称表主键
	@FieldTitle(name = "属性值", fieldType = FieldType.SELECTABLE_VALUE)
	private AttrValue attrValueID;	//	产品属性值表主键
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "productID", referencedColumnName = "productID")
	public Product getProductID() {
		return productID;
	}
	public void setProductID(Product productID) {
		this.productID = productID;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "attrNameID",referencedColumnName="attrID")
	public AttrName getAttrNameID() {
		return attrNameID;
	}
	public void setAttrNameID(AttrName attrNameID) {
		this.attrNameID = attrNameID;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "attrValueID")
	public AttrValue getAttrValueID() {
		return attrValueID;
	}
	public void setAttrValueID(AttrValue attrValueID) {
		this.attrValueID = attrValueID;
	}
	
	public void transferPo2Vo(ProductDynamicAttrVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setID(getID());
			vo.setProductID(getProductID().getProductID());
			vo.setAttrNameID(getAttrNameID().getAttrID());
			vo.setAttrValueID(getAttrValueID().getAttrValueID());
			vo.setProductName(getProductID().getProductEnName());
			vo.setAttrName(getAttrNameID().getAttrName());
			vo.setValueName(getAttrValueID().getValueName());
		} else {
			if(vo.getID()>0){
				setID(vo.getID());
			}
			if(vo.getProductID()>0){
				Product pd = new Product();
				pd.setProductID(vo.getProductID());
				setProductID(pd);
			}
			if(vo.getAttrNameID()>0){
				AttrName an = new AttrName();
				an.setAttrID(vo.getAttrNameID());
				setAttrNameID(an);
			}
			if(vo.getAttrValueID()>0){
				AttrValue av = new AttrValue();
				av.setAttrValueID(vo.getAttrValueID());
				setAttrValueID(av);
			}
		}

	}
	
}
