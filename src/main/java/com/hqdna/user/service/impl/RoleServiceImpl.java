package com.hqdna.user.service.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.dao.IRoleDao;
import com.hqdna.user.service.IRoleService;
import com.hqdna.user.vo.RoleVo;
import com.hqdna.user.vo.UserInfo;

@Service("roleService")
public class RoleServiceImpl extends AbstractBase implements IRoleService {

	@Resource(name = "roleDao")
	private IRoleDao roleDao;

	
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser, PageInfo pgInfo) {
		if (pgInfo == null) {
			return roleDao.queryAllRoles(-1, -1, null, null);
		} else {
			return roleDao.queryAllRoles(pgInfo.getStartIndex(),
					pgInfo.getRows(), null, null);
		}
	}

	
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap) {
		// TODO Auto-generated method stub
		return getRoleList(currentUser, pgInfo, whereSqlMap, null);
	}

	
	public QueryResult<RoleVo> getRoleList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return roleDao.queryAllRoles(-1, -1, whereSqlMap, order);
		} else {
			return roleDao.queryAllRoles(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}

	
	public RoleVo getRoleByID(String roleID) {
		// TODO Auto-generated method stub
		return roleDao.queryRoleByID(roleID);
	}

	
	public boolean updateRoleNameByID(UserInfo currentUser, String roleID,
			String roleName) {
		// TODO Auto-generated method stub
		return roleDao.updateRoleNameByID(currentUser, roleID, roleName);
	}

	
	public boolean disableRoleByID(UserInfo currentUser, String roleID) {
		return roleDao.disableRoleByID(currentUser, roleID);

	}

	
	public void addRole(UserInfo currentUser, RoleVo roleVo) {
		roleVo.setOperateDt(new Timestamp(System.currentTimeMillis()));
		roleVo.setRoleID("role"+UUID.randomUUID());
		roleDao.addRole(currentUser, roleVo);
	}

	
	public void updateRole(UserInfo currentUser, RoleVo roleVo) {
		roleVo.setOperateDt(new Timestamp(System.currentTimeMillis()));
		roleVo.setOperatorID(currentUser.getUserID());
		roleDao.updateRole(currentUser, roleVo);

	}

	@Transactional
	
	public void deleteRoleByIds(UserInfo currentUser, String roleIds) {
		String[] rolesId = roleIds.split(",");
		for (String roleID : rolesId) {
			roleDao.deleteRoleById(roleID);
		}
	}

	
	@Transactional
	public void assignRole4User(UserInfo currentUser, String targetUserID,
			Set<String> beforeRoles, Set<String> afterRoles) {
		for (Iterator<String> it = beforeRoles.iterator(); it.hasNext();) {
			String roleID = it.next();
			if (afterRoles.contains(roleID)) {
				it.remove();
				afterRoles.remove(roleID);
			}
		}

		// 删除
		roleDao.deleteRole4User(currentUser, targetUserID,
				beforeRoles.toArray(new String[] {}));
		// 新增
		roleDao.addRoles4User(currentUser, targetUserID,
				afterRoles.toArray(new String[] {}));
	};

	
	public List<RoleVo> getOwnRole4User(UserInfo currentUser,
			String targetUserID) {
		List<RoleVo> roles = roleDao.queryRole4User(currentUser, targetUserID);
		return roles;
	}

}
