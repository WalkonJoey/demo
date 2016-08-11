package com.hqdna.product.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hqdna.common.baseVo.ITransfer;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.bean.User;

/**
 * 产品分类基础信息表
 * 
 * 
 */
@Entity
@Table(name = "TBL_PRODUCT_CATEGORY")
public class Category implements ITransfer<CategoryVo>, Serializable {
	private static final long serialVersionUID = -2301701982581168220L;
	private int categoryID;
	private int categoryParentID;
	@FieldTitle(name = "分类代码", fieldType = FieldType.SELECTABLE_VALUE)
	private int categoryCode;
	@FieldTitle(name = "分类名称", fieldType = FieldType.SELECTABLE_VALUE)
	private String categoryCnName;
	private String categoryEnName;
	private String comment;
	@FieldTitle(name = "创建人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User creatorUser;
	@FieldTitle(name = "创建时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp createDt;
	@FieldTitle(name = "最后操作人员", fieldType = FieldType.SELECTABLE_VALUE)
	private User operatorUser;
	@FieldTitle(name = "最后操作时间", fieldType = FieldType.CUSTOM_DATETIME)
	private Timestamp operateDt;
	private byte level;//分类等级，一级还是二级
	private String declareName;//因为申报名称是通过分类名称得来的，所以在这里批量管理
	/*private List<Product> products;*/
	private Set<AttrGroup> attrGroups;
	private String ncCode;//分类在NC的编码

	//2014/12/1 Joey
	//private Set<Product> productSet = new HashSet<Product>();
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
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

	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "TBL_CATEGORY_ATTRGROUP", joinColumns = @JoinColumn(name = "categoryID"), inverseJoinColumns = @JoinColumn(name = "attrGroupID"))
	public Set<AttrGroup> getAttrGroups() {
		return attrGroups;
	}

	public void setAttrGroups(Set<AttrGroup> attrGroups) {
		this.attrGroups = attrGroups;
	}

	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "productID", referencedColumnName = "categoryID")
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}*/

	

	public String getCategoryEnName() {
		return categoryEnName;
	}

	public void setCategoryEnName(String categoryEnName) {
		this.categoryEnName = categoryEnName;
	}
	
	/*@ManyToMany(cascade = CascadeType.ALL,mappedBy = "categoryList")////关系的被维护端使用mappedBy
	public Set<Product> getProductSet() {
		return productSet;
	}
	
	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}*/

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

	//重载hashcode，通过categoryID判断对象是否相等
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + categoryID;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryID != other.categoryID)
			return false;
		return true;
	}

	public void transferPo2Vo(CategoryVo vo, boolean direction) {
		if (vo == null) {
			return;
		}
		if (direction) {
			vo.setCategoryID(getCategoryID());
			vo.setCategoryCnName(getCategoryCnName());
			vo.setCategoryParentID(getCategoryParentID());
			vo.setCategoryCode(getCategoryCode());
			vo.setComment(getComment());
			vo.setCreatorID(getCreatorUser() != null ? getCreatorUser()
					.getUserID() : null);
			vo.setCreatorName(getCreatorUser() != null ? getCreatorUser()
					.getUserCnName() : null);
			vo.setCategoryEnName(getCategoryEnName());
			vo.setCreateDt(getCreateDt());
			vo.setOperatorID(getOperatorUser() != null ? getOperatorUser()
					.getUserID() : null);
			vo.setOperatorName(getOperatorUser() != null ? getOperatorUser()
					.getUserCnName() : null);
			vo.setOperateDt(getOperateDt());
			vo.setLevel(getLevel());
			vo.setDeclareName(getDeclareName());
			vo.setNcCode(getNcCode());
		} else {
			if(vo.getCategoryID()>-1){
				setCategoryID(vo.getCategoryID());
			}
			if (vo.getCategoryCnName() != null) {
				setCategoryCnName(vo.getCategoryCnName());
			}
			if (vo.getCategoryENName() != null) {
				setCategoryEnName(vo.getCategoryENName());
			}
			if (vo.getCategoryCode() != 0) {
				setCategoryCode(vo.getCategoryCode());
			}
			if (vo.getCategoryParentID() >= 0) {
				setCategoryParentID(vo.getCategoryParentID());
			}
			if(vo.getComment()!=null){
				setComment(vo.getComment());
			}
			if (vo.getCreateDt() != null) {
				setCreateDt(vo.getCreateDt());
			}
			setLevel(vo.getLevel());
			setDeclareName(vo.getDeclareName());
			setOperateDt(vo.getOperateDt());
			setNcCode(vo.getNcCode());
		}

	}
}
