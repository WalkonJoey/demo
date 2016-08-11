package com.hqdna.permission.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.permission.bean.Permission;
import com.hqdna.permission.bean.RolePerm;
import com.hqdna.permission.bean.Rule;
import com.hqdna.permission.dao.IPermissionDao;
import com.hqdna.permission.util.exception.PermissionException;
import com.hqdna.permission.vo.EntityProperty;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.permission.vo.PermType;
import com.hqdna.permission.vo.RuleVo;
import com.hqdna.user.bean.Role;
import com.hqdna.user.bean.User;
import com.hqdna.user.vo.UserInfo;

@Component("permissionDao")
public class PermissionDaoImpl extends DaoSupport<Permission> implements
		IPermissionDao {

	public PermItem isExists(String userID, String permID) {
		String checkSql = "select rp from UserRole as ur ,Role as r,RolePerm as rp,Permission p ,User u where ur.user=u.userID and rp.perm=p.permID and ur.role=r.roleID and r.roleID=rp.role and u.userID=?1 and p.permID=?2";
		try {
			List<RolePerm> list = executeQuery(checkSql, new String[] { userID,
					permID });
			if (list != null && list.size() > 0) {
				RolePerm rp = list.get(0);
				PermItem pitem = new PermItem();
				pitem.setPermID(permID);
				pitem.setPermName(rp.getPerm().getPermName());
				pitem.setPermType(rp.getPerm().getPermType());
				pitem.setWhereSql(rp.getWhereSQL());
				return pitem;
			}

		} catch (Exception e) {
			throw new PermissionException("Dao鉴定用户是否拥有权限出错", e);
		}
		return null;
	}

	public PermItem[] isExists(String userID, String moduleName, PermType type) {
		String checkSql = "select rp from UserRole as ur ,Role as r,RolePerm as rp,Permission p ,User u where ur.user=u.userID and rp.perm=p.permID and ur.role=r.roleID and r.roleID=rp.role and u.userID=?1 and p.module=?2 and p.permType=?3";
		try {
			List<RolePerm> list = executeQuery(checkSql, new String[] { userID,
					moduleName, type.name() });

			if (list == null || list.size() == 0) {
				return null;
			}
			PermItem[] permItems = new PermItem[list.size()];
			for (int i = 0; i < permItems.length; i++) {
				RolePerm rp = list.get(i);
				PermItem pitem = new PermItem();
				pitem.setPermID(rp.getPerm().getPermID());
				pitem.setPermName(rp.getPerm().getPermName());
				pitem.setPermType(rp.getPerm().getPermType());
				pitem.setModule(rp.getPerm().getModule());
				pitem.setWhereSql(rp.getWhereSQL());
				permItems[i] = pitem;
			}
			return permItems;

		} catch (Exception e) {
			throw new PermissionException("Dao鉴定用户是否拥有权限出错", e);
		}
	}

	@Transactional
	public void addPerm4RoleWithoutRule(UserInfo currentUser, String roleID,
			String permID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a from RolePerm a where a.role.roleID=?1 and a.perm.permID=?2");
		List<RolePerm> rps = executeQuery(sql.toString(), new Object[] {
				roleID, permID });
		RolePerm rp = null;
		if (rps != null && rps.size() == 1) {
			rp = rps.get(0);
			rp.setRules(null);
			rp.setWhereSQL(null);
		} else {
			rp = new RolePerm();
			Role role = new Role();
			role.setRoleID(roleID);
			Permission permission = new Permission();
			permission.setPermID(permID);
			rp.setRole(role);
			rp.setPerm(permission);
		}
		User user = new User();
		user.transferPo2Vo(currentUser, false);

		try {
			rp.setOperateDt(new Timestamp(System.currentTimeMillis()));
			rp.setOperatorUser(user);
			if (rp.getID() > 0) {
				em.merge(rp);
			} else {
				em.persist(rp);
			}
		} catch (Exception e) {
			throw new PermissionException("给角色ID[" + roleID + "]分配权限ID["
					+ permID + "]出错", e);
		}
	}

	
	@Transactional
	public void addBatchPerm4RoleWithoutRule(UserInfo currentUser,
			String roleID, String... permIDs) {
		if (permIDs == null || permIDs.length == 0) {
			return;
		}
		for (String permID : permIDs) {
			addPerm4RoleWithoutRule(currentUser, roleID, permID);
		}
	}

	
	@Transactional
	public void addPerm4RoleWithRule(UserInfo currentUser, String roleID,
			String permID, String whereSql, RuleVo... rules) {
		User user = new User();
		user.transferPo2Vo(currentUser, false);

		Role role = new Role();
		role.setRoleID(roleID);

		Permission permission = new Permission();
		permission.setPermID(permID);

		List<Rule> ruleList = new ArrayList<Rule>();
		for (RuleVo rule : rules) {
			Rule t_rule = new Rule();
			t_rule.transferPo2Vo(rule, false);
			t_rule.setOperatorUser(user);
			ruleList.add(t_rule);
		}

		try {
			RolePerm rp = new RolePerm();
			rp.setOperateDt(new Timestamp(System.currentTimeMillis()));
			rp.setRole(role);
			rp.setPerm(permission);
			rp.setOperatorUser(user);
			rp.setWhereSQL(whereSql);
			rp.setRules(ruleList);
			em.persist(rp);
		} catch (Exception e) {
			throw new PermissionException("给角色ID[" + roleID + "]分配权限ID["
					+ permID + "]出错", e);
		}
	}

	
	@Transactional
	public void deletePerm4Role(UserInfo currentUser, String roleID,
			String... permIDs) {
		StringBuilder sql = new StringBuilder();
		for (String permID : permIDs) {
			sql.append("delete from RolePerm where roleID=?1 and permID=?2");
			Object[] params = new Object[] { roleID, permID };
			executeUpdate(sql.toString(), params);
			sql.delete(0, sql.length());
		}

	}

	
	public List<PermItem> queryAllPerms(UserInfo currentUser) {
		List<PermItem> permItems = new ArrayList<PermItem>();
		List<Permission> perms = getScrollData(0, -1, " isEnable=?1 ",
				new Object[] { (byte) 1 }).getResultlist();
		for (Permission perm : perms) {
			PermItem pi = new PermItem();
			perm.transferPo2Vo(pi, true);
			permItems.add(pi);
		}
		if (permItems != null) {
			Collections.sort(permItems);
		}
		return permItems;
	}

	
	public List<PermItem> queryPerm4Role(UserInfo currentUser, String roleID) {
		List<PermItem> permItems = new ArrayList<PermItem>();

		String sql = "select p from RolePerm rp ,Permission p,Role r where r.roleID=?1 and r.roleID=rp.role and rp.perm=p.permID";
		List<Object> list = (List<Object>) executeQuery(sql,
				new String[] { roleID });
		for (Object obj : list) {
			Permission perm = (Permission) obj;
			PermItem pi = new PermItem();
			perm.transferPo2Vo(pi, true);
			permItems.add(pi);
		}
		if (permItems != null) {
			Collections.sort(permItems);
		}
		return permItems;
	}

	
	public List<UserInfo> getUsersWithPermission(Module module, PermType type) {
		String getSql = "select distinct u from UserRole as ur ,Role as r,RolePerm as rp,Permission p ,User u where ur.user=u.userID and rp.perm=p.permID and ur.role=r.roleID and r.roleID=rp.role and p.module=?1 and p.permType=?2 order by p.code asc";
		try {
			List<User> list = executeQuery(getSql, new String[] {
					module.name(), type.name() });
			if (list != null && list.size() > 0) {
				List<UserInfo> userList = new ArrayList<UserInfo>();
				for (User user : list) {
					UserInfo userInfo = new UserInfo();
					user.transferPo2Vo(userInfo, true);
					userList.add(userInfo);
				}
				return userList;
			}

		} catch (Exception e) {
			throw new PermissionException("Dao获取拥有相应权限的用户列表出错", e);
		}
		return null;
	}

	
	public List<PermItem> queryMenusByUser(UserInfo currentUser) {
		List<PermItem> perms = new ArrayList<PermItem>();

		StringBuilder sb = new StringBuilder();
		Object[] params = null;
		if (currentUser.isAdministrator()) {
			sb.append(" select distinct p from Permission p  where  p.permType=?1 and p.isEnable=1 order by p.code asc ");
			params = new Object[] { PermType.Menu.name() };
		} else {
			sb.append("select distinct p from UserRole as ur ,Role as r,RolePerm as rp,Permission p ,User u where ur.user=u.userID and rp.perm=p.permID and ur.role=r.roleID and r.roleID=rp.role and u.userID=?1 and p.permType=?2 and p.isEnable=1 order by p.code asc");
			params = new String[] { currentUser.getUserID(),
					PermType.Menu.name() };
		}
		try {
			List<Object> list = (List<Object>) executeQuery(sb.toString(),
					params);

			if (list == null) {
				return null;
			}

			for (Object obj : list) {
				Permission perm = ((Permission) obj);
				PermItem pitem = new PermItem();
				perm.transferPo2Vo(pitem, true);
				perms.add(pitem);
			}

		} catch (Exception e) {
			throw new PermissionException("Dao鉴定用户是否拥有权限出错", e);
		}
		if (perms != null) {
			Collections.sort(perms);
		}
		return perms;
	}

	
	public List<RuleVo> queryRules4RolePerm(String roleID, String permID) {
		List<RuleVo> list = new ArrayList<RuleVo>();
		StringBuilder sql = new StringBuilder();
		sql.append("select rp from RolePerm rp where rp.role.roleID=?1 and rp.perm.permID=?2");
		List<RolePerm> res = executeQuery(sql.toString(), new Object[] {
				roleID, permID });
		if (res == null || res.size() <= 0) {
			return null;
		}
		for (RolePerm rp : res) {
			List<Rule> rules = rp.getRules();
			for (Rule rule : rules) {
				RuleVo rv = new RuleVo();
				rule.transferPo2Vo(rv, true);
				list.add(rv);
			}
		}
		return list;

	}

	@Transactional
	
	public boolean addRules(UserInfo currentUser, String roleID, String permID,
			String whereSql, RuleVo... rules) {
		if (rules == null) {
			return false;
		}
		User user = new User();
		user.transferPo2Vo(currentUser, false);

		StringBuilder sql = new StringBuilder();
		sql.append("select rp from RolePerm rp where rp.role.roleID=?1 and rp.perm.permID=?2 ");
		List<RolePerm> rps = this.<RolePerm> executeQuery(sql.toString(),
				new Object[] { roleID, permID });
		if (rps == null || rps.isEmpty()) {
			return false;
		}
		RolePerm rolePerm = rps.get(0);
		// 更新sql
		rolePerm.setWhereSQL(whereSql);
		List<Rule> ruleList = rolePerm.getRules();
		if (ruleList == null) {
			ruleList = new ArrayList<Rule>();
		} else {
			ruleList.clear();
		}
		for (RuleVo rule : rules) {
			Rule t_rule = new Rule();
			t_rule.transferPo2Vo(rule, false);
			t_rule.setOperatorUser(user);
			ruleList.add(t_rule);
		}
		em.merge(rolePerm);
		return true;
	}

	@Transactional
	
	public boolean deleteRules(UserInfo currentUser, String roleID,
			String permID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select r from Rule r where r.rolePerm.role.roleID=?1 and r.rolePerm.perm.permID=?2 ");
		List<Rule> rules = this.<Rule> executeQuery(sql.toString(),
				new Object[] { roleID, permID });
		if (rules == null || rules.isEmpty()) {
			return false;
		}
		for (Rule rule : rules) {
			em.remove(rule);
		}
		return true;
	}

	
	public List<Object> getPropertyValue(EntityProperty ep) {
		String objStr = ep.getParentPropRef().getMappedTarget().getSimpleName();
		String objField = ep.getPropName();
		String sql = "select distinct o." + objField + " from " + objStr + " o"
				+ " where o." + objField + "!=null";
		List<Object> list = executeQuery(sql, null);
		return list;
	}

	
	public String queryRolePermID(String roleID, String permID) {
		String sql = "select rp from RolePerm rp ,Permission p,Role r where r.roleID=?1 and r.roleID=rp.role and rp.perm=p.permID and p.permID=?2";
		List<Object> list = (List<Object>) executeQuery(sql, new String[] {
				roleID, permID });
		if (list != null && list.size() == 1) {
			return Long.toString(((RolePerm) list.get(0)).getID());
		}
		return null;
	}

	
	public String updateWhereSQL4RolePermID(String roleID, String permID,
			String whereSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("update RolePerm set whereSql=?1 where roleID=?2 and permID=?3 ");
		executeUpdate(sql.toString(), new Object[] { whereSql, roleID, permID });
		return null;
	}

	
	public PermItem queryPermByPermID(String permID) {
		PermItem permItem = new PermItem();
		Permission perm = find(permID);
		if (perm != null) {
			perm.transferPo2Vo(permItem, true);
			return permItem;
		}
		return null;
	}

}
