package com.hqdna.product.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.bean.AttrName;
import com.hqdna.product.bean.Category;
import com.hqdna.product.bean.ProductDynamicAttr;
import com.hqdna.product.dao.IProductAttrDao;
import com.hqdna.product.util.exception.ProductException;
import com.hqdna.product.vo.AttrGroupVo;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.user.bean.User;
import com.hqdna.user.vo.UserInfo;

@Component("productAttrDao")
@Transactional
public class ProductAttrDaoImpl extends DaoSupport<AttrName> implements IProductAttrDao{
	
	
	/**
	 * 查询属性信息
	 * 
	 * @param attrID
	 * @return
	 */
	private AttrName queryAttrName(int attrID) {
		StringBuilder tsb = new StringBuilder();
		tsb.append("select an from AttrName an where an.attrID=?1");
		List<AttrName> ans = this.executeQuery(tsb.toString(),
				new Object[] { attrID });
		return ans.get(0);
	}
	/**
	 * 查询属性组信息
	 * 
	 * @param attrGroupID
	 */
	private AttrGroup queryAttrGroup(int attrGroupID) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ag from AttrGroup ag where ag.attrGroupID=?1");
		List<AttrGroup> ags = this.<AttrGroup> executeQuery(sb.toString(),
				new Object[] { attrGroupID });
		return ags.get(0);
	}
	
	/**
	 * 查询分类信息
	 * 
	 * @param categoryID
	 */
	private Category queryCategory(int categoryID) {
		StringBuilder sb = new StringBuilder();
		sb.append("select cat from Category cat where cat.categoryID=?1");
		List<Category> cats = this.<Category> executeQuery(sb.toString(),
				new Object[] { categoryID });
		return cats.get(0);
	}
	
	
	public QueryResult<AttrNameVo> queryAllAttrNames(int startIndex, int rows,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		QueryResult<AttrNameVo> r = new QueryResult<AttrNameVo>();

		List<AttrNameVo> list = new ArrayList<AttrNameVo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<AttrName> result = null;
		if (sql == null) {
			result = getScrollData(startIndex, rows, null, null, order);
		} else {
			result = getScrollData(startIndex, rows, (String) sql[0],
					(Object[]) sql[1], order);
		}

		List<AttrName> products = (List<AttrName>) result.getResultlist();
		try {
			for (AttrName p : products) {
				AttrNameVo v_p = new AttrNameVo();
				p.transferPo2Vo(v_p, true);
				list.add(v_p);
			}
		} catch (Exception e) {
			throw new ProductException("产品查询Dao出错了", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}
	
	
	
	public AttrNameVo getAttrNameById(int attrID) {
		AttrName an = queryAttrName(attrID);
		AttrNameVo anv = new AttrNameVo();
		an.transferPo2Vo(anv, true);
		return anv;
	}
	
	
	public int addAttrName(UserInfo currentUser, AttrNameVo attrName) {
		AttrName an = new AttrName();
		an.transferPo2Vo(attrName, false);
		em.persist(an);
		return an.getAttrID();
	}
	
	
	
	public int updateAttrName(UserInfo currentUser, AttrNameVo attrNameVo) {
		User user = new User();
		user.transferPo2Vo(currentUser, false);
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("update AttrName an set an.attrName = ?1,an.attrCode=?2,an.comment = ?3 where an.attrID = ?4");
			Object[] params = new Object[] { attrNameVo.getAttrName(),
					attrNameVo.getAttrCode(), attrNameVo.getComment(),
					attrNameVo.getAttrID() };
			int num = executeUpdate(sb.toString(), params);
			return num;
		} catch (Exception e) {
			throw new ProductException("Dao产品更新出错", e);
		}
	}
	
	
	public boolean canDeleteAttr(UserInfo currentUser, int attrID) {
		AttrName an = queryAttrName(attrID);
		if (an.getAttrGroups().size() > 0) {// 如果关联了分组 就不能删除
			return false;
		} else {
			return true;
		}
	}
	
	
	public boolean deleteAttrNameByID(UserInfo currentUser, int attrID) {
		AttrName attrName = queryAttrName(attrID);
		List<AttrGroup> ags = attrName.getAttrGroups();
		List<ProductDynamicAttr> dynamicAttrs = attrName.getDynamicAttrs();
		if ((ags == null || ags.isEmpty())
				&& (dynamicAttrs == null || dynamicAttrs.isEmpty())) {
			em.remove(attrName);
			return true;
		}
		return false;
	}

	
	public void updateDynProductAttr(Map<String, Object> dynProdAttrMap,
			UserInfo currentUser) {
		Set<String> ketSet = dynProdAttrMap.keySet();
		Iterator<String> iterator = ketSet.iterator();
		Object[] params = new Object[ketSet.size()+2];
		if(iterator.hasNext()&&ketSet.contains("skuNo")){
			StringBuilder hql = new StringBuilder();
			String attrName = iterator.next().trim();
			if("skuNo".equals(attrName)){
				attrName = iterator.next().trim();
			}
			params[0] = dynProdAttrMap.get(attrName);
			hql.append("UPDATE TBL_PRODUCT SET "+attrName.trim()+" = ?1");
			int i = 2;
			while(iterator.hasNext()){
				attrName = iterator.next();
				if("skuNo".equals(attrName)){
					continue;
				}
				params[i-1] = dynProdAttrMap.get(attrName);
				hql.append(","+iterator.next().trim()+" = ?"+i);
				i++;
				
			}
			params[i-1] = currentUser.getUserID();
			hql.append(",operator = ?"+i);
			i++;
			params[i-1] = DateUtil.nowTs();
			hql.append(",operateDt = ?"+i);
			i++;
			params[i-1] = dynProdAttrMap.get("skuNo");
			hql.append(" WHERE skuNo = ?"+i);
			//Object[] params = new Object[]{skuNo,purchasePrice,currentUser.getUserID(),DateUtil.nowTs()};
			//hql.append("UPDATE TBL_PRODUCT SET purchasePrice = ?2,operator = ?3,operateDt = ?4 WHERE skuNo = ?1");
			executeByNativeSQL(hql.toString(), params);
		}
	}
	
	
	public void addAttrGroup4Category(UserInfo currentUser, int categoryID,
			int... attrGroupIDs) {
		Category category = queryCategory(categoryID);
		List<AttrGroup> list = new ArrayList<AttrGroup>(attrGroupIDs.length);
		for (int attrGroupID : attrGroupIDs) {
			AttrGroup ag = queryAttrGroup(attrGroupID);
			list.add(ag);
		}
		Set<AttrGroup> attrGroup = category.getAttrGroups();
		if (attrGroup == null) {
			attrGroup = new HashSet<AttrGroup>();
			category.setAttrGroups(attrGroup);
		}
		attrGroup.addAll(list);
		em.merge(category);

	}
	
	
	public void deteleAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			int... attrNames) {
		AttrGroup ag = queryAttrGroup(attrGroupID);

		List<AttrName> list = new ArrayList<AttrName>(attrNames.length);
		for (int anID : attrNames) {
			AttrName an = queryAttrName(anID);
			list.add(an);
		}
		ag.getAttrNames().removeAll(list);
		em.merge(ag);

	}
	
	
	public void addAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			int... attrNames) {

		AttrGroup ag = queryAttrGroup(attrGroupID);
		if (attrNames == null) {
			ag.setAttrNames(null);
		} else {
			List<AttrName> list = new ArrayList<AttrName>(attrNames.length);
			for (int anID : attrNames) {
				AttrName an = queryAttrName(anID);
				list.add(an);
			}

			List<AttrName> attrs = ag.getAttrNames();
			if (attrs == null) {
				attrs = new ArrayList<AttrName>();
				ag.setAttrNames(attrs);
			}
			attrs.addAll(list);
		}
		em.merge(ag);
	}
	
	
	public int addAttrGroup(UserInfo currentUser, AttrGroupVo attrGroup) {
		AttrGroup ag = new AttrGroup();
		ag.transferPo2Vo(attrGroup, false);
		em.persist(ag);
		return ag.getAttrGroupID();
	}
}
