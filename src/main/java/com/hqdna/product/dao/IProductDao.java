package com.hqdna.product.dao;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.Product;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.ProductDynamicAttrVo;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.vo.UserInfo;

/**
 * 产品持久层接口
 * 
 * 
 */
public interface IProductDao extends DAO<Product> {

	/**
	 * 查询产品
	 * 
	 * @return
	 */
	public QueryResult<ProductVo> queryAllProducts(int firstindex,
			int maxresult, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);

	/**
	 * 查询产品
	 * 
	 * @return
	 */
	public List<ProductVo> queryAllProducts();

	/**
	 * 通过skuNo查询产品信息
	 * 
	 * @param skuNo
	 * @return
	 */
	public ProductVo queryProductVoBySkuNo(String skuNo);

	/**
	 * 通过ID查询产品信息
	 * 
	 * @param productID
	 * @return
	 */
	public ProductVo queryProductByID(int productID);

	/**
	 * 新增产品信息
	 * 
	 * @param p
	 * @return
	 */
	public int addProduct(ProductVo p, UserInfo currentUser);

	/**
	 * 通过skuNo删除产品信息,实际为将产品isEnable改为0，禁止物理删除
	 * 
	 * @param p
	 */
	public void deleteProductBySkuNo(UserInfo currentUser, String skuNo);

	/**
	 * 更新产品的基本信息，包括属性，不包括图片
	 * 
	 * @param product
	 * @param currentUser
	 */
	public void updateProduct(ProductVo product, UserInfo currentUser);

	/**
	 * 查询所有产品信息
	 * 
	 * @param p
	 */
	public List<ProductVo> findProductList(UserInfo currentUser,
			String productID);



	/**
	 * 删除属性组
	 * 
	 * @param currentUser
	 * @param attrGroupID
	 * @return
	 */
	public boolean deleteAttrGroupByID(UserInfo currentUser, int attrGroupID);


	/**
	 * 获取系统全部产品拥有的全部属性
	 * 
	 * @return
	 */
	public List<AttrNameVo> getAllAttrName();


	/**
	 * 获取上一个新建产品的SkuNo
	 * 
	 * @return
	 */
	public String getLastSkuNo();

	/**
	 * 通过valueID获取valueVo对象
	 * 
	 * @param valueID
	 * @return
	 */
	public AttrValueVo getAttrValueByValID(int valueID);

	/**
	 * 增加产品属性
	 * 
	 * @param pda
	 */
	public void addProductDynamicAttrVal(ProductDynamicAttrVo pda);

	/**
	 * 更新图片路径
	 * 
	 * @param currentUser
	 * @param productID
	 * @param allPicPath
	 */
	public void updateProductPicPath(UserInfo currentUser, int productID,
			StringBuilder allPicPath);

	/**
	 * 通过分页等条件查询product的信息
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
	 * 通过产品id删除动态属性
	 * 
	 * @param productID
	 * @return
	 */
	public int deleteDynamicAttrByProdId(int productID);


	/**
	 * author :Carl 销售订单导入判断skuNo是否存在 skuNo:产品编码
	 * 
	 * @return
	 */
	public List<ProductVo> findSkuNoList(String skuNo);

	/**
	 * 通过属性名称获取属性名称对象
	 * 
	 * @param attrName
	 * @return
	 */
	public AttrNameVo findAttrNameByName(String attrName);

	/**
	 * 通过属性值名称获取属性值对象
	 * 
	 * @param attrValueName
	 * @return
	 */
	public AttrValueVo findAttrValueByName(String attrValueName);

	/**
	 * 同步myled数据时清除本地产品分类，属性等相关的表数据
	 */
	public void clearAllRelatedTableData();
/**
 * 删除和产品相关的信息 包括采购、发货、入库、销售
 */
	public void clearProductInfo();
/**
 * 删除产品相关的销售订单和采购订单相关信息
 */
	public void clearProdPoSo();
/**
 * 通过excel的方式更新产品的属性
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
 * 更新数据库中指定sku的采购价格
 * @param skuNo
 * @param purchasePrice
 * @param currentUser
 */
public void updatePurchasePrice(String skuNo, BigDecimal purchasePrice,
		UserInfo currentUser);

/**
 * 获取最大的EANcode
 * @return
 */
public String getLastEANcode();
/**
 * 可以查询一批sku 并分页显示
 * @param startIndex
 * @param rows
 * @param whereSqlMap
 * @param order
 * @param skuNoList
 * @return
 */
public QueryResult<ProductVo> queryAllProducts(int startIndex, int rows,
		Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order,
		List<String> skuNoList);

/**
 * 通过idList获取product list
 * @param prodIdList
 * @return
 */
public List<ProductVo> getProdByIDList(List<Integer> prodIdList);
/**
 * 通过条件查询对应的产品，不是用于显示，完全不用将bean转vo
 * @param whereSqlMap
 * @return
 */
public List<Product> getProductList(Map<String, Object> whereSqlMap);

public List<Product> getProdByIDs(List<Integer> prodIdList);

public void addOrUpdProduct(UserInfo currentUser, Product product);

}
