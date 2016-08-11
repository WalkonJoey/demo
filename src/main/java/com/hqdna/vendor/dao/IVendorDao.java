package com.hqdna.vendor.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.baseDao.DAO;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.bean.Vendor;
import com.hqdna.vendor.vo.VendorVo;


/**
 * 供应商持久层接口
 * 
 *
 * @author Aaron 2014-8-15
 *
 */
public interface IVendorDao extends DAO<Vendor>{

	/**
	 * 
	 * 分页查询方法
	 *
	 */
	public QueryResult<VendorVo> queryAllVendors(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order);
	
	/**
	 * 查询所有供应商信息(分页)
	 * 
	 * @return
	 */
	public QueryResult<VendorVo> queryAllVendor();
	
	/**
	 * 通过编码查询供应商信息
	 * @param vendorCode
	 * @return
	 */
	public VendorVo getVendorByCode(String vendorCode);
	
	/**
	 * 新增供应商
	 * @param currentUser
	 * @param vendor
	 */
	public int addVendor(VendorVo vendor);
	
	/**
	 * 删除供应商
	 * @param currentUser
	 * @param vendorID
	 */
	public void deleteVendorByID(int... vendorID);
	
	/**
	 * 更新供应商信息
	 * @param currentUser
	 * @param vendor
	 */
	public void updateVendor(VendorVo vendor);
	
	/**
	 * 按ID查询
	 * @param vendorID
	 * @return
	 */
	public VendorVo queryVendorByID(int vendorID);
	
	
	/**
	 * 获取每个供应商的最后一个序列号
	 * 
	 * @return
	 */
	public Map<String, Integer> getAllLastSN();
	
	/**
	 * 获取所有的跟单人
	 * @return
	 */
	public List<String> getAllDocumentary();

	/**
	 * 通过大分类获取最后订单编号
	 * @param category
	 * @return
	 */
	public int getAllLastSN(String category);

	/**
	 * 批量更新税率
	 * @param attrMap
	 * @param currentUser
	 */
	public void updAttr(Map<String, Object> attrMap, UserInfo currentUser);


	public List<String> getAllUseVendorCode();

}
