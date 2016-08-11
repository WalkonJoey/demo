package com.hqdna.user.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.bean.User;
import com.hqdna.user.vo.DepartmentVo;
import com.hqdna.user.vo.UserInfo;

public interface IUserDao extends DAO<User> {

	/**
	 * 通过userID获取用户信息
	 * 
	 * @param userID
	 * @return
	 */
	public UserInfo findUserByUserID(String userID);

	/**
	 * 通过userID获取用户信息
	 * 
	 * @param userID
	 * @return
	 */
	public UserInfo findUserByLoginID(String loginID);

	/**
	 * 查询用户  分页
	 * @return
	 */
	public QueryResult<UserInfo> queryAllUsers(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);
	
	/**
	 * 查询部门列表  
	 * @return
	 */
	public List<DepartmentVo> queryDepartmentList(UserInfo currentUser);
	/**
	 * 保存用户信息
	 * 
	 * @return
	 */
	public void saveUser(UserInfo currentUser, UserInfo userInfo);

	/**
	 * 更新用户信息
	 * 
	 * @return
	 */
	public void updateUser(UserInfo currentUser, UserInfo userInfo);

	/**
	 * 获取所有的跟单人员
	 * @return
	 */
	public QueryResult<UserInfo> getAllDocumentary();
}
