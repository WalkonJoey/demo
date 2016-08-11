package com.hqdna.permission.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hqdna.common.baseService.IService;
import com.hqdna.permission.vo.EntityProperty;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.PermType;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 用户角色权限服务类接口
 * 
 * 
 */
public interface IPermissionService extends IService {


	/**
	 * 鉴定用户是否具有某个权限
	 * 
	 * @param userID
	 * @param permID
	 * @return
	 */
	public PermItem[] checkPermission(String userID, String moduleName, PermType type);

	/**
	 * 给指定角色分配权限,不带rules
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void assignPermission4RoleWithoutRule(UserInfo currentUser,
			String roleID, String permID);

	/**
	 * 给指定角色分配权限,带rules
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void assignPermission4RoleWithRule(UserInfo currentUser,
			String roleID, String permID, Set<String> formerPerms,
			RuleVo... rules);

	
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
			String... permIDs);
	/**
	 * 获取所有权限
	 * 
	 * @param currentUser
	 * @param userID
	 * @return
	 */
	public Map<String, List<PermItem>> getAllPerms(UserInfo currentUser);

	/**
	 * 获取指定角色已经分配的权限
	 * 
	 * @param currentUser
	 * @param userID
	 * @return List<PermItem>
	 */
	public List<PermItem> getAssignedPerm4Role(UserInfo currentUser,
			String roleID);

	/**
	 * 获取当前用户相应的菜单
	 * 
	 * @param currentUser
	 * @param userID
	 * @return
	 */
	public Map<String, List<PermItem>> getMenusByUser(UserInfo currentUser);

	/**
	 * 获取相应字段的数据列表，如用户的登录名UserInfo.loginID
	 * 
	 * @param permID
	 *            权限项ID
	 * @return fieldName 属性全路径名,如operator.operator.loginID
	 */
	public List<Object> getPropertyValue(String refObjID, String fieldName);

	/**
	 * 获取指定角色下权限项的规则里列表
	 * 
	 * @param roleID
	 *            角色ID
	 * @param permID
	 *            权限项ID
	 * @return
	 */
	public List<RuleVo> getRules4RolePerm(String roleID, String permID);

	/**
	 * 给指定角色下的权限项新增规则, 保存
	 * 
	 * @param roleID
	 *            角色ID
	 * @param permID
	 *            权限项ID
	 * @param rule
	 *            新增的规则
	 * @return
	 */
	public void saveRules4RolePerm(UserInfo currentUser, String roleID,
			String permID, RuleVo... rules);

	/**
	 * 通过permID获取PermItem对象
	 * 
	 * @param permID
	 * @return
	 */
	public PermItem getPermItemById(String permID);

	/**
	 * 获取实体属性了列表
	 * 
	 * @param refObj
	 * @return
	 */
	public List<EntityProperty> getEntityProperties(String refObj);
	
	
}
