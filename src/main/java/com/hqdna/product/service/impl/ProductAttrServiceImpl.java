package com.hqdna.product.service.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.commonTools.ObjectUtils;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.dao.IProdAttrValDao;
import com.hqdna.product.dao.IProductAttrDao;
import com.hqdna.product.service.IProductAttrService;
import com.hqdna.product.vo.AttrGroupVo;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.user.vo.UserInfo;

@Service("productAttrService")
public class ProductAttrServiceImpl extends AbstractBase implements IProductAttrService {
	@Resource(name = "productAttrDao")
	private IProductAttrDao productAttrDao;
	@Resource(name = "prodAttrValDao")
	private IProdAttrValDao prodAttrValDao;
	
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo) {
		return getAttrNameList(currentUser, pgInfo, null, null);
	}

	
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap) {
		return getAttrNameList(currentUser, pgInfo, whereSqlMap, null);
	}

	
	public QueryResult<AttrNameVo> getAttrNameList(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		if (pgInfo == null) {
			return productAttrDao.queryAllAttrNames(-1, -1, whereSqlMap, order);
		} else {
			return productAttrDao.queryAllAttrNames(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}
	
	
	public AttrNameVo getAttrNameById(int attrID) {
		return productAttrDao.getAttrNameById(attrID);
	}

	
	public QueryResult<AttrValueVo> getAttrValue(UserInfo currentUser,
			PageInfo pgInfo, Map<String, Object> whereSqlMap,
			LinkedHashMap<String, String> order) {
		
		if (pgInfo == null) {
			return prodAttrValDao.queryAllAttrValues(-1, -1, whereSqlMap, order);
		} else {
			return prodAttrValDao.queryAllAttrValues(pgInfo.getStartIndex(),
					pgInfo.getRows(), whereSqlMap, order);
		}
	}
	
	
	public List<AttrValueVo> getAllAttrVal() {
		return prodAttrValDao.getAllAttrVal();
	}
	
	
	public int createNewAttrName(UserInfo currentUser, AttrNameVo attrNameVo) {
		return productAttrDao.addAttrName(currentUser, attrNameVo);
	}
	
	
	public int updateAttrName(UserInfo currentUser, AttrNameVo attrNameVo) {
		return productAttrDao.updateAttrName(currentUser, attrNameVo);
	}
	
	
	public int createNewAttrValue(UserInfo currentUser, AttrValueVo attrValue) {
		return prodAttrValDao.addAttrValue(currentUser, attrValue);
	}
	

	
	public int updateAttrValue(UserInfo currentUser, AttrValueVo attrValueVo) {
		return prodAttrValDao.updateAttrValue(currentUser, attrValueVo);
	}
	

	
	public boolean canDeleteAttr(UserInfo currentUser, int attrID) {
		return productAttrDao.canDeleteAttr(currentUser, attrID);
	}
	
	
	public boolean deleteAttrNameByID(UserInfo currentUser, int attrID) {
		return productAttrDao.deleteAttrNameByID(currentUser, attrID);
	}

	
	public void deleteAttrValueByID(UserInfo currentUser, int attrValueID) {
		prodAttrValDao.deleteAttrValueByID(currentUser, attrValueID);
	}
	
	
	public List<AttrValueVo> getAttrValueById(int attrID) {
		return prodAttrValDao.getAttrValueById(attrID);
	}
	

	
	public void updateDynProductAttr(List<Map<String, Object>> dynProdAttrList,
			UserInfo currentUser) {
		for (Map<String,Object> dynProdAttrMap : dynProdAttrList) {
			productAttrDao.updateDynProductAttr(dynProdAttrMap,currentUser);
		}
		
	}
	
	
	public int createNewAttrGroup(UserInfo currentUser, AttrGroupVo attrGroup) {
		return productAttrDao.addAttrGroup(currentUser, attrGroup);
	}
	
	@Transactional
	
	public void assignAttr4AttrGroup(UserInfo currentUser, int attrGroupID,
			List<Integer> beforeAttrNames, List<Integer> afterAttrNames) {
		if (beforeAttrNames != null) {
			if (afterAttrNames != null) {
				for (Iterator<Integer> it = beforeAttrNames.iterator(); it
						.hasNext();) {
					Integer attrID = it.next();
					if (afterAttrNames.contains(attrID)) {
						it.remove();
						afterAttrNames.remove(attrID);
					}
				}
			}
			// 先删除
			productAttrDao.deteleAttr4AttrGroup(currentUser, attrGroupID,
					ObjectUtils.toIntArray(beforeAttrNames));
		}

		// 后增加
		productAttrDao.addAttr4AttrGroup(currentUser, attrGroupID,
				ObjectUtils.toIntArray(afterAttrNames));
	}

}
