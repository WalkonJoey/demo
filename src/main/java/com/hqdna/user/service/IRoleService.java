package com.hqdna.user.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 角色服务类接口
 * 
 * 
 */
public interface IRoleService {

	/**
	 * 获取角色列表
	 * 
	 * @param currentUser
	 *            当前用户
	 * @param pgInfo
	 *            分页对象,可为null
	 * @return
	 */
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser,
			PageInfo pgInfo);

	/**
	 * 获取角色列表
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param pgInfo
	 *            分页对象 为NULL时则为查询所有记录
	 * @return
	 */
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap);

	/**
	 * 获取角色列表
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param pgInfo
	 *            分页对象,可为null
	 * @param whereSql
	 *            过滤SQL,可为null
	 * @param order
	 *            排序字段,<字段名,排序方式>,可为null
	 * @return
	 */
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 通过roleID查找角色信息
	 * 
	 * @param roleID
	 * @return
	 */
	public RoleVo getRoleByID(String roleID);

	/**
	 * 新建角色
	 * 
	 * @param role
	 */
	public void addRole(UserInfo currentUser, RoleVo role);

	/**
	 * 更新角色名称
	 * 
	 * @param roleID
	 * @param roleName
	 */
	public boolean updateRoleNameByID(UserInfo currentUser, String roleID,
			String roleName);

	/**
	 * 通过roleID禁用某个角色
	 * 
	 * @param roleID
	 */
	public boolean disableRoleByID(UserInfo currentUser, String roleID);

	/**
	 * 更新整个实体
	 * 
	 * @param roleVo
	 */
	public void updateRole(UserInfo currentUser, RoleVo roleVo);

	/**
	 * 根据roleId批量删除role
	 */
	public void deleteRoleByIds(UserInfo currentUser, String roleIds);

	/**
	 * 给指定用户分配角色
	 * 
	 * @param userID
	 * @param beforeRoles
	 *            分配前的roleID
	 * @param afterRoles
	 *            分配后的roleID
	 */
	public void assignRole4User(UserInfo currentUser, String targetUserID,
			Set<String> beforeRoles, Set<String> afterRoles);

	/**
	 * 获取指定用户的拥有角色
	 * 
	 * @param currentUser
	 * @param userID
	 * @return
	 */
	public List<RoleVo> getOwnRole4User(UserInfo currentUser, String targetUserID);

}
