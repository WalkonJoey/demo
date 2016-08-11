package com.hqdna.vendor.bean;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.user.bean.User;
import com.hqdna.vendor.vo.VendorVo;

/**
 * 供应商实体类
 * 
 */
@Entity
@Table(name = "TBL_VENDOR")
public class Vendor implements ITransfer<VendorVo>, Serializable {
	private static final long serialVersionUID = 2961976494336836807L;
	private int vendorID; // id
	private String vendorCode; // 供应商编码
	private String vendorName; // 供应商名称
	private String postCode; // 邮编
	private String address; // 地址
	private User documentary; // 默认跟单
	private User purchaseEngineer; // 采购工程师
	private byte isEnable; // 是否可用
	private String comment; // 备注
	private User creator; // 创建人员
	private Timestamp createDt; // 创建时间
	private User operator; // 操作人员
	private Timestamp operateDt; // 操作时间

	//新增的属性
	private byte payPeriod; // 结算周期
	private byte payAppoint;// 付款约定
	private byte invoiceType; // 票据类型 1普通发票2增值税专用发票3增值税普通发票
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
	
	private int payTaxPoint; // 单价所含支付税点
	private BigDecimal purchaseTax;//采购sku税额             采购供应商有的不是按照税点来计算税的，而是没个sku加上一个税费如0.3来计算税的
	private float billRate;//开票税率  13%或者17%等  用于传入NC，采购价格 通过单价所含税点得到税后价格，税后价格/(1+13%)得到传入NC的开票税前单价
	
	private String cooperationWay;//合作方式
	private String getGoodsWay;//拿货方式
	@Id
	@GeneratedValue
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "documentary")
	public User getDocumentary() {
		return documentary;
	}

	public void setDocumentary(User documentary) {
		this.documentary = documentary;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "pEngineerID")
	public User getPurchaseEngineer() {
		return purchaseEngineer;
	}

	public void setPurchaseEngineer(User purchaseEngineer) {
		this.purchaseEngineer = purchaseEngineer;
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "creator")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@OrderBy("desc")
	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "operator")
	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	public Timestamp getOperateDt() {
		return operateDt;
	}

	public void setOperateDt(Timestamp operateDt) {
		this.operateDt = operateDt;
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

	
	public void transferPo2Vo(VendorVo vo, boolean direction) {
		if (vo == null) {
			return;
		}

		if (direction) {
			vo.setVendorID(getVendorID());
			vo.setVendorCode(getVendorCode());
			vo.setVendorName(getVendorName());
			vo.setPostCode(getPostCode());
			vo.setAddress(getAddress());
			
			vo.setIsEnable(getIsEnable());
			vo.setComment(getComment());
			
			vo.setClerkTelephone(getClerkTelephone());
			vo.setClerkEmail(getClerkEmail());
			vo.setClerkMobilePhone(getClerkMobilePhone());
			vo.setClerkFax(getClerkFax());
			vo.setClerkName(getClerkName());
			vo.setClerkQQ(getClerkQQ());
			vo.setCsmTelephone(getCsmTelephone());
			vo.setCsmEmail(getCsmEmail());
			vo.setCsmMobilePhone(getCsmMobilePhone());
			vo.setCsmFax(getCsmFax());
			vo.setCsmName(getCsmName());
			vo.setCsmQQ(getCsmQQ());
			
			vo.setPayAppoint(getPayAppoint());
			vo.setPayPeriod(getPayPeriod());
			vo.setPayType(getPayType());
			vo.setInvoiceType(getInvoiceType());
			vo.setPayTaxPoint(getPayTaxPoint());
			vo.setPurchaseTax(getPurchaseTax());
			vo.setBillRate(getBillRate());
			if (getDocumentary() != null) {
				vo.setDocumentaryID(getDocumentary().getUserID());
				vo.setDocumentaryName(getDocumentary().getUserCnName());
			}
			if(getPurchaseEngineer()!=null){
				vo.setpEngineerID(getPurchaseEngineer().getUserID());
				vo.setpEngineerName(getPurchaseEngineer().getUserCnName());
			}
			vo.setCreateDt(getCreateDt());
			vo.setCreatorID(getCreator().getUserID());
			vo.setCreatorName(getCreator().getUserCnName());

			vo.setOperateDt(getOperateDt());
			vo.setOperatorID(getOperator().getUserID());
			vo.setOperatorName(getOperator().getUserCnName());
		} else {
			setVendorID(vo.getVendorID());
			setVendorCode(vo.getVendorCode());
			setVendorName(vo.getVendorName());
			setPostCode(vo.getPostCode());
			setAddress(vo.getAddress());
			
			setIsEnable(vo.getIsEnable());
			setComment(vo.getComment());
			
			setClerkTelephone(vo.getClerkTelephone());
			setClerkEmail(vo.getClerkEmail());
			setClerkMobilePhone(vo.getClerkMobilePhone());
			setClerkFax(vo.getClerkFax());
			setClerkName(vo.getClerkName());
			setClerkQQ(getClerkQQ());
			setCsmTelephone(vo.getCsmTelephone());
			setCsmEmail(vo.getCsmEmail());
			setCsmMobilePhone(vo.getCsmMobilePhone());
			setCsmFax(vo.getCsmFax());
			setCsmName(vo.getCsmName());
			setCsmQQ(getCsmQQ());
			
			setPayTaxPoint(vo.getPayTaxPoint());
			setPurchaseTax(vo.getPurchaseTax());
			setPayAppoint(vo.getPayAppoint());
			setInvoiceType(vo.getInvoiceType());
			setPayPeriod(vo.getPayPeriod());
			setPayType(vo.getPayType());
			setBillRate(vo.getBillRate());
			if (vo.getDocumentaryID() != null) {
				User documentary = new User();
				documentary.setUserID(vo.getDocumentaryID());
				setDocumentary(documentary);
			}
			setCreateDt(vo.getCreateDt());
			if(vo.getpEngineerID()!=null){
				User pEngineer = new User();
				pEngineer.setUserID(vo.getpEngineerID());
				setPurchaseEngineer(pEngineer);
			}
			if (vo.getCreatorID() != null) {
				User creator = new User();
				creator.setUserID(vo.getCreatorID());
				setCreator(creator);
			}

			if (vo.getOperateDt() != null) {
				User operator = new User();
				operator.setUserID(vo.getOperatorID());
				setOperator(operator);
				setOperateDt(vo.getOperateDt());
			}
		}

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
