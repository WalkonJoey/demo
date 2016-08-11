package com.hqdna.product.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.bean.AttrName;
import com.hqdna.product.bean.AttrValue;
import com.hqdna.product.bean.Category;
import com.hqdna.product.dao.ICategoryDao;
import com.hqdna.product.util.exception.ProductException;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.bean.User;
import com.hqdna.user.vo.UserInfo;

@Component("categoryDao")
@Transactional
public class CategoryDaoImpl extends DaoSupport<Category> implements ICategoryDao {
	
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
	public int addCategory(UserInfo currentUser, CategoryVo category) {
		Category targ_cat = new Category();
		targ_cat.transferPo2Vo(category, false);

		if (currentUser != null) {
			User creator = new User();
			creator.transferPo2Vo(currentUser, false);
			targ_cat.setCreatorUser(creator);
			targ_cat.setOperatorUser(creator);
		}
		// targ_cat.setCategoryCode(getMaxCategoryCode() + 1);//
		// 获取category表里面categoryCode最大的，然后加1赋值给新增的category
		em.persist(targ_cat);
		return targ_cat.getCategoryID();
	}


	
	public boolean updateCategoryByID(UserInfo operator, CategoryVo category) {
		StringBuilder sb = new StringBuilder();
		sb.append("update Category cat set cat.categoryCode=?1,cat.categoryParentID=?2,cat.categoryCnName=?3,cat.categoryEnName=?4,cat.operatorUser.userID=?5,cat.operateDt=?6 where cat.categoryID=?7");
		Object[] params = new Object[] { category.getCategoryCode(),
				category.getCategoryParentID(), category.getCategoryCnName(),
				category.getCategoryENName(), category.getOperatorID(),
				DateUtil.nowTs(), category.getCategoryID() };
		int num = executeUpdate(sb.toString(), params);
		return num > 0 ? true : false;
	}


	
	public void deleteAttrGroup4Category(UserInfo currentUser, int categoryID,
			int... attrGroupIDs) {
		Category category = queryCategory(categoryID);
		List<AttrGroup> list = new ArrayList<AttrGroup>(attrGroupIDs.length);
		for (int attrGroupID : attrGroupIDs) {
			AttrGroup ag = queryAttrGroup(attrGroupID);
			list.add(ag);
		}
		category.getAttrGroups().removeAll(list);
		em.merge(category);

	}
	
	
	public List<CategoryVo> queryProductCategory() {
		List<CategoryVo> cates = new ArrayList<CategoryVo>();
		String sql = "select ca from Category ca";
		List<Object> list = executeQuery(sql, null);
		for (Object obj : list) {
			CategoryVo ct = new CategoryVo();
			((Category) obj).transferPo2Vo(ct, true);
			cates.add(ct);
		}
		return cates;
	}

	
	public Map<AttrNameVo, List<AttrValueVo>> queryAttrs4Category(int categoryID) {
		Map<AttrNameVo, List<AttrValueVo>> attrs = new HashMap<AttrNameVo, List<AttrValueVo>>();
		String sql = "select cat from Category cat where cat.categoryID=?1";
		List<Object> list = executeQuery(sql, new Object[] { categoryID });
		Category cat = (Category) list.get(0);
		AttrGroup[] ags = cat.getAttrGroups().toArray(new AttrGroup[] {});
		for (AttrGroup ag : ags) {// ？ 如果遍历Category拥有的组的话，那么不同的组里面有相同的属性怎么办？
			List<AttrName> li = ag.getAttrNames();
			for (AttrName attr : li) {
				AttrNameVo anvo = new AttrNameVo();
				attr.transferPo2Vo(anvo, true);
				List<AttrValueVo> avList = new ArrayList<AttrValueVo>();
				for (AttrValue av : attr.getAttrValues()) {
					AttrValueVo avv = new AttrValueVo();
					av.transferPo2Vo(avv, true);
					avList.add(avv);
				}
				attrs.put(anvo, avList);
			}
		}
		return attrs;
	}

	
	public CategoryVo queryCategoryById(int categoryID) {
		Category category = queryCategory(categoryID);
		CategoryVo ct = new CategoryVo();
		category.transferPo2Vo(ct, true);
		return ct;
	}

	
	public List<Integer> getAttrsID4Category(int categoryID) {
		String sql = "select cat from Category cat where cat.categoryID=?1";
		List<Object> list = executeQuery(sql, new Object[] { categoryID });
		List<Integer> attrids = new ArrayList<Integer>();
		Set<Integer> attridsSet = new HashSet<Integer>();// 通过set来过滤重复的项
		Category cat = (Category) list.get(0);
		AttrGroup[] ags = cat.getAttrGroups().toArray(new AttrGroup[] {});
		for (AttrGroup ag : ags) {
			List<AttrName> li = ag.getAttrNames();
			for (AttrName attrName : li) {
				attridsSet.add(attrName.getAttrID());
			}
		}
		attrids.addAll(attridsSet);
		return attrids;
	}
	
	@SuppressWarnings("unchecked")
	
	public QueryResult<CategoryVo> getProductLevelOneCategory(int startIndex,
			int rows, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		Query query = null;
		QueryResult<Category> qr = new QueryResult<Category>();
		QueryResult<CategoryVo> returnQr = new QueryResult<CategoryVo>();
		if (whereSqlMap != null) {
			Object[] sql = transferSql(whereSqlMap);
			String wherejpql = (String) sql[0];
			query = em.createQuery("select o from Category o "
					+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
							: "where " + wherejpql));
			setQueryParams(query, (Object[]) sql[1]);
			if (startIndex != -1 && rows != -1)
				query.setFirstResult(startIndex).setMaxResults(rows);
			qr.setResultlist(query.getResultList());
			query = em.createQuery("select count(*) from Category o "
					+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
							: "where " + wherejpql));
			setQueryParams(query, (Object[]) sql[1]);
			qr.setTotalrecord((Long) query.getSingleResult());
		} else {
			query = em
					.createQuery("select count(*) from Category o where o.categoryParentID = 0");
			qr.setResultlist(query.getResultList());
			qr.setTotalrecord((Long) query.getSingleResult());
		}
		List<Category> categoryList = (List<Category>) qr.getResultlist();
		List<CategoryVo> categoryVoList = new ArrayList<CategoryVo>();
		try {
			for (Category cate : categoryList) {
				CategoryVo target_cate = new CategoryVo();
				cate.transferPo2Vo(target_cate, true);
				categoryVoList.add(target_cate);
			}
			returnQr.setResultlist(categoryVoList);
			returnQr.setTotalrecord(qr.getTotalrecord());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnQr;

	}
	
	
	public CategoryVo getCategoryByCode(int categoryCode) {
		try {
			StringBuilder tsb = new StringBuilder();
			tsb.append("select av from Category av where av.categoryCode=?1");
			List<Category> avs = this.executeQuery(tsb.toString(),
					new Object[] { categoryCode });
			if (avs != null && avs.size() > 0) {
				CategoryVo avv = new CategoryVo();
				Category av = avs.get(0);
				av.transferPo2Vo(avv, true);
				return avv;
			}
		} catch (Exception e) {
			throw new ProductException("通过分类code获取分类出错!", e);
		}
		return null;
	}
	
	public boolean deleteCategoryByID(int categoryID) {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 通过categoryID获取对应category的AttrGroup
	 * 
	 * @param categoryID
	 * @return
	 */
	public Set<AttrGroup> queryAttrGroupByCategoryID(int categoryID) {
		Category category = queryCategory(categoryID);
		CategoryVo ct = new CategoryVo();
		category.transferPo2Vo(ct, true);
		Set<AttrGroup> agSet = category.getAttrGroups();
		return agSet;
	}

	
	public void updateDynCategoryAttr(Map<String, Object> dynProdAttrMap,
			UserInfo currentUser) {
		Set<String> ketSet = dynProdAttrMap.keySet();
		Iterator<String> iterator = ketSet.iterator();
		Object[] params = new Object[ketSet.size()+2];
		if(iterator.hasNext()&&ketSet.contains("categoryCode")){
			StringBuilder hql = new StringBuilder();
			String attrName = iterator.next().trim();
			if("categoryCode".equals(attrName)){
				attrName = iterator.next().trim();
			}
			params[0] = dynProdAttrMap.get(attrName);
			hql.append("UPDATE TBL_PRODUCT_CATEGORY SET "+attrName.trim()+" = ?1");
			int i = 2;
			while(iterator.hasNext()){
				attrName = iterator.next();
				if("categoryCode".equals(attrName)){
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
			params[i-1] = Integer.parseInt(dynProdAttrMap.get("categoryCode").toString());
			hql.append(" WHERE categoryCode = ?"+i);
			executeByNativeSQL(hql.toString(), params);
		}
	}

	
	public void updDeclareName(UserInfo currentUser, int categoryId,
			String declareName) {
		StringBuilder hql = new StringBuilder();
		Object[] params = new Object[]{declareName,currentUser.getUserID(),DateUtil.nowTs(),categoryId};
		hql.append("UPDATE TBL_PRODUCT_CATEGORY SET declareName = ?1,operator = ?2,operateDt = ?3 WHERE categoryCode = ?4");
		executeByNativeSQL(hql.toString(), params);
	}

	
	public void updCategory(CategoryVo category) {
		Category ca = new Category();
		
		ca.transferPo2Vo(category, false);
		em.merge(ca);
	}

}
