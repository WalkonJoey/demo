package com.hqdna.vendor.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hqdna.common.baseVo.AbstractBizObject;


/**
 * 供应商类
 * 
 *
 */
public class VendorVo extends AbstractBizObject {
	private static final long serialVersionUID = 1L;
	
	private int vendorID; // id
	private String vendorCode; // 供应商编码
	private String vendorName; // 供应商名称
	private String postCode; // 邮编
	private String address; // 地址
	private String documentaryID;	//默认跟单
	private String documentaryName;	//默认跟单
	private String pEngineerID;	//采购工程师ID
	private String pEngineerName;	//采购工程师名字
	private byte isEnable; // 是否可用
	private String comment; // 备注
	
	private String creatorID;	//创建人员ID
	private String creatorName; // 创建人员
	private Timestamp createDt; // 创建时间
	//新增的属性
	private byte payPeriod; // 结算周期
	private byte payAppoint;// 付款约定
	private byte invoiceType; // 票据类型  1普通发票2增值税专用发票3增值税普通发票
	private byte payType; // 结算方式   1银行转账 2现金
	
	//customer-service manager  csm  客服经理
	private String csmName; // 联系人姓名
	private String csmTelephone; // 固定电话
	private String csmMobilePhone; // 移动电话
	private String csmEmail; // 邮箱
	private String csmFax; // 传真
	private String csmQQ;//QQ号码
	//Clerk  业务文员
	private String clerkName; // 联系人姓名
	private String clerkTelephone; // 固定电话
	private String clerkMobilePhone; // 移动电话
	private String clerkEmail; // 邮箱
	private String clerkFax; // 传真
	private String clerkQQ;//QQ号码
	
	private int payTaxPoint; // 单价所含支付税点            有税点就不会有下面的sku税额
	private BigDecimal purchaseTax;//采购sku税额             采购供应商有的不是按照税点来计算税的，而是没个sku加上一个税费如0.3来计算税的    有sku税额就不会有税点
	private float billRate;//开票税率  13%或者17%等  用于传入NC，采购价格 通过单价所含税点得到税后价格，税后价格/(1+13%)得到传入NC的开票税前单价
	
	private String cooperationWay;//合作方式
	private String getGoodsWay;//拿货方式
	
	public int getVendorID() {
		return vendorID;
	}
	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public byte getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(byte payPeriod) {
		this.payPeriod = payPeriod;
	}
	public byte getPayAppoint() {
		return payAppoint;
	}
	public void setPayAppoint(byte payAppoint) {
		this.payAppoint = payAppoint;
	}
	public byte getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(byte invoiceType) {
		this.invoiceType = invoiceType;
	}
	public byte getPayType() {
		return payType;
	}
	public void setPayType(byte payType) {
		this.payType = payType;
	}
	public String getCsmName() {
		return csmName;
	}
	public void setCsmName(String csmName) {
		this.csmName = csmName;
	}
	public String getCsmTelephone() {
		return csmTelephone;
	}
	public void setCsmTelephone(String csmTelephone) {
		this.csmTelephone = csmTelephone;
	}
	public String getCsmMobilePhone() {
		return csmMobilePhone;
	}
	public void setCsmMobilePhone(String csmMobilePhone) {
		this.csmMobilePhone = csmMobilePhone;
	}
	public String getCsmEmail() {
		return csmEmail;
	}
	public void setCsmEmail(String csmEmail) {
		this.csmEmail = csmEmail;
	}
	public String getCsmFax() {
		return csmFax;
	}
	public void setCsmFax(String csmFax) {
		this.csmFax = csmFax;
	}
	public String getCsmQQ() {
		return csmQQ;
	}
	public void setCsmQQ(String csmQQ) {
		this.csmQQ = csmQQ;
	}
	public String getClerkName() {
		return clerkName;
	}
	public void setClerkName(String clerkName) {
		this.clerkName = clerkName;
	}
	public String getClerkTelephone() {
		return clerkTelephone;
	}
	public void setClerkTelephone(String clerkTelephone) {
		this.clerkTelephone = clerkTelephone;
	}
	public String getClerkMobilePhone() {
		return clerkMobilePhone;
	}
	public void setClerkMobilePhone(String clerkMobilePhone) {
		this.clerkMobilePhone = clerkMobilePhone;
	}
	public String getClerkEmail() {
		return clerkEmail;
	}
	public void setClerkEmail(String clerkEmail) {
		this.clerkEmail = clerkEmail;
	}
	public String getClerkFax() {
		return clerkFax;
	}
	public void setClerkFax(String clerkFax) {
		this.clerkFax = clerkFax;
	}
	public String getClerkQQ() {
		return clerkQQ;
	}
	public void setClerkQQ(String clerkQQ) {
		this.clerkQQ = clerkQQ;
	}
	public String getDocumentaryID() {
		return documentaryID;
	}
	public void setDocumentaryID(String documentaryID) {
		this.documentaryID = documentaryID;
	}
	public String getDocumentaryName() {
		return documentaryName;
	}
	public void setDocumentaryName(String documentaryName) {
		this.documentaryName = documentaryName;
	}
	public byte getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(byte isEnable) {
		this.isEnable = isEnable;
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
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	public String getpEngineerID() {
		return pEngineerID;
	}
	public void setpEngineerID(String pEngineerID) {
		this.pEngineerID = pEngineerID;
	}
	public String getpEngineerName() {
		return pEngineerName;
	}
	public void setpEngineerName(String pEngineerName) {
		this.pEngineerName = pEngineerName;
	}
	public int getPayTaxPoint() {
		return payTaxPoint;
	}
	public void setPayTaxPoint(int payTaxPoint) {
		this.payTaxPoint = payTaxPoint;
	}
	public BigDecimal getPurchaseTax() {
		return purchaseTax;
	}
	public void setPurchaseTax(BigDecimal purchaseTax) {
		this.purchaseTax = purchaseTax;
	}
	public float getBillRate() {
		return billRate;
	}
	public void setBillRate(float billRate) {
		this.billRate = billRate;
	}
	public String getCooperationWay() {
		return cooperationWay;
	}
	public void setCooperationWay(String cooperationWay) {
		this.cooperationWay = cooperationWay;
	}
	public String getGetGoodsWay() {
		return getGoodsWay;
	}
	public void setGetGoodsWay(String getGoodsWay) {
		this.getGoodsWay = getGoodsWay;
	}
}
