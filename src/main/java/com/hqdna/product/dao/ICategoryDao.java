package com.hqdna.product.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.bean.Category;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.vo.UserInfo;

public interface ICategoryDao extends DAO<Category> {

	/**
	 * 新增产品分类
	 * 
	 * @param category
	 * @return
	 */
	public int addCategory(UserInfo creator, CategoryVo category);

	/**
	 * 删除产品分类
	 * 
	 * @param currentUser
	 * @param categoryID
	 * @return
	 */
	public boolean deleteCategoryByID(int categoryID);

	/**
	 * 更新产品分类信息，包括分类名称，分类编码
	 * 
	 * @param operator
	 * @param category
	 */
	public boolean updateCategoryByID(UserInfo operator, CategoryVo category);

	public void updCategory(CategoryVo category);
	/**
	 * 为指定分类删除属性组
	 * 
	 * @param currentUser
	 * @param attrGroupID
	 * @param AttrNames
	 */
	public void deleteAttrGroup4Category(UserInfo currentUser, int categoryID,
			int... attrGroupIDs);


	/**
	 * 获取产品的所有产品分类类型
	 * 
	 * @return List<Category>分类列表
	 */
	public List<CategoryVo> queryProductCategory();

	/**
	 * 获取某种产品分类的所有属性以及可选属性值
	 * 
	 * @param categoryID
	 * @return <属性名对象,List<>属性值列表>
	 */
	public Map<AttrNameVo, List<AttrValueVo>> queryAttrs4Category(int categoryID);

	/**
	 * 通过categoryID获取对应category的AttrGroup
	 * 
	 * @param categoryID
	 * @return
	 */
	public Set<AttrGroup> queryAttrGroupByCategoryID(int categoryID);

	/**
	 * 通过categoryID获取category的信息
	 * 
	 * @param categoryID
	 * @return CategoryVo
	 */
	public CategoryVo queryCategoryById(int categoryID);

	/**
	 * 通过categoryID获得Category的关联的属性ID
	 * 
	 * @param categoryID
	 * @return
	 */
	public List<Integer> getAttrsID4Category(int categoryID);


	/**
	 * 通过分类code获取分类
	 * 
	 * @param categoryCode
	 * @return
	 */
	public CategoryVo getCategoryByCode(int categoryCode);

	/**
	 * 获取一级分类
	 * @param startIndex
	 * @param rows
	 * @param whereSqlMap
	 * @param order
	 * @return
	 */
	public QueryResult<CategoryVo> getProductLevelOneCategory(int startIndex,
			int rows, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 更新category excel中提供的字段
	 * @param dynProdAttrMap
	 * @param currentUser
	 */
	public void updateDynCategoryAttr(Map<String, Object> dynProdAttrMap,
			UserInfo currentUser);

	/**
	 * 通过categoryID更新分类的申报名称
	 * @param currentUser
	 * @param categoryId
	 * @param declareName
	 */
	public void updDeclareName(UserInfo currentUser, int categoryId,
			String declareName);

}
