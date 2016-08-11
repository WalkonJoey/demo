package com.hqdna.permission.util;

/**
 * 已分配角色中某个权限项关联的whereSql
 * 
 *
 */
public interface IPermConstants {
	//权限中的SQL
	public static final String PERM_SQL = "PERM_SQL";
	//属性缓存标记
	public static final String ENTITY_PROPERTY = "ENTITY_PROPERTY";
	//用户在分配权限时的数据权限编辑时存储于session中的ruleVo的标记
	public static final String EDIT_RULE = "EDIT_RULE";
	//类的包名前缀
	public static final String PAKAGE_PREFIX = "com.hqdna";
}
