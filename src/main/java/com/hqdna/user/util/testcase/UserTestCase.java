package com.hqdna.user.util.testcase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hqdna.common.BaseTestCase;
import com.hqdna.common.IGlobalConstants;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.bean.User;
import com.hqdna.user.dao.IRoleDao;
import com.hqdna.user.dao.IUserDao;
import com.hqdna.user.service.IUserService;
import com.hqdna.user.vo.UserInfo;


public class UserTestCase extends BaseTestCase implements IGlobalConstants {
	private static IUserDao userDao;
	private static IRoleDao roleDao;
	private static IUserService userService;

	/**
	 * 注：不要覆盖setUpBeforeClass()
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClassChild() throws Exception {
		userDao = (IUserDao) cxt.getBean("userDao");
		roleDao = (IRoleDao) cxt.getBean("roleDao");
		userService = (IUserService) cxt.getBean("userService");
	}

	/**
	 * 测试保存 condition: 实体类无参构造 PK自动生成
	 */
	@Test
	public void testSave() {
		User entity = new User();
		entity.setUserID("user" + UUID.randomUUID().toString());
		entity.setLoginID("testuser2");
		entity.setUserCnName("testuser2");
		userDao.save(entity);

		System.out.println("=============>保存成功...请往数据库查看");
	}

	/**
	 * 测试查询--查询单个对象 condition: 数据库表中需存在 PK值(_id)
	 */
	@Test
	public void testQuerySingle() {
		/** 根据PK:_id,查询单个对象信息 */
		Integer _id = 1;
		User entity = userDao.find(_id);
		System.out.println(entity);
	}

	/**
	 * 测试查询--查询对象的全部记录
	 */
	@Test
	public void testQueryAll() {
		/** 查询全部 */
		QueryResult<User> qr = userDao.getScrollData();
		if (qr.getTotalrecord() > 0) {
		} else
			System.out.println("数据库表中没有任何数据...");
	}

	/**
	 * 测试查询--查询对象的全部记录
	 */
	@Test
	public void testQueryAll2() {
		/** 查询全部 */
		PageInfo pgInfo = new PageInfo();
		QueryResult<UserInfo> qr = userService.getUserList(null, pgInfo);
		Assert.assertEquals(5, qr.getResultlist().size());
	}

	/**
	 * 测试查询--根据条件进行查询 condition: 根据实际情况完善查询条件
	 */
	@Test
	public void testQueryByCondition() {
		/** 分页 */
		int startRow = 1;
		int pageSize = 3;

		/** 排序 */
		String orderColName = ""; // 排序的列名
		String orderHow = ""; // 升序还是降序 DESC/ASC
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put(orderColName, orderHow);

		/** 查询条件 */
		Object[] queryParams = null;
		StringBuffer strbuf = new StringBuffer(" 1=1 ");
		List<Object> objList = new ArrayList<Object>();

		// strbuf.append(" and o.xxxx = ?1"); //第一个查询条件
		// strbuf.append(" and o.xxxx = ?2"); //第二个查询条件
		// objList.add("xxx"); //第一个查询条件占位符的值
		// objList.add("xxx"); //第二个查询条件占位符的值

		if (objList.size() > 0)
			queryParams = objList.toArray();

		String wherejpql = strbuf.toString();

		QueryResult<User> qr = userDao.getScrollData(startRow, pageSize,
				wherejpql, queryParams, orderby);
		if (qr.getTotalrecord() > 0) {
			for (User entity : qr.getResultlist()) {
				 System.out.println(entity);
			}
		} else
			System.out.println("数据库表中没有任何数据...");
	}

	/**
	 * 测试更新 condition: 数据库表中需存在 PK值(_id)
	 */
	@Test
	public void testUpdate() {
		Integer _id = 1;
		User entity = userDao.find(_id);
		// System.out.println("更新前：" + entity...);

		// entity.setXX(xx);

		userDao.update(entity);
		System.out.println("=============>更新成功...");

		entity = userDao.find(_id);
		// System.out.println("更新后：" + entity...);
	}

	/**
	 * 测试删除 condition: 数据库表中需存在 PK值(_id)
	 */
	@Test
	public void testDelete() {
		Integer _id = 1;
		userDao.delete(_id);
		System.out.println("=============>删除成功...");
	}

	/**
	 * 测试批量删除 condition: 数据库表中需存在 PK值(ids)
	 */
	@Test
	public void testBatchDelete() {
		String ids = "1,2,3,";// 先确保该id在数据库表中
		String[] idsArr = ids.split(",");
		Integer[] _ids = new Integer[idsArr.length];
		for (int i = 0; i < _ids.length; i++)
			_ids[i] = Integer.parseInt(idsArr[i]);

		System.out.println("=============>批量删除成功...");
	}

}
