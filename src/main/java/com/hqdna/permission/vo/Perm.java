package com.hqdna.permission.vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * 
 * 
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Perm {
	// 这里的关联相当于模块，但是为了启用数据权限，这里直接以关联对象代替
	public Module module();

	// 权限类型@See PermType
	public PermType type();
}
