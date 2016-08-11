package com.hqdna.permission.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体对象属性标记
 * 
 * 
 * @author Aaron 2014-8-5
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface FieldTitle {
	// 属性名称
	public String name();

	// 引用属性类型
	public Class refClass() default Object.class;

	// 属性类型
	public FieldType fieldType() default FieldType.CUSTOM_STRING;
	
	//	关联的普通的值
	public String[] values() default {};
}
