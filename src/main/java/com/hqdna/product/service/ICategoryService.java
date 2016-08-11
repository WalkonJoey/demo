package com.hqdna.product.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hqdna.common.baseService.IService;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 产品分类服务类接口
 * 
 * 
 */
public interface ICategoryService extends IService {

	/**
	 * 新增产品分类
	 * 
	 */
	public int createNewCategory(UserInfo currentUser, CategoryVo category);

	/**
	 * 删除指定ID的产品分类
	 * 
	 */
	public boolean deleteCategoryByID(UserInfo currentUser, int categoryID);

	/**
	 * 更新指定ID的产品分类信息，ID包含在对象中
	 * 
	 * @return
	 * 
	 */
	public boolean updateCategoryByID(UserInfo currentUser, CategoryVo category);

	/**
	 * 给分类分配相应的属性组
	 * 
	 * @param currentUser
	 * @param categoryID
	 *            分类ID
	 * @param beforeAttrGroups
	 *            分配前的属性组ID列表
	 * @param afterAttrGroups
	 *            分配后的属性组ID列表
	 */
	public void assignAttrGroup4Category(UserInfo currentUser, int categoryID,
			List<Integer> beforeAttrGroups, List<Integer> afterAttrGroups);

	/**
	 * 获取所有产品分类
	 * 
	 * @return <产品分类编码,List<CategoryVo>子分类列表>
	 */
	public Map<Integer, List<CategoryVo>> getProductCategory();

	/**
	 * 获取产品的一级分类（大类）
	 * 
	 * @param order
	 * @param whereSqlMap
	 * @param pgInfo
	 * 
	 * @return <产品分类编码,List<CategoryVo>子分类列表>
	 */
	public QueryResult<CategoryVo> getProductLevelOneCategory(PageInfo pgInfo,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);

	/**
	 * 获取某种产品分类的所有属性以及可选属性值
	 * 
	 * @param categoryID
	 * @return <属性名对象,List<>属性值列表>
	 */
	public Map<AttrNameVo, List<AttrValueVo>> getAttrs4Category(int categoryID);

	/**
	 * 一个attrNameGroup对应一个分类，这里将一个AttrGroup和一个Category绑定
	 * 
	 * @param currentUser
	 * @param categoryID
	 * @param beforeGroupID
	 * @param afterGroupID
	 */
	public void assignAttrGroup4Category(UserInfo currentUser, int categoryID,
			int beforeGroupID, int afterGroupID);

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
	public CategoryVo getCategoryById(int categoryID);

	/**
	 * 通过categoryID获得Category的关联的属性ID
	 * 
	 * @param categoryID
	 * @return
	 */
	public List<Integer> getAttrsID4Category(int categoryID);

	/**
	 * 通过分类code获取分类
	 * @param categoryCodes
	 * @return
	 */
	public CategoryVo getCategoryByCode(int categoryCode);

	/**
	 * 通过导入Excel的第一行判断更新产品的哪一个字段
	 * @param dynProdAttrList
	 * @param currentUser
	 */
	public void updateDynCategoryAttr(List<Map<String, Object>> dynProdAttrList,
			UserInfo currentUser);

	/**
	 * 通过分类编码更新分类的申报名称
	 * @param currentUser
	 * @param categoryId
	 * @param declareName
	 */
	public void updDeclareName(UserInfo currentUser, int categoryId,
			String declareName);

	public void updateCategory(CategoryVo vo);

}