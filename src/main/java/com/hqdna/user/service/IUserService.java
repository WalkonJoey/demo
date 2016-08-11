package com.hqdna.user.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.PermType;
import com.hqdna.user.vo.DepartmentVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 用户服务接口
 *
 */
public interface IUserService {

	/**
	 * 获取用户列表
	 * 
	 * @param currentUser
	 *            当前用户
	 * @param pgInfo
	 *            分页对象,可为null
	 * @return
	 */
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo);

	/**
	 * 获取用户列表
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param pgInfo
	 *            分页对象 为NULL时则为查询所有记录
	 * @return
	 */
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap);

	/**
	 * 获取用户列表
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
	public QueryResult<UserInfo> getUserList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 获取拥有指定模块指定权限的所有用户
	 * 
	 * @param moduleName
	 * @param type
	 * @return
	 */
	public List<UserInfo> getUserListWithPermission(Module module, PermType type);

	/**
	 * 通过userID查询对象用户信息
	 * 
	 * @param user
	 * @return
	 */
	public UserInfo queryUserByID(String userID);

	/**
	 * 通过loginID查询对象用户信息
	 * 
	 * @param user
	 * @return
	 */
	public UserInfo getUserByLoginID(String loginID);

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 */
	public UserInfo createUser(UserInfo currentUser, UserInfo user);

	/**
	 * 编辑更新
	 * 
	 * @param currentUser
	 * @param user
	 * @return
	 */
	public UserInfo updateUser(UserInfo currentUser, UserInfo user);

	/**
	 * 
	 * 更新用户密码
	 */
	public boolean updatePassword(UserInfo currentUser, String userID,
			String password);

	/**
	 * 获取所有部门列表
	 * 
	 * @param currentUser
	 * @param pgInfo
	 * @param whereSqlMap
	 * @return
	 */
	public List<DepartmentVo> getDepartmentList(UserInfo currentUser);

	/**
	 * 
	 * @Title: updateUserType
	 * @Description: 修改用户类型
	 * @param user
	 *            void
	 * @throws
	 */
	public void updateUserType(UserInfo user);

	/**
	 * 
	 * @param userIds
	 * @return
	 */
	public boolean deleteUser(String... userIds);

	/**
	 * 用户登录
	 * 
	 * @param loginID
	 * @param password
	 * @return
	 */
	public Object login(String loginID, String password);

	/**
	 * 用户退出
	 * 
	 * @param currentUser
	 * @return
	 */
	public boolean logout(UserInfo currentUser);

	/**
	 * 获取所有的跟单人员
	 * @return
	 */
	public QueryResult<UserInfo> getAllDocumentary();

}
