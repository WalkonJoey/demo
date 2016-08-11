package com.hqdna.permission.service.impl;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.permission.dao.IPermissionDao;
import com.hqdna.permission.service.IPermissionService;
import com.hqdna.permission.util.IPermConstants;
import com.hqdna.permission.vo.EntityProperty;
import com.hqdna.permission.vo.FieldTitle;
import com.hqdna.permission.vo.FieldType;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.PermType;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.vo.UserInfo;

@Service("permissionService")
public class PermissionServiceImpl extends AbstractBase implements
		IPermissionService {

	@Resource(name = "permissionDao")
	private IPermissionDao permissionDao;

	// 这里的鉴定权限以后使用缓存机制
	
	public PermItem[] checkPermission(String userID, String moduleName,
			PermType type) {
		PermItem[] pItem = permissionDao.isExists(userID, moduleName, type);
		return pItem;
	}

	public void extract() {

		// 删除
		// permissionDao.deletePerm4Role(currentUser, roleID,
		// beforePerms.toArray(new String[] {}));
		// // 增加
		// permissionDao.addBatchPerm4RoleWithoutRule(currentUser, roleID,
		// afterPerms.toArray(new String[] {}));
	}

	
	public void assignPermission4RoleWithoutRule(UserInfo currentUser,
			String roleID, String permID) {
		// 增加
		permissionDao.addBatchPerm4RoleWithoutRule(currentUser, roleID, permID);

	}

	/**
	 * 给指定角色取消权限
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void deletePerm4Role(UserInfo currentUser, String roleID,
			String... permIDs) {
		permissionDao.deletePerm4Role(currentUser, roleID, permIDs);
	}

	
	public void assignPermission4RoleWithRule(UserInfo currentUser,
			String roleID, String permID, Set<String> formerPerms,
			RuleVo... rules) {
		if (rules == null || rules.length == 0) {
			assignPermission4RoleWithoutRule(currentUser, roleID, permID);
			return;
		}
		PermItem pi = permissionDao.queryPermByPermID(permID);
		String refObjID = pi.getRefObjID();

		// 临时变量，用作whereSQL
		StringBuilder whereSql = new StringBuilder();
		for (RuleVo rule : rules) {
			// 从缓存中获取该规则对应实体对象的属性对象
			EntityProperty veryEp = getVeryEntityProperty(
					rule.getPropertyName(), refObjID);
			// 转换whereSQL
			rule.setEp(veryEp);
			whereSql.append(rule.getWhereSql()).append(" ");
		}

		if (formerPerms.contains(permID)) {
			// 如果已经分配有此权限，则直接更新规则
			permissionDao.deleteRules(currentUser, roleID, permID);
			permissionDao.addRules(currentUser, roleID, permID, whereSql
					.substring(0, whereSql.length() - 1).toString(), rules);
		} else {
			permissionDao.addPerm4RoleWithRule(currentUser, roleID, permID,
					whereSql.substring(0, whereSql.length() - 1).toString(),
					rules);
		}

	}

	
	public List<PermItem> getAssignedPerm4Role(UserInfo currentUser,
			String roleID) {
		List<PermItem> permItems = permissionDao.queryPerm4Role(currentUser,
				roleID);
		return permItems;
	}

	
	public Map<String, List<PermItem>> getAllPerms(UserInfo currentUser) {
		Map<String, List<PermItem>> permMap = new TreeMap<String, List<PermItem>>();
		List<PermItem> permItems = permissionDao.queryAllPerms(currentUser);
		for (PermItem pi : permItems) {
			String parentCode = pi.getParentCode();
			if (permMap.get(parentCode) == null) {
				List<PermItem> list = new ArrayList<PermItem>();
				permMap.put(parentCode, list);
			}
			permMap.get(parentCode).add(pi);
		}

		return permMap;
	}

	
	public Map<String, List<PermItem>> getMenusByUser(UserInfo currentUser) {
		Map<String, List<PermItem>> permMap = new TreeMap<String, List<PermItem>>();
		List<PermItem> menus = permissionDao.queryMenusByUser(currentUser);
		for (PermItem pi : menus) {
			String parentCode = pi.getParentCode();
			if (permMap.get(parentCode) == null) {
				List<PermItem> list = new ArrayList<PermItem>();
				permMap.put(parentCode, list);
			}
			permMap.get(parentCode).add(pi);
		}
		return permMap;
	}

	
	public List<Object> getPropertyValue(String refObj, String propertyName) {
		EntityProperty veryEp = getVeryEntityProperty(propertyName, refObj);
		if (veryEp != null && veryEp.getPropValue() != null
				&& veryEp.getPropValue().length > 0) {
			return Arrays.asList(veryEp.getPropValue());
		}
		return permissionDao.getPropertyValue(veryEp);
	}

	
	public List<RuleVo> getRules4RolePerm(String roleID, String permID) {
		List<RuleVo> list = permissionDao.queryRules4RolePerm(roleID, permID);
		if (list == null) {
			return null;
		}
		Collections.sort(list);
		return list;
	}

	@Transactional
	
	public void saveRules4RolePerm(UserInfo currentUser, String roleID,
			String permID, RuleVo... rules) {

		PermItem pi = permissionDao.queryPermByPermID(permID);
		if (pi == null || pi.getRefObjID() == null) {
			permissionDao.addPerm4RoleWithoutRule(currentUser, roleID, permID);
			return;
		}
		String refObjID = pi.getRefObjID();

		// 临时变量，用作whereSQL
		StringBuilder whereSql = new StringBuilder();
		for (RuleVo rule : rules) {
			// 从缓存中获取该规则对应实体对象的属性对象
			EntityProperty veryEp = getVeryEntityProperty(
					rule.getPropertyName(), refObjID);
			// 转换whereSQL
			rule.setEp(veryEp);
			// 设置规则信息
			rule.setOperatorID(currentUser.getUserID());
			rule.setOperateDt(new Timestamp(System.currentTimeMillis()));
		}
		// 删除所有rule
		permissionDao.deleteRules(currentUser, roleID, permID);
		// 新增rule
		permissionDao.addRules(currentUser, roleID, permID,
				whereSql.toString(), rules);

	}

	
	public PermItem getPermItemById(String permID) {
		return permissionDao.queryPermByPermID(permID);
	}

	
	public List<EntityProperty> getEntityProperties(String refObjID) {
		if (refObjID == null || refObjID.trim().length() == 0) {
			return null;
		}
		// 从缓存中获取
		EntityProperty epRoot = cm.<EntityProperty> getCacheByName(
				IPermConstants.ENTITY_PROPERTY).getFromCache(refObjID);
		if (epRoot != null) {
			List<EntityProperty> list = epRoot.getPropRef();
			if (list != null && list.size() == 1) {
				return list.get(0).getPropRef();
			}
		}
		// 如果缓存中没有，则主动生成，并放到缓存中
		EntityProperty objectRoot = null;
		try {
			objectRoot = getRootEntityProperty(Class.forName(refObjID));
		} catch (Exception e) {
			e.printStackTrace();
		}
		cm.getCacheByName(IPermConstants.ENTITY_PROPERTY).putIfAbsent(refObjID,
				objectRoot);
		return objectRoot.getPropRef();
	}

	/**
	 * 获取当前关联对象中 指定属性名的直接具体属性对象
	 * 
	 * @param propertyName
	 *            属性全路径名
	 * @param refObjID
	 *            关联对象ID
	 */
	private EntityProperty getVeryEntityProperty(String propertyName,
			String refObjID) {
		EntityProperty ep = new EntityProperty();
		ep.setPropRef(getEntityProperties(refObjID));
		EntityProperty veryEp = searchForField(propertyName, ep);
		return veryEp;
	}

	/**
	 * 获取属性全名的实体属性对象，如operator.operator.loginID
	 */
	private EntityProperty searchForField(String fieldName, EntityProperty ep) {
		int firstIndox = fieldName.indexOf(".");
		String partStr = firstIndox == -1 ? fieldName : fieldName.substring(0,
				firstIndox);
		for (EntityProperty eep : ep.getPropRef()) {
			if (eep.getPropName() == null
					|| eep.getPropName().equalsIgnoreCase(partStr)) {
				if (firstIndox == -1) {
					return eep;
				} else {
					return searchForField(fieldName.substring(firstIndox + 1),
							eep);
				}
			}
		}

		return null;
	}

	/**
	 * 获取一个对象的所有层级属性对象
	 * 
	 * @param className
	 * @return
	 */
	private EntityProperty getRootEntityProperty(Class className) {
		EntityProperty root = new EntityProperty();
		root.setMappedTarget(className);
		root.setPropRef(generateFieldTitle(className, root, true));
		return root;
	}

	/**
	 * 生成实体的属性对象
	 * 
	 * @param className
	 *            字段所属父亲对象的类
	 * @param parentProRef
	 *            父亲属性对象
	 * @param isGoDeep
	 *            是否继续深度遍历
	 * @return
	 */
	private List<EntityProperty> generateFieldTitle(Class className,
			EntityProperty parentProRef, boolean isGoDeep) {

		List<EntityProperty> properties = new ArrayList<EntityProperty>();
		for (Class clazz = className; clazz != null; clazz = clazz
				.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field fld : fields) {
				FieldTitle ft = fld.getAnnotation(FieldTitle.class);
				if (ft == null)
					continue;
				EntityProperty peoperty = new EntityProperty();
				if (ft.refClass().equals(Object.class)) {
					// 一对一，多对一关联时，获取字段定义类型的类类型
					Class ftClazz = fld.getType();
					if (!ftClazz.isPrimitive()
							&& !String.class.equals(ftClazz)
							&& ftClazz.getName().startsWith(
									IPermConstants.PAKAGE_PREFIX)) {
						// 非原始类型，以及String类型,这里最重要的是最后一个条件，前面两个条件还不够，比如时间，日历等
						peoperty.setMappedTarget(ftClazz);
					}
				} else {
					// 一对多，多对多时，直接获取注解中的关联属性名定义的类类型
					peoperty.setMappedTarget(ft.refClass());
				}

				// 设置父亲属性对象
				peoperty.setParentProRef(parentProRef);
				peoperty.setPropTitle(ft.name());
				peoperty.setPropName(fld.getName());
				peoperty.setPropType(ft.fieldType());
				if (ft.fieldType() == FieldType.SELECTABLE_VALUE
						&& ft.values().length > 0) {
					peoperty.setPropValue(ft.values());
				}
				boolean isCircle = peoperty.checkIsCircle();
				if (peoperty.getMappedTarget() == null) {
					// 普通字段
					peoperty.setPropRef(null);
					properties.add(peoperty);
				} else if (isGoDeep && isCircle) {
					// 上下级重复
					peoperty.setPropRef(generateFieldTitle(
							peoperty.getMappedTarget(), peoperty, false));
					properties.add(peoperty);
				} else if (isGoDeep && !isCircle) {
					// 上下级类型不重复
					peoperty.setPropRef(generateFieldTitle(
							peoperty.getMappedTarget(), peoperty, true));
					properties.add(peoperty);
				} else {
					peoperty.setPropRef(null);
				}

			}
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return properties;
	}

	/**
	 * 初始化方法
	 */
	
	public void init() {
		long start = DateUtil.nowMilli();
		if (logger.isDebugEnabled()) {
			logger.debug("加载权限数据开始...");
		}
		cm.createAndGetCacheByName(IPermConstants.ENTITY_PROPERTY);
		if (logger.isDebugEnabled()) {
			logger.debug("加载权限数据结束." + DateUtil.cal(start, DateUtil.nowMilli()));
		}
	}

}
