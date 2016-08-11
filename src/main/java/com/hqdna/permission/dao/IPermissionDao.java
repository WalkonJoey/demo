package com.hqdna.permission.dao;

import java.util.List;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.permission.bean.Permission;
import com.hqdna.permission.vo.EntityProperty;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.PermType;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 权限持久层接口
 * 
 * 
 */
public interface IPermissionDao extends DAO<Permission> {

	/**
	 * 是否存在相应的权限
	 * 
	 * @param userID
	 * @param permID
	 * @return
	 */
	public PermItem isExists(String userID, String permID);

	/**
	 * 是否存在相应的权限
	 * 
	 * @param userID
	 * @return
	 */
	public PermItem[]  isExists(String userID, String moduleName, PermType type);
	

	/**
	 * 获取拥有相应权限的用户
	 * 
	 * @param userID
	 * @return
	 */
	public List<UserInfo> getUsersWithPermission(Module module, PermType type);
	/**
	 * 给指定角色分配权限,不带有rule
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void addPerm4RoleWithoutRule(UserInfo currentUser, String roleID,
			String permIDs);

	/**
	 * 批量 给指定角色分配权限,不带有rule
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void addBatchPerm4RoleWithoutRule(UserInfo currentUser,
			String roleID, String... permIDs);

	/**
	 * 给指定角色分配权限,带有rule
	 * 
	 * @param roleID
	 * @param beforeRoles
	 *            分配前的beforePermIDs
	 * @param afterRoles
	 *            分配后的afterPermIDs
	 */
	public void addPerm4RoleWithRule(UserInfo currentUser, String roleID,
			String permID, String whereSql, RuleVo... rules);

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
	public List<PermItem> queryAllPerms(UserInfo currentUser);

	/**
	 * 获取指定角色已经分配的权限
	 * 
	 * @param currentUser
	 * @param userID
	 * @return
	 */
	public List<PermItem> queryPerm4Role(UserInfo currentUser, String roleID);

	/**
	 * 获取目标用户的菜单
	 * 
	 * @param currentUser
	 * @param userID
	 * @return
	 */
	public List<PermItem> queryMenusByUser(UserInfo currentUser);

	/**
	 * 获取相应字段的数据列表，如用户的登录名UserInfo.loginID
	 * 
	 * @param EntityProperty
	 * @return
	 */
	public List<Object> getPropertyValue(EntityProperty ep);

	/**
	 * 获取指定角色下权限项的规则里列表
	 * 
	 * @param roleID
	 *            角色ID
	 * @param permID
	 *            权限项ID
	 * @return
	 */
	public List<RuleVo> queryRules4RolePerm(String roleID, String permID);

	/**
	 * 给指定角色下的权限项新增规则
	 * 
	 * @param rolePermID
	 *            角色权限ID
	 * @param rule
	 *            新增的规则
	 * @return
	 */
	public boolean addRules(UserInfo currentUser, String roleID, String permID,
			String whereSql, RuleVo... rules);

	/**
	 * 更新rule（唯一rule调用接口,先删除原先的，再新增）
	 * 
	 * @param rule
	 *            新增的规则
	 * @return
	 */
	public boolean deleteRules(UserInfo currentUser, String roleID,
			String permID);

	/**
	 * 查询指定的角色已分配的指定权限的ID值
	 * 
	 * @param roleID
	 * @param permID
	 * @return
	 */
	public String queryRolePermID(String roleID, String permID);

	/**
	 * 更新指定角色分配的权限的sql
	 * 
	 * @param roleID
	 * @param permID
	 * @return
	 */
	public String updateWhereSQL4RolePermID(String roleID, String permID,
			String whereSql);

	/**
	 * 通过permID获取permission对象
	 * 
	 * @param permID
	 * @return
	 */
	public PermItem queryPermByPermID(String permID);

}
