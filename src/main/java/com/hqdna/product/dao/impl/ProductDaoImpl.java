package com.hqdna.product.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.baseException.BaseException;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.bean.AttrName;
import com.hqdna.product.bean.AttrValue;
import com.hqdna.product.bean.Category;
import com.hqdna.product.bean.Product;
import com.hqdna.product.bean.ProductDynamicAttr;
import com.hqdna.product.dao.IProductDao;
import com.hqdna.product.util.exception.ProductException;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.ProductDynamicAttrVo;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.bean.User;
import com.hqdna.user.util.exception.UserException;
import com.hqdna.user.vo.UserInfo;

@Component("productDao")
@Transactional
public class ProductDaoImpl extends DaoSupport<Product> implements IProductDao {

	
	public QueryResult<ProductVo> queryAllProducts(int firstindex,
			int maxresult, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		QueryResult<ProductVo> r = new QueryResult<ProductVo>();

		List<ProductVo> list = new ArrayList<ProductVo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<Product> result = null;
		if (sql == null) {
			result = getScrollData(firstindex, maxresult, null, null, order);
		} else {
			result = getScrollData(firstindex, maxresult, (String) sql[0],
					(Object[]) sql[1], order);
		}

		List<Product> products = (List<Product>) result.getResultlist();
		try {
			for (Product p : products) {
				ProductVo v_p = new ProductVo();
				p.transferPo2Vo(v_p, true);
				list.add(v_p);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new ProductException("产品查询Dao出错了", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}

	
	public List<ProductVo> queryAllProducts() {
		String sql = "from Product";
		List<Product> list = executeQuery(sql, null);
		List<ProductVo> voList = new ArrayList<ProductVo>();
		for (Product product : list) {
			ProductVo vo = new ProductVo();
			product.transferPo2Vo(vo, true);
			voList.add(vo);
		}
		return voList;
	}

	
	public void deleteProductBySkuNo(UserInfo currentUser, String skuNo) {
		/*
		 * try { String sql =
		 * "update Product pro set pro.isEnable=0 where pro.skuNo=?1";
		 * executeUpdate(sql, new Object[] { skuNo }); } catch (Exception e) {
		 * throw new ProductException("Dao删除对象时出错!", e); }
		 */
		try {
			Product product = queryProductBySkuNo(skuNo);
			em.remove(product);
		} catch (Exception e) {
			throw new ProductException("Dao删除对象时出错!", e);
		}
	}

	private Product queryProductBySkuNo(String skuNo) {
		List<Product> pros = getScrollData(-1, -1, "skuNo=?",
				new Object[] { skuNo }).getResultlist();
		if (pros == null || pros.size() != 1) {
			return null;
		}
		return pros.get(0);
	}

	
	public ProductVo queryProductVoBySkuNo(String skuNo) {
		ProductVo p_vo = new ProductVo();
		Product p = queryProductBySkuNo(skuNo);
		if (p == null) {
			return null;
		}
		try {

			p.transferPo2Vo(p_vo, true);
		} catch (Exception e) {
			throw new ProductException("Dao查找对象时出错!", e);
		}
		return p_vo;
	}

	
	public ProductVo queryProductByID(int productID) {
		ProductVo p_vo = new ProductVo();
		List<Product> pros = getScrollData(-1, -1, "productID=?",
				new Object[] { productID }).getResultlist();
		if (pros == null || pros.size() != 1) {
			return null;
		}
		try {
			Product p = pros.get(0);
			p.transferPo2Vo(p_vo, true);
		} catch (Exception e) {
			throw new ProductException("Dao查找对象时出错!", e);
		}
		return p_vo;
	}

	
	public void updateProduct(ProductVo productVo, UserInfo currentUser) {
		Product product = new Product();

		User operater = new User();
		operater.setUserID(currentUser.getUserID());
		product.setOperatorUser(operater);
		try {
			product.transferPo2Vo(productVo, false);
			em.merge(product);
		} catch (Exception e) {
			throw new ProductException("Dao产品更新出错", e);
		}
	}

	
	public int addProduct(ProductVo p, UserInfo currentUser)
			throws BaseException {
		Product pp = new Product();
		try {
			pp.transferPo2Vo(p, false);
			if (currentUser != null) {
				User creator = new User();
				creator.transferPo2Vo(currentUser, false);
				pp.setCreatorUser(creator);
				pp.setOperatorUser(creator);
			}
			em.persist(pp);
			return pp.getProductID();
		} catch (Exception e) {
			throw new ProductException("Dao产品新增出错", e);
		}
	}



	
	public boolean deleteAttrGroupByID(UserInfo currentUser, int attrGroupID) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ag from AttrGroup ag where ag.attrGroupID=?1");
		List<Object> obj = executeQuery(sb.toString(),
				new Object[] { attrGroupID });
		if (obj != null && obj.size() == 1) {
			AttrGroup agg = (AttrGroup) obj.get(0);
			Set<Category> cats = agg.getCategorys();
			if (cats == null || cats.isEmpty()) {
				em.remove(agg);
				return true;
			}
		}
		return false;
	}




	
	public List<AttrNameVo> getAllAttrName() {
		StringBuilder tsb = new StringBuilder();
		tsb.append("select an from AttrName an");
		List<Object> list = this.executeQuery(tsb.toString(), null);
		List<AttrNameVo> anvList = null;
		if (list.size() > 0) {
			anvList = new ArrayList<AttrNameVo>();
			for (Object obj : list) {
				AttrNameVo anv = new AttrNameVo();
				((AttrName) obj).transferPo2Vo(anv, true);
				anvList.add(anv);
			}
		}
		return anvList;
	}

	/**
	 * 获取当前最大的分类编码
	 * 
	 * @return
	 */
	/*
	 * private synchronized int getMaxCategoryCode() { StringBuilder tsb = new
	 * StringBuilder();
	 * tsb.append("select max(cat.categoryCode) from Category cat");
	 * List<Integer> list = executeQuery(tsb.toString(), null); return list !=
	 * null && list.size() == 1 ? list.get(0) : 0; }
	 */

	
	public List<ProductVo> findProductList(UserInfo currentUser,
			String productID) {
		List<ProductVo> list = null;

		StringBuilder sb = new StringBuilder();
		sb.append("select t from Product t");
		List<Product> productList = this.executeQuery(sb.toString(), null);
		if (productList == null) {
			return null;
		}
		list = new ArrayList<ProductVo>();
		for (Product product : productList) {
			ProductVo vo = new ProductVo();
			product.transferPo2Vo(vo, true);
			list.add(vo);
		}
		return list;
	}

	
	public String getLastSkuNo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select p.skuNo from Product p order by p.operateDt desc,p.productID desc");
		List<String> prodList = executeQuery(sb.toString(), null);
		if (prodList == null || prodList.isEmpty()) {
			return "00000";
		}
		return prodList.get(0);
	}

	
	public AttrValueVo getAttrValueByValID(int valueID) {
		StringBuilder tsb = new StringBuilder();
		tsb.append("select an from AttrValue an where an.attrValueID = ?1");
		Object[] params = new Object[] { valueID };
		List<Object> list = this.executeQuery(tsb.toString(), params);
		AttrValueVo avv = new AttrValueVo();
		AttrValue av = (AttrValue) list.get(0);
		av.transferPo2Vo(avv, true);
		return avv;
	}

	
	public void addProductDynamicAttrVal(ProductDynamicAttrVo pda) {
		ProductDynamicAttr av = new ProductDynamicAttr();
		av.transferPo2Vo(pda, false);
		em.persist(av);
	}

	
	public void updateProductPicPath(UserInfo currentUser, int productID,
			StringBuilder allPicPath) {
		try {
			StringBuilder sb = new StringBuilder();
			/*
			 * sb.append(
			 * "update Product pt set pt.picturePath = ?1,pt.operatorUser.userCNName =?2,pt.operateDt = ?3 where pt.productID = ?4"
			 * ); Object[] params = new Object[] {allPicPath.toString(),
			 * currentUser.getUserCNName(), Timestamp.valueOf(DateUtil
			 * .formatDateTime(new Date())),productID};
			 * executeUpdate(sb.toString(), params);
			 */
			sb.append("select pt from Product pt where pt.productID=?1");
			List<Product> productList = this.<Product> executeQuery(
					sb.toString(), new Object[] { productID });
			Product product = productList.get(0);
			product.setPicturePath(allPicPath.toString());
			User operator = new User();
			operator.setUserID(currentUser.getUserID());
			product.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(new Date())));
			product.setOperatorUser(operator);
			em.merge(product);
		} catch (Exception e) {
			throw new ProductException("Dao产品更新出错", e);
		}
	}

	
	public QueryResult<ProductVo> getProducts4DgByCondition(int firstindex,
			int maxresult, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		List<ProductVo> list = new ArrayList<ProductVo>();

		QueryResult<ProductVo> r = new QueryResult<ProductVo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<Product> result = null;
		if (sql == null) {
			result = getScrollData(firstindex, maxresult, null, null, order);
		} else {
			result = getScrollData(firstindex, maxresult, (String) sql[0],
					(Object[]) sql[1], order);
		}
		List<Product> products = (List<Product>) result.getResultlist();
		try {
			for (Product u : products) {
				ProductVo target_product = new ProductVo();
				u.transferPo2Vo(target_product, true);
				list.add(target_product);
			}
		} catch (Exception e) {
			throw new UserException("Dao用户查询所有用户出错", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}

	
	public int deleteDynamicAttrByProdId(int productID) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ProductDynamicAttr pda where pda.productID.productID=?1");
		return executeUpdate(sb.toString(), new Object[] { productID });
	}

	
	public List<ProductVo> findSkuNoList(String skuNo) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("select pt from Product pt where pt.skuNo=?1");
			List<Product> pList = this.<Product> executeQuery(sb.toString(),
					new Object[] { skuNo });
			if (pList == null) {
				return null;
			}
			List<ProductVo> voList = new ArrayList<ProductVo>();
			for (Product product : pList) {
				ProductVo vo = new ProductVo();
				product.transferPo2Vo(vo, true);
				voList.add(vo);
			}
			return voList;
		} catch (Exception e) {
			throw new ProductException("销售订单导入判断skuNo是否存在查询出错!", e);
		}
	}

	
	public AttrNameVo findAttrNameByName(String attrName) {
		try {
			StringBuilder tsb = new StringBuilder();
			tsb.append("select an from AttrName an where lower(an.attrName)=?1");
			List<AttrName> ans = this.executeQuery(tsb.toString(),
					new Object[] { attrName.toLowerCase() });
			if (ans != null && ans.size() > 0) {
				AttrNameVo anv = new AttrNameVo();
				AttrName an = ans.get(0);
				an.transferPo2Vo(anv, true);
				return anv;
			}
		} catch (Exception e) {
			throw new ProductException("销售订单导入判断skuNo是否存在查询出错!", e);
		}
		return null;
	}

	
	public AttrValueVo findAttrValueByName(String attrValueName) {
		try {
			StringBuilder tsb = new StringBuilder();
			tsb.append("select av from AttrValue av where lower(av.valueName)=?1");
			List<AttrValue> avs = this.executeQuery(tsb.toString(),
					new Object[] { attrValueName.toLowerCase() });
			if (avs != null && avs.size() > 0) {
				AttrValueVo avv = new AttrValueVo();
				AttrValue av = avs.get(0);
				av.transferPo2Vo(avv, true);
				return avv;
			}
		} catch (Exception e) {
			throw new ProductException("销售订单导入判断skuNo是否存在查询出错!", e);
		}
		return null;
	}

	public void clearAllRelatedTableData() {
		String sql1 = "delete from TBL_CATEGORY_LANGUAGE";
		String sql2 = "delete from TBL_CATEGORY_ATTRGROUP";
		String sql3 = "delete from TBL_PRODUCT_CATEGORY";
		String sql4 = "delete from TBL_ATTRGROUP_ATTRNAME";
		String sql5 = "delete from TBL_ATTRGROUP";
		String sql6 = "delete from TBL_ATTRVALUE_LANGUAGE";
		String sql7 = "delete from TBL_ATTRVALUE";
		String sql8 = "delete from TBL_ATTRNAME_LANGUAGE";
		String sql9 = "delete from TBL_ATTRNAME";
		executeByNativeSQL(sql1, null);
		executeByNativeSQL(sql2, null);
		executeByNativeSQL(sql3, null);
		executeByNativeSQL(sql4, null);
		executeByNativeSQL(sql5, null);
		executeByNativeSQL(sql6, null);
		executeByNativeSQL(sql7, null);
		executeByNativeSQL(sql8, null);
		executeByNativeSQL(sql9, null);
	}

	
	public void clearProductInfo() {
		String sql1 = "delete from TBL_PRODUCT";
		executeByNativeSQL(sql1, null);
	}

	
	public void clearProdPoSo() {
		String sql1 = "delete from TBL_SO_REF_PO";
		String sql2 = "delete from TBL_SO_DETAIL";
		String sql3 = "delete from TBL_SO";
		String sql4 = "delete from TBL_DELIVERY_REF_WAREHOUSE_ENTRY";
		String sql5 = "delete from TBL_SHIPMENT_DETAIL";
		String sql6 = "delete from TBL_SHIPMENT";
		String sql7 = "delete from TBL_LOGI";
		String sql8 = "delete from TBL_PO_REF_DELIVERY";
		String sql9 = "delete from TBL_DELIVERY_DETAIL";
		String sql10 = "delete from TBL_DELIVERY";
		String sql11 = "delete from TBL_EXPRESS";
		String sql12 = "delete from TBL_PO_DETAIL";
		String sql13 = "delete from TBL_PO";
		String sql14 = "delete from TBL_WAREHOUSE_ENTRY_DETAIL";
		String sql15 = "delete from TBL_WAREHOUSE_ENTRY";
		String sql16 = "delete from TBL_HIST_PO_DETAIL";
		String sql17 = "delete from TBL_HIST_PO";
		String sql18 = "delete from TBL_SYS_NOTICE_SENT";
		String sql19 = "delete from TBL_SYS_NOTICE";
		executeByNativeSQL(sql1, null);
		executeByNativeSQL(sql2, null);
		executeByNativeSQL(sql3, null);
		executeByNativeSQL(sql4, null);
		executeByNativeSQL(sql5, null);
		executeByNativeSQL(sql6, null);
		executeByNativeSQL(sql7, null);
		executeByNativeSQL(sql8, null);
		executeByNativeSQL(sql9, null);
		executeByNativeSQL(sql10, null);
		executeByNativeSQL(sql11, null);
		executeByNativeSQL(sql12, null);
		executeByNativeSQL(sql13, null);
		executeByNativeSQL(sql14, null);
		executeByNativeSQL(sql15, null);
		executeByNativeSQL(sql16, null);
		executeByNativeSQL(sql17, null);
		executeByNativeSQL(sql18, null);
		executeByNativeSQL(sql19, null);
	}

	
	public void updateExcelAttr(List<Map<String, Object>> rProdAttrList) {
		for (Map<String, Object> map : rProdAttrList) {
			StringBuilder hql = new StringBuilder();
			StringBuilder whereStr = new StringBuilder();
			Object[] params = new Object[map.keySet().size()];
			whereStr.append(" WHERE 1=1");
			hql.append("UPDATE TBL_PRODUCT SET ");
			int i = 0;//参数要从1开始
			for (String key : map.keySet()) {
				if("skuNo".equals(key)){
					whereStr.append(" AND skuNo = ?"+(i+1));
				}else if(i==map.keySet().size()-1){
					hql.append(key+"= ?"+(i+1));
				}else{
					hql.append(key+"= ?"+(i+1)+",");
				}
				params[i] = map.get(key);
				i++;
			}
			hql.append(whereStr.toString());
			executeByNativeSQL(hql.toString(), params);
		}
		
	}

	
	public void updProductAttrByCMD(String skuNo, String productName) {
		StringBuilder hql = new StringBuilder();
		Object[] params = new Object[]{productName,skuNo};
		hql.append("UPDATE TBL_PRODUCT SET productName = ?1 WHERE skuNo = ?2");
		executeByNativeSQL(hql.toString(), params);
	}

	
	public void updatePurchasePrice(String skuNo, BigDecimal purchasePrice,
			UserInfo currentUser) {
		StringBuilder hql = new StringBuilder();
		Object[] params = new Object[]{skuNo,purchasePrice,currentUser.getUserID(),DateUtil.nowTs()};
		hql.append("UPDATE TBL_PRODUCT SET purchasePrice = ?2,operator = ?3,operateDt = ?4 WHERE skuNo = ?1");
		executeByNativeSQL(hql.toString(), params);
		
	}

	
	
	public String getLastEANcode() {
		StringBuilder sb = new StringBuilder();
		sb.append("select p.EANcode from Product p order by p.EANcode desc");
		Query query = em.createQuery(sb.toString());
		query.setFirstResult(0);
		query.setMaxResults(10);
		@SuppressWarnings("unchecked")
		List<String> prodList = query.getResultList();
		//List<String> prodList = executeQuery(sb.toString(), null);
		if (prodList == null || prodList.isEmpty()) {
			return "0000000000000";
		}
		return prodList.get(0);
	}

	@SuppressWarnings("unchecked")
	
	public QueryResult<ProductVo> queryAllProducts(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order, List<String> skuNoList) {
		QueryResult<ProductVo> r = new QueryResult<ProductVo>();

		List<ProductVo> list = new ArrayList<ProductVo>();

		Object[] sql = transferSql(whereSqlMap);

		//QueryResult<Product> result = null;
		String wherejpql = (String) sql[0];
		Object[] param = (Object[]) sql[1];
		QueryResult<Product> qr = new QueryResult<Product>();
		String entityname = getEntityName(this.entityClass);
		Query query = em.createQuery("select distinct o from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql +"AND o.skuNo in (:skuNos)") + buildOrderby(order));
		setQueryParams(query, param);
		query.setParameter("skuNos",skuNoList);
		if (maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult);
		qr.setResultlist(query.getResultList());
		
		query = em.createQuery("select count("
				+ getCountField(this.entityClass)
				+ ") from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql+"AND o.skuNo in (:skuNos)"));
		setQueryParams(query, param);
		query.setParameter("skuNos",skuNoList);
		qr.setTotalrecord((Long) query.getSingleResult());
		

		List<Product> products = (List<Product>) qr.getResultlist();
		try {
			for (Product p : products) {
				ProductVo v_p = new ProductVo();
				p.transferPo2Vo(v_p, true);
				list.add(v_p);
			}
		} catch (Exception e) {
			throw new ProductException("产品查询Dao出错了", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(qr.getTotalrecord());
		return r;
	}

	
	public List<ProductVo> getProdByIDList(List<Integer> prodIdList) {
		List<ProductVo> prodVoList = new ArrayList<ProductVo>(); 
		String sql = "SELECT a FROM Product a WHERE a.productID IN :prodIdList";
		Query query = em.createQuery(sql);
		query.setParameter("prodIdList",prodIdList);
		@SuppressWarnings("unchecked")
		List<Product> poList = query.getResultList();
		if(poList!=null&&poList.size()>0){
			for (Product prod : poList) {
				ProductVo tempVo = new ProductVo();
				prod.transferPo2Vo(tempVo, true);
				prodVoList.add(tempVo);
			}
			return prodVoList;
		}else{
			return null;
		}
	}

	
	public List<Product> getProductList(Map<String, Object> whereSqlMap) {
		Object[] sql = transferSql(whereSqlMap);
		QueryResult<Product> result = null;
		if (sql != null) {
			result = getScrollData(-1, -1, (String) sql[0],
					(Object[]) sql[1], null);
		}
		if(result!=null&&result.getResultlist().size()>0){
			return (List<Product>) result.getResultlist();
		}
		return null;
	}

	
	public List<Product> getProdByIDs(List<Integer> prodIdList) {
		String sql = "SELECT a FROM Product a WHERE a.productID IN :prodIdList";
		Query query = em.createQuery(sql);
		query.setParameter("prodIdList",prodIdList);
		@SuppressWarnings("unchecked")
		List<Product> poList = query.getResultList();
		if(poList!=null&&poList.size()>0){
			return poList;
		}else{
			return null;
		}
	}

	
	public void addOrUpdProduct(UserInfo currentUser, Product product) {
		if(product.getProductID()>0){
			em.merge(product);
		}else{
			em.persist(product);
		}
	}


}
