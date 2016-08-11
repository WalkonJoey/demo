package com.hqdna.user.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.bean.Role;
import com.hqdna.user.bean.User;
import com.hqdna.user.dao.IRoleDao;
import com.hqdna.user.util.exception.RoleException;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.vo.UserInfo;

@Component("roleDao")
@Transactional
public class RoleDaoImpl extends DaoSupport<Role> implements IRoleDao {

	
	public QueryResult<RoleVo> queryAllRoles(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		List<RoleVo> list = new ArrayList<RoleVo>();

		QueryResult<RoleVo> r = new QueryResult<RoleVo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<Role> result = null;
		if (sql == null) {
			result = getScrollData(firstindex, maxresult, null, null, order);
		} else {
			result = getScrollData(firstindex, maxresult, (String) sql[0],
					(Object[]) sql[1], order);
		}

		List<Role> roles = (List<Role>) result.getResultlist();
		try {
			for (Role role : roles) {
				RoleVo target_role = new RoleVo();
				role.transferPo2Vo(target_role, true);
				list.add(target_role);
			}
		} catch (Exception e) {
			throw new RoleException("Dao查询全部角色出错", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}

	
	public RoleVo queryRoleByID(String roleID) {
		RoleVo target_role = new RoleVo();
		Role role = find(roleID);
		try {
			role.transferPo2Vo(target_role, true);
		} catch (Exception e) {
			throw new RoleException("Dao角色获取出错", e);
		}
		return target_role;
	}

	@Transactional
	
	public void addRole(UserInfo currentUser, RoleVo role) {
		User op = new User();
		Role target_role = new Role();
		try {
			BeanUtils.copyProperties(currentUser, op,
					new String[] { "operator" });

			BeanUtils.copyProperties(role, target_role,
					new String[] { "isEnable" });
		} catch (Exception e) {
			throw new RoleException("Dao角色新增出错", e);
		}
		target_role.setIsEnable((byte) 1);
		target_role.setOperatorUser(op);
		save(target_role);
	}

	@Transactional
	
	public void deleteRoleById(String roleId) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("delete from Role r where r.roleID=?1 and not exists(select 1 from UserRole ur where ur.role.roleID=?2)");
			executeUpdate(sql.toString(), new Object[] { roleId,
					roleId });
		} catch (Exception e) {
			throw new RoleException("Dao删除角色出错", e);
		}
	}

	@Transactional
	
	public boolean updateRoleNameByID(UserInfo currentUser, String roleID,
			String roleName) {
		int count = 0;
		try {
			count = executeUpdate(
					"update Role r set r.roleName=?1 where r.roleID=?2 ",
					new String[] { roleName, roleID });
		} catch (Exception e) {
			throw new RoleException("Dao更新角色出错", e);
		}
		return count == 1 ? true : false;
	}

	@Transactional
	
	public boolean disableRoleByID(UserInfo currentUser, String roleID) {
		int count = executeUpdate(
				"update Role r set r.isEnable=0 where r.roleID=?1",
				new String[] { roleID });
		return count == 1 ? true : false;
	}

	@Transactional
	
	public void updateRole(UserInfo currentUser, RoleVo roleVo) {
		User op = new User();
		Role target_role = new Role();
		try {
			BeanUtils.copyProperties(currentUser, op,
					new String[] { "operator" });

			BeanUtils.copyProperties(roleVo, target_role,
					new String[] { "operator" });
		} catch (Exception e) {
			throw new RoleException("Dao角色修改出错", e);
		}
		target_role.setOperatorUser(op);
		update(target_role);
	}

	
	public List<RoleVo> queryRole4User(UserInfo currentUser, String targetUserID) {
		String sql = "select r from Role r,UserRole ur,User u where u.userID=?1 and u.userID=ur.user and r.roleID=ur.role";
		String[] params = new String[] { targetUserID };
		List<Object> roles = executeQuery(sql, params);
		List<RoleVo> roleVos = new ArrayList<RoleVo>();
		for (Object obj : roles) {
			RoleVo rv = new RoleVo();
			((Role) obj).transferPo2Vo(rv, true);
			roleVos.add(rv);
		}
		return roleVos;
	}

	
	@Transactional
	public void deleteRole4User(UserInfo currentUser, String targetUserID,
			String... roleIDs) {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from UserRole ur where ur.user.userID=?1 and ur.role.roleID=?2");
		for (String roleID : roleIDs) {
			Object[] params = new Object[] { targetUserID, roleID };
			executeUpdate(sql.toString(), params);
		}

	}

	
	@Transactional
	public void addRoles4User(UserInfo currentUser, String targetUserID,
			String... roleIDs) {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into TBL_USER_ROLE (userID,roleID,operator,operateDt) values (?1,?2,?3,?4)");
		for (String roleID : roleIDs) {
			Object[] params = new Object[] { targetUserID, roleID,
					currentUser.getUserID(),
					new Timestamp(System.currentTimeMillis()) };
			executeByNativeSQL(sql.toString(), params);
		}

	}

}
