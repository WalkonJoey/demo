package com.hqdna.vendor.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.baseService.IService;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.dao.IVendorDao;
import com.hqdna.vendor.service.IVendorService;
import com.hqdna.vendor.vo.VendorVo;

@Service("vendorService")
public class VendorServiceImpl extends AbstractBase implements
		IService,IVendorService {

	// 供应商的最后三位序号缓存
	//private AtomicInteger sNCache = new AtomicInteger();

	
	public QueryResult<VendorVo> getVendorList(UserInfo currentUser) {
		return vendorDao.queryAllVendor();
	}

	
	public VendorVo getVendorByCode(String vendorCode) {
		return vendorDao.getVendorByCode(vendorCode);
	}

	
	public void updateVendor(UserInfo currentUser, VendorVo vendor) {
		vendor.setOperatorID(currentUser.getUserID());
		vendor.setOperateDt(DateUtil.nowTs());
		vendorDao.updateVendor(vendor);
	}

	
	public int createNewVendor(UserInfo currentUser, VendorVo vendor) {
		vendor.setCreatorID(currentUser.getUserID());
		vendor.setOperatorID(currentUser.getUserID());
		vendor.setCreateDt(DateUtil.nowTs());
		vendor.setOperateDt(DateUtil.nowTs());
		if(vendor.getPurchaseTax()==null){
			vendor.setPurchaseTax(new BigDecimal(0));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("正在新增供应商vendorCode:" + vendor.getVendorCode() + ",名称："
					+ vendor.getVendorName());
		}
		int vendorID = vendorDao.addVendor(vendor);
		return vendorID;
	}

	
	public void deleteVendorByID(UserInfo currentUser, int... vendorIDs) {
		vendorDao.deleteVendorByID(vendorIDs);
	}

	
	public QueryResult<VendorVo> getVendorList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return vendorDao.queryAllVendors(-1, -1, whereSqlMap, order);
		} else {
			return vendorDao.queryAllVendors(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}

	
	public VendorVo getVendorByID(int vendorID) {
		return vendorDao.queryVendorByID(vendorID);
	}

	/**
	 * 获取下一个给供应商的编码
	 * 
	 * @return
	 */
	private String getNextCode(String category) {
		StringBuilder sb = new StringBuilder();
		int lastSN = vendorDao.getAllLastSN(category);
		// 第一部分 产品大类
		sb.append(category);
		sb.append("-");
		// 第二部分 序列号
		String nextSn = String.valueOf(lastSN+1);
		sb.append("0000".substring(0, 4 - nextSn.length()) + nextSn);
		return sb.toString();
	}

	
	public void init() {
	}

	
	public List<String> getAllDocumentary() {
		return vendorDao.getAllDocumentary();
	}

	
	public int createCustomVendor(UserInfo currentUser, VendorVo vendorVo) {
		vendorVo.setCreatorID(currentUser.getUserID());
		vendorVo.setOperatorID(currentUser.getUserID());
		vendorVo.setCreateDt(DateUtil.nowTs());
		vendorVo.setOperateDt(DateUtil.nowTs());

		if (logger.isDebugEnabled()) {
			logger.debug("正在新增供应商vendorCode:" + vendorVo.getVendorCode() + ",名称："
					+ vendorVo.getVendorName());
		}
		int vendorID = vendorDao.addVendor(vendorVo);
		return vendorID;
	}

	
	public void updateTaxRate(List<Map<String, Object>> attrList,
			UserInfo currentUser) {
		for (Map<String, Object> attrMap : attrList) {
			vendorDao.updAttr(attrMap,currentUser);
		}
	}

}
