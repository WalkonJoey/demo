package com.hqdna.product.vo;

import java.io.Serializable;


public class ProductDynamicAttrVo implements Serializable{
	private static final long serialVersionUID = 7609654153181096700L;
	private int ID;	//	id
	private int productID;	//	产品表主键
	private String productName;
	private int attrNameID;	//	产品属性名称表主键
	private String attrName;
	private int attrValueID;	//	产品属性值表主键
	private String valueName;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getAttrNameID() {
		return attrNameID;
	}
	public void setAttrNameID(int attrNameID) {
		this.attrNameID = attrNameID;
	}
	public int getAttrValueID() {
		return attrValueID;
	}
	public void setAttrValueID(int attrValueID) {
		this.attrValueID = attrValueID;
	}

}
