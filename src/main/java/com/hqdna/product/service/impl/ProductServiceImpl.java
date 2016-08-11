package com.hqdna.product.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.baseException.BaseException;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.mv.excel.ProductExcel;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.Product;
import com.hqdna.product.service.IProductService;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.product.vo.ProductDynamicAttrVo;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.vo.VendorVo;

@Service("productService")
public class ProductServiceImpl extends AbstractBase implements
		IProductService {


	
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap) {
		return getProductList(pgInfo, whereSqlMap, null);
	}

	
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return productDao.queryAllProducts(0, -1, whereSqlMap, order);
		} else {
			return productDao.queryAllProducts(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}

	
	public ProductVo getProductBySkuNo(String skuNo) throws BaseException {
		// 直接从已备案产品缓存中获取产品信息
		ProductVo p  = productDao.queryProductVoBySkuNo(skuNo);
		return p;
	}

	
	public ProductVo getProductByID(int productID) {
		return productDao.queryProductByID(productID);
	}

	
	public int addProduct(ProductVo p, UserInfo currentUser)
			throws BaseException {
		/*if (p.getLightColor() == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("新增产品，必须有发光颜色属性！");
			}
			return -1;
		}
		StringBuilder thisSkuNo = new StringBuilder();
		// 获取产品分类编码(小类)
		thisSkuNo.append(p.getCategoryCode());
		// 获取产品发光颜色代码，这里固定写死
		thisSkuNo.append(p.getLightColor());
		// 获取上一个的序列值
		int lashSkuo = lastSkuNo.incrementAndGet();
		String nowSN = Integer.toString(lashSkuo).toString();
		thisSkuNo.append("00000".subSequence(0, 5 - nowSN.length()) + nowSN);
		p.setSkuNo(thisSkuNo.toString());*/
		int productID = productDao.addProduct(p, currentUser);
		p.setProductID(productID);
		// 加入到未被备案产品缓存中
		// ProductVo vo = productDao.queryProductByID(productID);
		// unregisteCache.putIfAbsent(thisSkuNo.toString(), vo);
		return productID;
	}

	
	public void deleteProductBySkuNo(UserInfo currentUser, String skuNo) {
		productDao.deleteProductBySkuNo(currentUser, skuNo);
	}

	
	public boolean deleteAttrGroupByID(UserInfo currentUser, int attrGroupID) {
		return productDao.deleteAttrGroupByID(currentUser, attrGroupID);
	}


	
	public List<ProductVo> findProductList(UserInfo currentUser,
			String productID) {
		return productDao.findProductList(currentUser, productID);
	}

	/**
	 * 初始化
	 */

	
	public void init() {
	}

	
	public AttrValueVo getAttrValueByValID(int valueID) {
		return productDao.getAttrValueByValID(valueID);
	}

	
	public void addProductDynamicAttrVal(ProductDynamicAttrVo pda) {
		productDao.addProductDynamicAttrVal(pda);
	}

	
	public QueryResult<ProductVo> getProducts4DgByCondition(int firstindex,
			int maxresult, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		return productDao.getProducts4DgByCondition(firstindex, maxresult,
				whereSqlMap, order);
	}

	
	public int deleteDynamicAttrByProdId(int productID) {
		return productDao.deleteDynamicAttrByProdId(productID);
	}

	
	public void updateProduct(ProductVo productVo, UserInfo currentUser) {
		productDao.updateProduct(productVo, currentUser);
	}

	
	public List<ProductVo> findSkuNoList(String skuNo) {
		return productDao.findSkuNoList(skuNo);
	}


	
	public AttrNameVo findAttrNameByName(String attrName) {
		return productDao.findAttrNameByName(attrName);
	}

	
	public AttrValueVo findAttrValueByName(String attrValueName) {
		return productDao.findAttrValueByName(attrValueName);
	}


	
	public void updateExcelAttr(List<Map<String, Object>> rProdAttrList) {
		productDao.updateExcelAttr(rProdAttrList);
	}

	
	public void updProductAttrByCMD(String skuNo, String productName) {
		productDao.updProductAttrByCMD(skuNo,productName);
	}

	public void saveUrlProduct(UserInfo currentUser,String strUrl){

		ProductExcel productExcel = new ProductExcel();
		URL url;
		try {
			url = new URL(strUrl);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			InputStream inputStream = conn.getInputStream();
			List<Object[]> rProductList = productExcel
					.readProductExcel(inputStream);
			
			if (rProductList.size() > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("Excel导入数据有:" + rProductList.size() + "条");
				}
				List<ProductVo> prodlist = productExcel.parseProduct(rProductList);
				
				for (ProductVo productVo : prodlist) {
					ProductVo existProdcuct = getProductBySkuNo(productVo
							.getSkuNo());
					if (productVo.getVendorCode() == null
							|| "".equals(productVo.getVendorCode())) {
						if (logger.isDebugEnabled()) {
							logger.debug("Excel导入数据中:" + productVo.getProductEnName()
									+ "必须添加供应商编码！");
						}
						VendorVo tempVendor = vendorDao.getVendorByCode("119-0001");
						productVo.setVendorID(tempVendor.getVendorID());
					} else {// 绑定产品供应商
						VendorVo vendorVo = vendorDao.getVendorByCode(productVo
								.getVendorCode());
						if (vendorVo == null) {
							if (logger.isDebugEnabled()) {
								logger.debug("Excel导入数据中:" + productVo.getSkuNo()
										+ "的供应商编码是无效的！");
							}
							VendorVo tempVendor = vendorDao
									.getVendorByCode("119-0001");
							vendorVo = new VendorVo();
							String[] ignoreProperties = { "vendorID", "vendorCode",
							"vendorName" };
							BeanUtils.copyProperties(tempVendor, vendorVo,
									ignoreProperties);
							
							vendorVo.setVendorCode(productVo.getVendorCode());
							vendorVo.setVendorName("更新产品时系统添加的，请及时更新");
							vendorVo.setCreatorID(currentUser.getUserID());
							vendorVo.setOperatorID(currentUser.getUserID());
							vendorVo.setCreateDt(DateUtil.nowTs());
							vendorVo.setOperateDt(DateUtil.nowTs());
							int vendorID = vendorDao.addVendor(vendorVo);
							vendorVo.setVendorID(vendorID);
						}
						productVo.setVendorID(vendorVo.getVendorID());
					}
					/* 设置导入产品的默认项 */
					// 默认为可用
					productVo.setPurchasePrice(new BigDecimal(0));
					productVo.setOperateDt(DateUtil.nowTs());
					if (existProdcuct != null) {
						productVo.setProductID(existProdcuct.getProductID());
						productVo.setCreateDt(existProdcuct.getCreateDt());
						productVo.setCreatorID(existProdcuct.getCreatorID());
						productVo.setPurchasePrice(existProdcuct.getPurchasePrice());
						updateProduct(productVo, currentUser);
					} else {
						productVo.setCreateDt(DateUtil.nowTs());
						addProduct(productVo, currentUser);
					}
					saveProdPic2Local(productVo);
					
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void saveProdPic2Local(ProductVo productVo) {
		String realPath =File.separator+"root"+File.separator+"wgerp"+File.separator+"skupicture"+File.separator+productVo.getVendorCode();
		String urlStr = productVo.getPicturePath();
		if(urlStr!=null&&!"".equals(urlStr)){
			try {
				String suffix = urlStr.substring(urlStr.lastIndexOf("."),urlStr.length());
				localPic(urlStr, suffix, productVo.getSkuNo(), realPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void localPic(String urlStr,String suffix,String skuNo,String realPath){
		URL url;
		InputStream inStream = null;
		try {
			url = new URL("https://www.myled.com/image/cache/"+urlStr.substring(0,urlStr.lastIndexOf("."))+"-170x170"+urlStr.substring(urlStr.lastIndexOf("."),urlStr.length()));
			//打开链接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//设置请求方式为"GET"
			conn.setRequestMethod("GET");
			//超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			//通过输入流获取图片数据
			try {
				inStream = conn.getInputStream();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if(inStream!=null){
				String mainPicName = skuNo;
				mainPicName = mainPicName.concat(suffix);
				FileUtils.copyInputStreamToFile(inStream,new File(realPath, mainPicName));
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Transactional(rollbackFor=RuntimeException.class)
	public void updatePurchasePrice(List<Object[]> ppList,
			UserInfo currentUser) {
		for (Object[] objects : ppList) {
			String skuNo = objects[0].toString();
			BigDecimal purchasePrice = new BigDecimal(objects[1].toString());
			productDao.updatePurchasePrice(skuNo,purchasePrice,currentUser);
		}
		
	}


	
	public QueryResult<ProductVo> getProductList(PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order, List<String> skuNoList) {
		if (pgInfo == null) {
			return productDao.queryAllProducts(0, -1, whereSqlMap, order,skuNoList);
		} else {
			return productDao.queryAllProducts(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order,skuNoList);
		}
	}

	
	public List<ProductVo> getProdByIDList(List<Integer> prodIdList) {
		return productDao.getProdByIDList(prodIdList);
	}

	
	public List<Product> getProductList(Map<String, Object> whereSqlMap) {
		return productDao.getProductList(whereSqlMap);
	}


}
