package com.hqdna.product.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 产品类
 * 
 * 
 */
public class ProductVo implements Serializable{
	private static final long serialVersionUID = 8189483387530422929L;
	
	private int productID; // 产品ID
	private String skuNo; // 产品编码
	private String productModel;//
	private String twoLevelCategory;//产品类别
	private String productCategory;//产品类别
	private String productEnName; // 产品名称
	private String productCnName;//中文名称
	private String brand;//品牌
	private String picturePath; // 图片路径
	private String specParam; // 规格参数 产品参数
	private String description; // 产品描述
	private String keyword;//关键字
	private BigDecimal purchasePrice; // 采购单价  批发价（元）
	private BigDecimal salePrice;//	建议零售价（元）
	private float deliveryTime;//发货时效（小时）
	private float beforePackWeight; // 打包前重量：单位 g 净重（g）
	private float afterPackWeight; // 打包后重量：单位 g 发货重量（g）
	private float packageLength;//产品打包后长度 (cm)
	private float packageWidth;//产品打包后宽度
	private float packageHeight;//产品打包后高度
	private String packageList;//包装清单
	private String authType;//认证类型
	private int minOrderQty;// 起定量
	private String address; // 地址
	private byte billStatus;// 单据状态 主要用于产品审批流程控制
	private String comment; // 备注
	private String shelfLife;//保质期
	private int vendorID;// 供应商ID
	private String vendorCode;//供应商编码
	private String vendorName;// 供应商名称
	private String creatorID;
	private String creatorName; //
	private Timestamp createDt;// 创建时间
	private String operatorID;
	private String operatorName; //
	private Timestamp operateDt;// 操作时间
	private String attachmentPath;//附件路径
	public ProductVo() {
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}

	public int getVendorID() {
		return vendorID;
	}

	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getSpecParam() {
		return specParam;
	}

	public void setSpecParam(String specParam) {
		this.specParam = specParam;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public float getBeforePackWeight() {
		return beforePackWeight;
	}

	public void setBeforePackWeight(float beforePackWeight) {
		this.beforePackWeight = beforePackWeight;
	}

	public float getAfterPackWeight() {
		return afterPackWeight;
	}

	public void setAfterPackWeight(float afterPackWeight) {
		this.afterPackWeight = afterPackWeight;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public float getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(float deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public float getPackageLength() {
		return packageLength;
	}

	public void setPackageLength(float packageLength) {
		this.packageLength = packageLength;
	}

	public float getPackageWidth() {
		return packageWidth;
	}

	public void setPackageWidth(float packageWidth) {
		this.packageWidth = packageWidth;
	}

	public float getPackageHeight() {
		return packageHeight;
	}

	public void setPackageHeight(float packageHeight) {
		this.packageHeight = packageHeight;
	}


	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public int getMinOrderQty() {
		return minOrderQty;
	}

	public void setMinOrderQty(int minOrderQty) {
		this.minOrderQty = minOrderQty;
	}

	public byte getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(byte billStatus) {
		this.billStatus = billStatus;
	}

	public String getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8") 
	public Timestamp getOperateDt() {
		return operateDt;
	}

	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
	}

	public String getPackageList() {
		return packageList;
	}

	public void setPackageList(String packageList) {
		this.packageList = packageList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getTwoLevelCategory() {
		return twoLevelCategory;
	}

	public void setTwoLevelCategory(String twoLevelCategory) {
		this.twoLevelCategory = twoLevelCategory;
	}

	public String getProductCnName() {
		return productCnName;
	}

	public void setProductCnName(String productCnName) {
		this.productCnName = productCnName;
	}

	public String getProductEnName() {
		return productEnName;
	}

	public void setProductEnName(String productEnName) {
		this.productEnName = productEnName;
	}

	public String getShelfLife() {
		return shelfLife;
	}

	public void setShelfLife(String shelfLife) {
		this.shelfLife = shelfLife;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}


}
