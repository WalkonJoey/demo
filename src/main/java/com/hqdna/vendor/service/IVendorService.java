package com.hqdna.vendor.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.vo.VendorVo;

/**
 * 供应商服务类接口
 * 
 * 
 */
public interface IVendorService {

	/**
	 * 查询供应商信息
	 * 
	 * @param currentUser
	 * @return
	 */
	public QueryResult<VendorVo> getVendorList(UserInfo currentUser);

	/**
	 * 更新供应商信息
	 * 
	 * @param vendor
	 */
	public void updateVendor(UserInfo currentUser, VendorVo vendor);

	/**
	 * 新增供应商信息
	 * 
	 * @param vendorID
	 */
	public int createNewVendor(UserInfo currentUser, VendorVo vendor);
	
	/**
	 * 删除指定ID的供应商
	 * 
	 * @param vendorID
	 */
	public void deleteVendorByID(UserInfo currentUser, int... vendorIDs);

	/**
	 * 通过vendorCode编号查找相关联的供应商信息
	 * 
	 * @param vendorCode
	 * @return
	 */
	public VendorVo getVendorByCode(String vendorCode);
	
	/**
	 *
	 * 条件查询
	 * 
	 */
	public QueryResult<VendorVo> getVendorList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order);
	
	/**
	 * 
	 * 按id查询
	 * 
	 */
	public VendorVo getVendorByID(int id);
	/**
	 * 获取所有的跟单人
	 * @return
	 */
	public List<String> getAllDocumentary();

	/**
	 * 创建自定义好的供应商   就是供应商编码不是程序生成的
	 * @param currentUser
	 * @param vendorVo
	 * @return
	 */
	public int createCustomVendor(UserInfo currentUser, VendorVo vendorVo);


	/**
	 * 批量更新税率
	 * @param attrList
	 * @param currentUser
	 */
	public void updateTaxRate(List<Map<String, Object>> attrList,
			UserInfo currentUser);

}
