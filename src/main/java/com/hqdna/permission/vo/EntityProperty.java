package com.hqdna.permission.vo;

import java.util.List;

/**
 * 实体属性树节点对象
 * 
 */
public final class EntityProperty {

	// 所属类,映射的PO类
	private Class mappedTarget = null;
	// 中文属性名
	private String propTitle;
	// 字段名
	private String propName;
	// 可供选择的属性值
	private Object[] propValue;
	// 属性类型
	private FieldType propType;
	// 父亲属性实体
	private EntityProperty parentPropRef = null;
	// 属性关联实体
	private List<EntityProperty> propRef = null;

	public Class getMappedTarget() {
		return mappedTarget;
	}

	public void setMappedTarget(Class mappedTarget) {
		this.mappedTarget = mappedTarget;
	}

	public String getPropTitle() {
		return propTitle;
	}

	public void setPropTitle(String propTitle) {
		this.propTitle = propTitle;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public Object[] getPropValue() {
		return propValue;
	}

	public void setPropValue(Object[] propValue) {
		this.propValue = propValue;
	}

	public FieldType getPropType() {
		return propType;
	}

	public void setPropType(FieldType propType) {
		this.propType = propType;
	}

	public EntityProperty getParentPropRef() {
		return parentPropRef;
	}

	public void setParentProRef(EntityProperty parentPropRef) {
		this.parentPropRef = parentPropRef;
	}

	public List<EntityProperty> getPropRef() {
		return propRef;
	}

	public void setPropRef(List<EntityProperty> propRef) {
		this.propRef = propRef;
	}

	/**
	 * 判断上下级属性类型是否存在循环包含关系
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean checkIsCircle() {
		EntityProperty ep = this;
		Class temp=null;
		while (ep != null) {
			if (ep.getMappedTarget() == null) {
				return true;
			} else if (ep.getMappedTarget().equals(temp)) {
				return true;
			}
			if(temp==null){
				temp=ep.getMappedTarget();
			}
			ep = ep.getParentPropRef();
		}
		return false;
	}
}
