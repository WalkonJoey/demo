package com.hqdna.user.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.commonTools.MD5;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.PermType;
import com.hqdna.user.bean.User;
import com.hqdna.user.service.IUserService;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.util.exception.UserException;
import com.hqdna.user.vo.DepartmentVo;
import com.hqdna.user.vo.UserInfo;

@Service("userService")
public class UserServiceImpl extends AbstractBase implements IUserService {
	
	
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo) {
		return getUserList(currentUser, pgInfo, null);
	}

	
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap) {
		return getUserList(currentUser, pgInfo, whereSqlMap, null);
	}

	
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return userDao.queryAllUsers(0, -1, whereSqlMap, order);
		} else {
			return userDao.queryAllUsers(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}

	
	public List<DepartmentVo> getDepartmentList(UserInfo currentUser) {
		return userDao.queryDepartmentList(currentUser);
	}

	
	public List<UserInfo> getUserListWithPermission(Module module, PermType type) {
		return permissionDao.getUsersWithPermission(module, type);
	}

	
	public UserInfo createUser(UserInfo currentUser, UserInfo userInfo) {
		// userInfo.setPassword(MD5.MD5Encode(userInfo.getPassword()));
		userInfo.setUserID("user" + UUID.randomUUID().toString());
		try {
			userDao.saveUser(currentUser, userInfo);
		} catch (Exception e) {
			throw new UserException("Dao新增用户出错", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("新建用户成功：" + userInfo.getUserCnName());
		}
		return userInfo;
	}

	
	public UserInfo updateUser(UserInfo currentUser, UserInfo userInfo) {
		try {
			userDao.updateUser(currentUser, userInfo);
		} catch (Exception e) {
			throw new UserException("Dao更新用户出错", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug(currentUser.getLoginID() + "更新用户信息成功："
					+ userInfo.getUserCnName());
		}
		return userInfo;
	}

	
	public UserInfo getUserByLoginID(String loginID) {
		return userDao.findUserByLoginID(loginID);
	}

	
	public UserInfo queryUserByID(String userID) {
		return userDao.findUserByUserID(userID);
	}

	
	public boolean updatePassword(UserInfo currentUser, String userID,
			String password) {
		return false;
	}

	
	public void updateUserType(UserInfo user) {

	}

	
	public boolean deleteUser(String... userIds) {
		try {
			userDao.delete(userIds);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	
	public Object login(String loginID, String password) {
		if (loginID == null || loginID.trim().length() == 0) {
			return "用户名不能为空";
		}
		if (password == null || password.length() == 0) {
			return "密码不能为空";
		}

		UserInfo currentUser = userDao.findUserByLoginID(loginID);
		String encryptPassword = MD5.MD5Encode(loginID + password
				+ IUserConstants.VERIFY_CODE);
		// &&
		if (currentUser == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("用户名不存在：" + loginID);
			}
			return "用户名不存在";
		} else if (currentUser.getIsEnable() != 1) {
			if (logger.isDebugEnabled()) {
				logger.debug("用户名已经失效：" + loginID);
			}
			return "用户名已经失效";
		} else if (currentUser.getPassword() != null
				&& !currentUser.getPassword().equals(encryptPassword)) {
			// !currentUser.getPassword().equals(password)
			if (logger.isDebugEnabled()) {
				logger.debug("密码出错：" + loginID);
			}
			return "密码出错";
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("用户登录成功：" + loginID);
			}
			return currentUser;
		}

	}

	
	public boolean logout(UserInfo currentUser) {
		boolean isSuccess = true;
		try {
			User user = userDao.find(currentUser.getUserID());
			user.setLoginTime(DateUtil.nowTs());
			userDao.update(user);
		} catch (Exception e) {
			isSuccess = false;
			throw new UserException("用户退出系统出错", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("用户退出成功：" + currentUser.getLoginID());
		}
		return isSuccess;
	}

	
	public QueryResult<UserInfo> getAllDocumentary() {
		return userDao.getAllDocumentary();
	}

}
