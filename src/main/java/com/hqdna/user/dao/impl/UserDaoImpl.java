package com.hqdna.user.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.bean.Department;
import com.hqdna.user.bean.User;
import com.hqdna.user.dao.IUserDao;
import com.hqdna.user.util.exception.UserException;
import com.hqdna.user.vo.DepartmentVo;
import com.hqdna.user.vo.UserInfo;


@Service("userDao")
@Transactional
public class UserDaoImpl extends DaoSupport<User> implements IUserDao {

	
	public UserInfo findUserByLoginID(String loginID) {
		List<User> users = getScrollData(-1, -1, "loginID=?",
				new Object[] { loginID }).getResultlist();
		if (logger.isDebugEnabled()) {
			logger.debug("findUser---------------==========================");
		}
		if (users != null && users.size() == 1 && users.get(0) != null) {
			UserInfo userInfo = new UserInfo();
			users.get(0).transferPo2Vo(userInfo, true);
			return userInfo;
		} else {
			return null;
		}
	}

	
	public QueryResult<UserInfo> queryAllUsers(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		List<UserInfo> list = new ArrayList<UserInfo>();

		QueryResult<UserInfo> r = new QueryResult<UserInfo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<User> result = null;
		if (sql == null) {
			result = getScrollData(firstindex, maxresult, null, null, order);
		} else {
			result = getScrollData(firstindex, maxresult, (String) sql[0],
					(Object[]) sql[1], order);
		}
		List<User> users = (List<User>) result.getResultlist();
		try {
			for (User u : users) {
				UserInfo target_user = new UserInfo();
				u.transferPo2Vo(target_user, true);
				list.add(target_user);
			}
		} catch (Exception e) {
			throw new UserException("Dao用户查询所有用户出错", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}

	
	public List<DepartmentVo> queryDepartmentList(UserInfo currentUser) {
		List<DepartmentVo> depList = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select dep from Department dep");
		List<Department> list = this.executeQuery(sb.toString(), null);
		if (list == null) {
			return null;
		}
		depList = new ArrayList<DepartmentVo>();

		for (Department dep : list) {
			DepartmentVo vo = new DepartmentVo();
			dep.transferPo2Vo(vo, true);
			depList.add(vo);
		}
		return depList;

	}

	
	public UserInfo findUserByUserID(String userID) {
		List<User> users = getScrollData(-1, -1, "UserID=?",
				new Object[] { userID }).getResultlist();
		if (logger.isDebugEnabled())
			logger.debug("findUser---------------==========================");

		if (users != null && users.get(0) != null) {
			UserInfo userInfo = new UserInfo();
			users.get(0).transferPo2Vo(userInfo, true);
			return userInfo;
		} else {
			return null;
		}
	}

	
	public void updateUser(UserInfo currentUser, UserInfo userInfo) {
		User user = find(userInfo.getUserID());
		user.transferPo2Vo(userInfo, false);
		if (currentUser != null) {
			User operator = new User();
			operator.transferPo2Vo(currentUser, false);
			user.setOperatorUser(operator);
		}
		if (userInfo.getDepartmentID() > 0) {
			Department depart = new Department();
			depart.setDepartID(userInfo.getDepartmentID());
			user.setDepartment(depart);
		}
		em.merge(user);

	}

	
	public void saveUser(UserInfo currentUser, UserInfo userInfo) {
		User user = new User();
		user.transferPo2Vo(userInfo, false);

		User creator = new User();
		creator.transferPo2Vo(currentUser, false);

		user.setCreatorUser(creator);
		user.setCreateDt(new Timestamp(System.currentTimeMillis()));

		user.setOperatorUser(creator);
		user.setOperateDt(new Timestamp(System.currentTimeMillis()));
		if (userInfo.getDepartmentID() > 0) {
			Department depart = new Department();
			depart.setDepartID(userInfo.getDepartmentID());
			user.setDepartment(depart);
		}
		save(user);
	}

	
	public QueryResult<UserInfo> getAllDocumentary() {
		String hql = "SELECT doc FROM User doc WHERE doc.userID IN(SELECT DISTINCT v.documentary.userID FROM Vendor v)";
		Query query = em.createQuery(hql);
		@SuppressWarnings("unchecked")
		List<User> userList = query.getResultList();
		if(userList!=null&&userList.size()>0){
			QueryResult<UserInfo> qr = new QueryResult<UserInfo>();
			List<UserInfo> userVoList = new ArrayList<UserInfo>();
			for (User user : userList) {
				UserInfo userInfo = new UserInfo();
				user.transferPo2Vo(userInfo, true);
				userVoList.add(userInfo);
			}
			qr.setResultlist(userVoList);
			qr.setTotalrecord(userList.size());
			return qr;
		}
		return null;
	}

}
