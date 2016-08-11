package com.hqdna.product.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.bean.User;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TBL_PRODUCT")
public class Product implements ITransfer<ProductVo>, Serializable {
	private static final long serialVersionUID = 6224586044589109305L;
	/*产品图片	SKU	产品型号	供应商代码	产品一级目录 	产品二级目录	英文名称	中文名称	关键词  	品牌	质保期	     认证类型	产品参数	产品描述	
	 * 包装清单	批发价（元）	建议零售价（元）	净重（g）	发货重量（g）	包装长（cm）	包装宽（cm）	包装高（cm）	发货时效（小时）	
	 * 起定量	发货地	   	供货单位
*/

	private int productID; // 产品ID
	private String skuNo; // 产品编码
	private String productCategory;//产品类别
	private String productEnName; // 产品英文名称
	private String productCnName;//中文名称
	private String keyword;//关键字
	private String vendorCode;//
	private String productModel;//
	private String twoLevelCategory;//产品类别
	private String brand;//品牌
	private String shelfLife;//保质期
	private String authType;//认证类型
	private String picturePath; // 图片路径
	private String specParam; // 规格参数 产品参数
	private String description; // 产品描述
	private String packageList;//包装清单
	private BigDecimal purchasePrice; // 采购单价  批发价（元）
	private BigDecimal salePrice;//	建议零售价（元）
	private float beforePackWeight; // 打包前重量：单位 g 净重（g）
	private float afterPackWeight; // 打包后重量：单位 g 发货重量（g）
	private float packageLength;//产品打包后长度 (cm)
	private float packageWidth;//产品打包后宽度
	private float packageHeight;//产品打包后高度
	private float deliveryTime;//发货时效（小时）
	private int minOrderQty;// 起定量
	private String vendorName; // 供应商名称
	private String address; // 地址
	private String attachmentPath;//附件路径
	/*@FieldTitle(name = "供应商")
	private Vendor vendor; // 供应商
*/	
	private byte billStatus;// 单据状态 主要用于产品审批流程控制
	private User creatorUser;// 创建用户
	private Timestamp createDt;// 创建时间
	private User operatorUser;// 操作用户
	private Timestamp operateDt;// 操作时间
	private String comment; // 备注

	@Id
	@GeneratedValue
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


	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendorID", referencedColumnName = "vendorID")
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
*/
	public String getProductEnName() {
		return productEnName;
	}

	public void setProductEnName(String productEnName) {
		this.productEnName = productEnName;
	}

	public String getSpecParam() {
		return specParam;
	}

	public void setSpecParam(String specParam) {
		this.specParam = specParam;
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "operator")
	public User getOperatorUser() {
		return operatorUser;
	}

	public void setOperatorUser(User operatorUser) {
		this.operatorUser = operatorUser;
	}

	public Timestamp getOperateDt() {
		return operateDt;
	}

	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "creator")
	public User getCreatorUser() {
		return creatorUser;
	}

	public void setCreatorUser(User creatorUser) {
		this.creatorUser = creatorUser;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
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

	public byte getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(byte billStatus) {
		this.billStatus = billStatus;
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

	public String getPackageList() {
		return packageList;
	}

	public void setPackageList(String packageList) {
		this.packageList = packageList;
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

	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
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

	public void transferPo2Vo(ProductVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setProductID(getProductID());
			vo.setSkuNo(getSkuNo());
			vo.setAddress(getAddress());
			vo.setAfterPackWeight(getAfterPackWeight());
			vo.setAuthType(getAuthType());
			vo.setBeforePackWeight(getBeforePackWeight());
			vo.setBillStatus(getBillStatus());
			vo.setBrand(getBrand());
			vo.setDeliveryTime(getDeliveryTime());
			vo.setKeyword(getKeyword());
			vo.setMinOrderQty(getMinOrderQty());
			vo.setPackageHeight(getPackageHeight());
			vo.setPackageLength(getPackageLength());
			vo.setPackageList(getPackageList());
			vo.setPackageWidth(getPackageWidth());
			vo.setProductCategory(getProductCategory());
			vo.setSalePrice(getSalePrice());
			vo.setVendorName(getVendorName());
			vo.setOperateDt(getOperateDt());
			vo.setPicturePath(getPicturePath());
			vo.setPurchasePrice(getPurchasePrice());
			vo.setCreateDt(getCreateDt());
			vo.setDescription(getDescription());
			vo.setComment(getComment());
			vo.setSpecParam(getSpecParam());
			vo.setVendorCode(getVendorCode());
			vo.setProductModel(getProductModel());
			vo.setTwoLevelCategory(getTwoLevelCategory());
			vo.setProductEnName(getProductEnName());
			vo.setProductCnName(getProductCnName());
			vo.setShelfLife(getShelfLife());
			vo.setAttachmentPath(getAttachmentPath());
			if(getOperatorUser()!=null){
				vo.setOperatorID(getOperatorUser().getUserID());
				vo.setOperatorName(getOperatorUser().getUserCnName());
			}
			if(getCreatorUser()!=null){
				vo.setCreatorID(getCreatorUser().getUserID());
				vo.setCreatorName(getCreatorUser().getUserCnName());
			}
		} else {
			if (vo.getProductID() > 0) {
				setProductID(vo.getProductID());
			}
			if (vo.getCreatorID() != null) {
				User creater = new User();
				creater.setUserID(vo.getCreatorID());
				setCreatorUser(creater);
			}
			if (vo.getSkuNo() != null && !vo.getSkuNo().isEmpty()) {
				setSkuNo(vo.getSkuNo());
			}
			if (vo.getProductEnName() != null && !vo.getProductEnName().isEmpty()) {
				setProductEnName(vo.getProductEnName());
			}
			setProductCnName(vo.getProductCnName());
			if (vo.getPicturePath() != null && !vo.getPicturePath().isEmpty()) {
				setPicturePath(vo.getPicturePath());
			}
			if (vo.getPurchasePrice() != null
					&& vo.getPurchasePrice().intValue() > -1) {
				setPurchasePrice(vo.getPurchasePrice());
			}
			if (vo.getCreateDt() != null) {
				setCreateDt(vo.getCreateDt());
			}
			if (vo.getPicturePath() != null && !vo.getPicturePath().isEmpty()) {
				setPicturePath(vo.getPicturePath());
			}
			setBillStatus(vo.getBillStatus());
			setOperateDt(vo.getOperateDt());
			setDescription(vo.getDescription());
			setComment(vo.getComment());
			setSpecParam(vo.getSpecParam());
			setAddress(vo.getAddress());
			setAfterPackWeight(vo.getAfterPackWeight());
			setAuthType(vo.getAuthType());
			setBeforePackWeight(vo.getBeforePackWeight());
			setBillStatus(vo.getBillStatus());
			setBrand(vo.getBrand());
			setDeliveryTime(vo.getDeliveryTime());
			setKeyword(vo.getKeyword());
			setMinOrderQty(vo.getMinOrderQty());
			setPackageHeight(vo.getPackageHeight());
			setPackageLength(vo.getPackageLength());
			setPackageList(vo.getPackageList());
			setPackageWidth(vo.getPackageWidth());
			setProductCategory(vo.getProductCategory());
			setSalePrice(vo.getSalePrice());
			setVendorName(vo.getVendorName());
			setVendorCode(vo.getVendorCode());
			setProductModel(vo.getProductModel());
			setTwoLevelCategory(vo.getTwoLevelCategory());
			setShelfLife(vo.getShelfLife());
			setAttachmentPath(vo.getAttachmentPath());
		}

	}
}
