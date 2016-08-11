package com.hqdna.product.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseService.IService;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.Product;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.ProductDynamicAttrVo;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 产品服务类接口
 * 
 */
public interface IProductService extends IService {

	/**
	 * 获取产品列表
	 * 
	 * @param currentUser
	 *            当前登录用户
	 * @param pgInfo
	 *            分页对象 为NULL时则为查询所有记录
	 * @return
	 */
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap);

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
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 通过SKU编号查找产品信息
	 * 
	 * @param skuNo
	 * @return
	 */
	public ProductVo getProductBySkuNo(String skuNo);

	/**
	 * 通过productID查找产品信息
	 * 
	 * @param productID
	 * @return
	 */
	public ProductVo getProductByID(int productID);

	/**
	 * 新增产品
	 * 
	 * @param p
	 * @return
	 */
	public int addProduct(ProductVo p, UserInfo currentUser);

	/**
	 * 查询所有产品信息
	 * 
	 * @param p
	 */
	public List<ProductVo> findProductList(UserInfo currentUser,
			String productID);

	/**
	 * 通过skuNo删除产品
	 * 
	 * @param currentUser
	 * @param skuNo
	 */
	public void deleteProductBySkuNo(UserInfo currentUser, String skuNo);


	/**
	 * 删除指定ID的属性组，首先鉴别是否存在已经关联，如果存在不能删除
	 * 
	 */
	public boolean deleteAttrGroupByID(UserInfo currentUser, int attrGroupID);



	/**
	 * 通过valueID获取valueVo对象
	 * 
	 * @param valueID
	 * @return
	 */
	public AttrValueVo getAttrValueByValID(int valueID);

	/**
	 * 增加产品的属性
	 * 
	 * @param pda
	 */
	public void addProductDynamicAttrVal(ProductDynamicAttrVo pda);


	/**
	 * 根据条件查询product信息，并显示在datagrid中 并包含分页信息
	 * 
	 * @param firstindex
	 * @param maxresult
	 * @param whereSqlMap
	 * @param order
	 * @return
	 */
	public QueryResult<ProductVo> getProducts4DgByCondition(int firstindex,
			int maxresult, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 通过ProductID删除所有的动态属性值
	 * 
	 * @param productID
	 * @return
	 */
	public int deleteDynamicAttrByProdId(int productID);

	/**
	 * 更新产品的基本信息，包括属性 ，不包括图片
	 * 
	 * @param productVo
	 * @param currentUser
	 * @return
	 */
	public void updateProduct(ProductVo productVo, UserInfo currentUser);

	/**
	 * author :Carl 销售订单导入判断skuNo是否存在 skuNo:产品编码
	 * 
	 * @return
	 */
	public List<ProductVo> findSkuNoList(String skuNo);


	/**
	 * 通过属性名称,获取属性对象
	 * @param attrName
	 * @return
	 */
	public AttrNameVo findAttrNameByName(String attrName);

	/**
	 * 通过属性值名称，获取属性值对象
	 * @param attrValueName
	 * @return
	 */
	public AttrValueVo findAttrValueByName(String attrValueName);
	
	/**
	 * 更新产品的属性    map的key为属性名称  key的值为属性值
	 * @param rProdAttrList
	 */
	public void updateExcelAttr(List<Map<String, Object>> rProdAttrList);
	/**
	 * 通过skuNo,更新产品的名称
	 * @param skuNo
	 * @param productName
	 */
	public void updProductAttrByCMD(String skuNo, String productName);
	
	
	/**
	 * 存贮产品图片到本地
	 * @param productVo
	 */
	public void saveProdPic2Local(ProductVo productVo);

	/**
	 * 批量更新导入进来的采购价格
	 * @param rStockReferList
	 * @param currentUser
	 */
	public void updatePurchasePrice(List<Object[]> ppList,
			UserInfo currentUser);


	/**
	 * 包含批量sku的功能
	 * @param currentUser
	 * @param pgInfo
	 * @param whereSqlMap
	 * @param order
	 * @param skuNoList
	 * @return
	 */
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order, List<String> skuNoList);

	/**
	 * 通过prodIDList 获取产品信息列表
	 * @param poIDList
	 * @return
	 */
	public List<ProductVo> getProdByIDList(List<Integer> poIDList);

	/**
	 * 通过条件查询对应的产品，不是用于显示，完全不用将bean转vo
	 * @param whereSqlMap
	 * @return
	 */
	public List<Product> getProductList(Map<String, Object> whereSqlMap);

}