package com.hqdna.product.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrValue;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.user.vo.UserInfo;

public interface IProdAttrValDao extends DAO<AttrValue>{
	
	/**
	 * 通过条件获取所有的属性值列表
	 * @param startIndex
	 * @param rows
	 * @param whereSqlMap
	 * @param order
	 * @return
	 */
	public QueryResult<AttrValueVo> queryAllAttrValues(int startIndex, int rows,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);

	/**
	 * 获取所有的属性值
	 * 
	 * @return
	 */
	public List<AttrValueVo> getAllAttrVal();

	/**
	 * 新增属性值
	 * 
	 * @param currentUser
	 * @param attrValue
	 * @return
	 */
	public int addAttrValue(UserInfo currentUser, AttrValueVo attrValue);

	/**
	 * 更新属性值信息
	 * 
	 * @param currentUser
	 * @param attrValueVo
	 * @return
	 */
	public int updateAttrValue(UserInfo currentUser, AttrValueVo attrValueVo);

	/**
	 * 删除产品属性值
	 * 
	 * @param currentUser
	 * @param attrValue
	 */
	public boolean deleteAttrValueByID(UserInfo currentUser, int attrValueID);

	/**
	 * 通过attrID获取attribute的可选属性值
	 * 
	 * @param attrID
	 * @return
	 */
	public List<AttrValueVo> getAttrValueById(int attrID);
}
