package com.hqdna.product.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.dao.ICategoryDao;
import com.hqdna.product.dao.IProdAttrValDao;
import com.hqdna.product.dao.IProductAttrDao;
import com.hqdna.product.service.ICategoryService;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.vo.UserInfo;

@Service("categoryService")
public class CategoryServiceImpl extends AbstractBase implements
ICategoryService {

	@Resource(name = "categoryDao")
	private ICategoryDao categoryDao;
	@Resource(name = "prodAttrValDao")
	private IProdAttrValDao prodAttrValDao;
	@Resource(name = "productAttrDao")
	private IProductAttrDao productAttrDao;

	
	public Map<Integer, List<CategoryVo>> getProductCategory() {
		Map<Integer, List<CategoryVo>> cateMap = new TreeMap<Integer, List<CategoryVo>>();
		List<CategoryVo> cats = categoryDao.queryProductCategory();
		for (CategoryVo cat : cats) {
			Integer parentID = cat.getCategoryParentID();
			if (parentID == null || cateMap.get(parentID) == null) {
				List<CategoryVo> list = new ArrayList<CategoryVo>();
				cateMap.put(parentID, list);
			}
			cateMap.get(parentID).add(cat);
		}
		return cateMap;
	}

	
	public QueryResult<CategoryVo> getProductLevelOneCategory(PageInfo pgInfo,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return categoryDao.getProductLevelOneCategory(-1, -1, whereSqlMap,
					order);
		} else {
			return categoryDao.getProductLevelOneCategory(
					pgInfo.getStartIndex(), pgInfo.getRows(), whereSqlMap,
					order);
		}
		/*
		 * List<CategoryVo> levelOneList = new ArrayList<CategoryVo>();
		 * List<CategoryVo> cats = productDao.queryProductCategory(); for
		 * (CategoryVo cat : cats) { Integer parentID =
		 * cat.getCategoryParentID(); if (parentID == 0) {
		 * levelOneList.add(cat); } } return levelOneList;
		 */
	}

	
	public Map<AttrNameVo, List<AttrValueVo>> getAttrs4Category(int categoryID) {
		return categoryDao.queryAttrs4Category(categoryID);
	}

	@Transactional
	
	public int createNewCategory(UserInfo currentUser, CategoryVo category) {
		if (category == null) {
			return -1;
		}
		return categoryDao.addCategory(currentUser, category);
	}

	
	public boolean deleteCategoryByID(UserInfo currentUser, int categoryID) {
		// 查询当前是否有此产品分类的产品，如果没有则可以删除，同时删除分配的属性组
		return categoryDao.deleteCategoryByID(categoryID);
	}


	
	public boolean updateCategoryByID(UserInfo currentUser, CategoryVo category) {
		return categoryDao.updateCategoryByID(currentUser, category);
	}

	@Transactional
	
	public void assignAttrGroup4Category(UserInfo currentUser, int categoryID,
			List<Integer> beforeAttrGroups, List<Integer> afterAttrGroups) {
		for (Iterator<Integer> it = beforeAttrGroups.iterator(); it.hasNext();) {
			Integer attrID = it.next();
			if (afterAttrGroups.contains(attrID)) {
				it.remove();
				afterAttrGroups.remove(attrID);
			}
		}
		categoryDao.deleteAttrGroup4Category(currentUser, categoryID,
				toIntArray(beforeAttrGroups));
		productAttrDao.addAttrGroup4Category(currentUser, categoryID,
				toIntArray(afterAttrGroups));
	}


	/**
	 * 将Integer列表转化为int数组
	 * 
	 * @param inputs
	 * @return
	 */
	private int[] toIntArray(List<Integer> inputs) {
		if (inputs == null) {
			return null;
		}
		int[] outs = new int[inputs.size()];
		for (int i = 0; i < inputs.size(); i++) {
			outs[i] = inputs.get(i);
		}
		return outs;
	}

	/*
	 *  public List<AttrNameVo> getAllAttrName() { return
	 * productDao.getAllAttrName(); }
	 */

	
	public void assignAttrGroup4Category(UserInfo currentUser, int categoryID,
			int beforeGroupID, int afterGroupID) {
		if (afterGroupID < 0) {
			return;
		}
		int[] ids = new int[1];
		ids[0] = afterGroupID;
		productAttrDao.addAttrGroup4Category(currentUser, categoryID, ids);

	}

	
	public Set<AttrGroup> queryAttrGroupByCategoryID(int categoryID) {
		return categoryDao.queryAttrGroupByCategoryID(categoryID);
	}

	
	public CategoryVo getCategoryById(int categoryID) {
		return categoryDao.queryCategoryById(categoryID);
	}

	
	public List<Integer> getAttrsID4Category(int categoryID) {
		return categoryDao.getAttrsID4Category(categoryID);
	}


	/**
	 * 初始化
	 */

	
	public void init() {
	}

	
	public CategoryVo getCategoryByCode(int categoryCode) {
		return categoryDao.getCategoryByCode(categoryCode);
	}


	
	public void updateDynCategoryAttr(List<Map<String, Object>> dynProdAttrList,
			UserInfo currentUser) {
		for (Map<String,Object> dynProdAttrMap : dynProdAttrList) {
			categoryDao.updateDynCategoryAttr(dynProdAttrMap,currentUser);
		}
		
	}

	
	public void updDeclareName(UserInfo currentUser, int categoryId,
			String declareName) {
		categoryDao.updDeclareName(currentUser,categoryId,declareName);
		
	}

	
	public void updateCategory(CategoryVo vo) {
		categoryDao.updCategory(vo);
		
	}


}
