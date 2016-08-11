package com.hqdna.vendor.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.page.QueryResult;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.bean.Vendor;
import com.hqdna.vendor.dao.IVendorDao;
import com.hqdna.vendor.vo.VendorVo;

@Transactional
@Component("vendorDao")
public class VendorDaoImpl extends DaoSupport<Vendor> implements IVendorDao {

	
	public QueryResult<VendorVo> queryAllVendors(int firstindex, int maxresult,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		QueryResult<VendorVo> r = new QueryResult<VendorVo>();
		QueryResult<Vendor> result = null;
		Object[] sql = transferSql(whereSqlMap);
		if (sql == null) {
			result = getScrollData(firstindex, maxresult, null, null, order);
		} else {
			result = getScrollData(firstindex, maxresult, (String) sql[0],
					(Object[]) sql[1], order);
		}
		List<VendorVo> list = new ArrayList<VendorVo>(result.getResultlist()
				.size());
		for (Vendor vendor : result.getResultlist()) {
			VendorVo vo = new VendorVo();
			vendor.transferPo2Vo(vo, true);
			list.add(vo);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}

	
	public QueryResult<VendorVo> queryAllVendor() {
		return queryAllVendors(0, -1, null, null);
	}

	
	public VendorVo getVendorByCode(String vendorCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select vendor from Vendor vendor where vendor.vendorCode=?1");
		List<Vendor> list = this.<Vendor> executeQuery(sb.toString(),
				new Object[] { vendorCode });
		if (list != null && list.size() == 1) {
			Vendor vendor = list.get(0);
			VendorVo vo = new VendorVo();
			vendor.transferPo2Vo(vo, true);
			return vo;
		}
		return null;
	}

	
	public int addVendor(VendorVo vendorVo) {
		Vendor vendor = new Vendor();
		vendor.transferPo2Vo(vendorVo, false);
		em.persist(vendor);
		return vendor.getVendorID();
	}

	
	public void deleteVendorByID(int... vendorIDs) {
		StringBuilder sb = new StringBuilder();
		sb.append("select vendor from Vendor vendor where vendor.vendorID=?1");
		for (int i = 0; i < vendorIDs.length; i++) {
			List<Vendor> list = this.<Vendor> executeQuery(sb.toString(),
					new Object[] { vendorIDs[i] });
			Vendor vendor = list.get(0);
			try {
				em.remove(vendor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 通过ID获取供应商信息
	 * 
	 * @param vendorID
	 * @return
	 */
	public VendorVo queryVendorByID(int vendorID) {
		Vendor vendor = find(vendorID);
		VendorVo vo = new VendorVo();
		vendor.transferPo2Vo(vo, true);
		return vo;
	}

	/**
	 * 通过ID获取供应商信息
	 * 
	 * @param vendorID
	 * @return
	 */
	public Vendor queryVendorByIDInternally(int vendorID) {
		return find(vendorID);
	}

	
	public void updateVendor(VendorVo vendorVo) {
		Vendor vendor = queryVendorByIDInternally(vendorVo.getVendorID());
		vendor.transferPo2Vo(vendorVo, false);
		em.merge(vendor);
	}

	@SuppressWarnings("unchecked")
	
	public Map<String, Integer> getAllLastSN() {
		StringBuilder sql = new StringBuilder();
		sql.append("select v.vendorCode from TBL_VENDOR v ORDER BY v.createDt desc");
		Query query = em.createNativeQuery(sql.toString());
		List<Object> vendors = query.getResultList();
		if (vendors == null || vendors.isEmpty()) {
			return null;
		}
		// 供应商编码格式：“122-0003” 前面三位是产品大类
		Map<String, Integer> map = new HashMap<String, Integer>();
		String vendorCode = (String) vendors.get(0);
		String categoryKey = vendorCode.substring(0, 3);
		String sNvalue = vendorCode.substring(4, vendorCode.length());
		map.put(categoryKey, Integer.valueOf(sNvalue));
		return Collections.unmodifiableMap(map);
	}

	
	public List<String> getAllDocumentary() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT v.documentary FROM TBL_VENDOR v ORDER BY v.createDt DESC");
		Query query = em.createNativeQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object> documentarys = query.getResultList();
		if (documentarys == null || documentarys.isEmpty()) {
			return null;
		}
		List<String> dList = new ArrayList<String>();
		for (Object object : documentarys) {
			String documentary = (String) object;
			dList.add(documentary);
		}
		return dList;
	}


	
	public int getAllLastSN(String category) {
		StringBuilder sql = new StringBuilder();
		sql.append("select v.vendorCode from Vendor v WHERE v.vendorCode like '");
		sql.append(category);
		sql.append("%' ORDER BY v.createDt desc");
		Query query = em.createQuery(sql.toString());
		/*Object[] params = new Object[] {category};
		setQueryParams(query, params);*/
		@SuppressWarnings("unchecked")
		List<Object> vendors = query.getResultList();
		if (vendors == null || vendors.isEmpty()) {
			return 0;
		}
		// 供应商编码格式：“122-0003” 前面三位是产品大类
		String vendorCode = (String) vendors.get(0);
		String sNvalue = vendorCode.substring(4, vendorCode.length());
		return Integer.valueOf(sNvalue);
	}

	
	public void updAttr(Map<String, Object> attrMap, UserInfo currentUser) {
		Set<String> ketSet = attrMap.keySet();
		Iterator<String> iterator = ketSet.iterator();
		Object[] params = new Object[ketSet.size()+2];
		if(iterator.hasNext()&&ketSet.contains("vendorCode")){
			StringBuilder hql = new StringBuilder();
			String attrName = iterator.next().trim();
			if("vendorCode".equals(attrName)){
				attrName = iterator.next().trim();
			}
			params[0] = attrMap.get(attrName);
			hql.append("UPDATE TBL_VENDOR SET "+attrName.trim()+" = ?1");
			int i = 2;
			while(iterator.hasNext()){
				attrName = iterator.next();
				if("vendorCode".equals(attrName)){
					continue;
				}
				params[i-1] = attrMap.get(attrName);
				hql.append(","+iterator.next().trim()+" = ?"+i);
				i++;
				
			}
			params[i-1] = currentUser.getUserID();
			hql.append(",operator = ?"+i);
			i++;
			params[i-1] = DateUtil.nowTs();
			hql.append(",operateDt = ?"+i);
			i++;
			params[i-1] = attrMap.get("vendorCode");
			hql.append(" WHERE vendorCode = ?"+i);
			executeByNativeSQL(hql.toString(), params);
		}
	}


	
	public List<String> getAllUseVendorCode() {
		Object[] params = new Object[] { (byte)1};
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT v.vendorCode FROM Vendor v WHERE v.isUsing = ?1");
		Query query = em.createQuery(sql.toString());
		setQueryParams(query, params);
		@SuppressWarnings("unchecked")
		List<Object> vendorCodeList = query.getResultList();
		if (vendorCodeList == null || vendorCodeList.isEmpty()) {
			return null;
		}
		List<String> dList = new ArrayList<String>();
		for (Object object : vendorCodeList) {
			String vendorCode = (String) object;
			dList.add(vendorCode);
		}
		return dList;
	}

}
