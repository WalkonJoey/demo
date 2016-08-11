package com.hqdna.product.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.vo.AttrGroupVo;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.user.vo.UserInfo;

public interface IProductAttrService {

	/**
	 * 获取产品列表
	 * 
	 * @param currentUser
	 *            当前用户
	 * @param pgInfo
	 *            分页对象,可为null
	 * @return
	 */
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo);

	/**
	 * 获取产品列表
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param pgInfo
	 *            分页对象 为NULL时则为查询所有记录
	 * @return
	 */
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap);

	/**
	 * 获取产品列表
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
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 通过attrID获取attribute对象
	 * 
	 * @param attrID
	 * @return
	 */
	public AttrNameVo getAttrNameById(int attrID);

	/**
	 * 根据条件获取属性值列表
	 * @param currentUser
	 * @param pgInfo
	 * @param whereSqlMap
	 * @param order
	 * @return
	 */
	public QueryResult<AttrValueVo> getAttrValue(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);


	/**
	 * 获取所有的属性值
	 * 
	 * @return
	 */
	public List<AttrValueVo> getAllAttrVal();

	/**
	 * 创建一个新的产品属性
	 * 
	 * @param currentUser
	 * @param attrNameVo
	 * @return
	 */
	public int createNewAttrName(UserInfo currentUser, AttrNameVo attrNameVo);

	/**
	 * 更新产品属性名信息
	 * 
	 * @param currentUser
	 * @param attrNameVo
	 * @return
	 */
	public int updateAttrName(UserInfo currentUser, AttrNameVo attrNameVo);

	/**
	 * 新增产品属性值
	 * 
	 * @param currentUser
	 * @param attrValue
	 */
	public int createNewAttrValue(UserInfo currentUser, AttrValueVo attrValue);

	/**
	 * 更新属性值信息
	 * 
	 * @param currentUser
	 * @param attrValueVo
	 * @return
	 */
	public int updateAttrValue(UserInfo currentUser, AttrValueVo attrValueVo);

	/**
	 * 将属性名称从属性组中删除
	 * 
	 * @param currentUser
	 * @param attrID
	 */
	public boolean canDeleteAttr(UserInfo currentUser, int attrID);

	/**
	 * 删除指定ID的属性名，首先鉴别是否存在已经关联，如果存在不能删除
	 * 
	 */
	public boolean deleteAttrNameByID(UserInfo currentUser, int attrID);

	/**
	 * 删除产品属性值
	 * 
	 * @param currentUser
	 * @param attrValueID
	 */
	public void deleteAttrValueByID(UserInfo currentUser, int attrValueID);

	/**
	 * 通过attrID获取attribute的可选属性值
	 * 
	 * @param attrID
	 * @return
	 */
	public List<AttrValueVo> getAttrValueById(int attrID);

	/**
	 * 通过导入Excel的第一行判断更新产品的哪一个字段
	 * @param dynProdAttrList
	 * @param currentUser
	 */
	public void updateDynProductAttr(List<Map<String, Object>> dynProdAttrList,
			UserInfo currentUser);

	/**
	 * 新增产品属性组
	 * 
	 * @return
	 * 
	 */
	public int createNewAttrGroup(UserInfo currentUser, AttrGroupVo attrGroup);

	/**
	 * 给属性组分配属性
	 * 
	 * @param currentUser
	 * @param beforeAttrNames
	 *            分配前的属性ID列表
	 * @param afterAttrNames
	 *            分配后的属性ID列表
	 */
	public void assignAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			List<Integer> beforeAttrNames, List<Integer> afterAttrNames);
}
