package com.hqdna.product.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrName;
import com.hqdna.product.vo.AttrGroupVo;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.user.vo.UserInfo;

public interface IProductAttrDao  extends DAO<AttrName> {
	/**
	 * 
	 * @param startIndex
	 * @param rows
	 * @param whereSqlMap
	 * @param order
	 * @return
	 */
	public QueryResult<AttrNameVo> queryAllAttrNames(int startIndex, int rows,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);

	/**
	 * 通过attrID获取attribute对象
	 * 
	 * @param attrID
	 * @return
	 */
	public AttrNameVo getAttrNameById(int attrID);

	/**
	 * 新增属性
	 * 
	 * @param currentUser
	 * @param attrName
	 * @return
	 */
	public int addAttrName(UserInfo currentUser, AttrNameVo attrName);

	/**
	 * 更新产品属性名信息
	 * 
	 * @param currentUser
	 * @param attrNameVo
	 * @return
	 */
	public int updateAttrName(UserInfo currentUser, AttrNameVo attrNameVo);

	/**
	 * 将属性名称从属性组中删除
	 * 
	 * @param currentUser
	 * @param attrID
	 */
	public boolean canDeleteAttr(UserInfo currentUser, int attrID);


	/**
	 * 删除指定的属性名
	 * 
	 * @param currentUser
	 * @param attrID
	 * @return
	 */
	public boolean deleteAttrNameByID(UserInfo currentUser, int attrID);

	/**
	 * 更新产品属性，通过map的key判断更新哪些属性
	 * @param dynProdAttrMap
	 * @param currentUser
	 */
	public void updateDynProductAttr(Map<String, Object> dynProdAttrMap,
			UserInfo currentUser);

	/**
	 * 为指定分类增加属性组
	 * 
	 * @param currentUser
	 * @param attrGroupID
	 * @param AttrNames
	 */
	public void addAttrGroup4Category(UserInfo currentUser, int categoryID,
			int... attrGroupIDs);

	/**
	 * 从指定属性组删除属性
	 * 
	 * @param currentUser
	 * @param attrGroupID
	 * @param AttrNames
	 */
	public void deteleAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			int... attrNames);

	/**
	 * 为指定属性组增加属性
	 * 
	 * @param currentUser
	 * @param attrGroupID
	 * @param AttrNames
	 */
	public void addAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			int... attrNames);

	/**
	 * 新增属性组
	 * 
	 * @param currentUser
	 * @param attrGroup
	 * @return
	 */
	public int addAttrGroup(UserInfo currentUser, AttrGroupVo attrGroup);



}
