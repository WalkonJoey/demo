package com.hqdna.user.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.bean.Role;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 角色持久层接口
 * 
 * 
 * @author Aaron 2014-7-25
 * 
 */
public interface IRoleDao extends DAO<Role> {

	/**
	 * 查询角色
	 * 
	 * @return
	 */
	public QueryResult<RoleVo> queryAllRoles(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);

	/**
	 * 通过roleID查找角色信息
	 * 
	 * @param roleID
	 * @return
	 */
	public RoleVo queryRoleByID(String roleID);

	/**
	 * 新增角色
	 * 
	 * @param role
	 */
	public void addRole(UserInfo currentUser, RoleVo role);

	/**
	 * 删除角色
	 * @param roleIds
	 */
	public void deleteRoleById(String roleId);
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
	 * 获取指定用户所拥有的角色
	 * 
	 * @param currentUser
	 * @param targetUserID
	 * @return
	 */
	public List<RoleVo> queryRole4User(UserInfo currentUser, String targetUserID);

	/**
	 * 对指定用户删除相应的角色
	 * 
	 * @param targetUserID
	 * @param roleIDs
	 */
	public void deleteRole4User(UserInfo currentUser, String targetUserID,
			String... roleIDs);

	/**
	 * 对指定用户增加相应的角色
	 * 
	 * @param targetUserID
	 * @param roleIDs
	 */
	public void addRoles4User(UserInfo currentUser, String targetUserID,
			String... roleIDs);

}
