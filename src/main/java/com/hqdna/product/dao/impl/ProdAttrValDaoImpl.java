package com.hqdna.product.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.baseDao.DaoSupport;
import com.hqdna.common.page.QueryResult;
import com.hqdna.product.bean.AttrName;
import com.hqdna.product.bean.AttrValue;
import com.hqdna.product.bean.ProductDynamicAttr;
import com.hqdna.product.dao.IProdAttrValDao;
import com.hqdna.product.util.exception.ProductException;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.user.vo.UserInfo;

@Component("prodAttrValDao")
@Transactional
public class ProdAttrValDaoImpl extends DaoSupport<AttrValue> implements IProdAttrValDao {

	/**
	 * 查询属性信息
	 * 
	 * @param attrID
	 * @return
	 */
	private AttrName queryAttrName(int attrID) {
		StringBuilder tsb = new StringBuilder();
		tsb.append("select an from AttrName an where an.attrID=?1");
		List<AttrName> ans = this.executeQuery(tsb.toString(),
				new Object[] { attrID });
		return ans.get(0);
	}
	
	public QueryResult<AttrValueVo> queryAllAttrValues(int startIndex, int rows,
			Map<String, Object> whereSqlMap, LinkedHashMap<String, String> order) {
		QueryResult<AttrValueVo> r = new QueryResult<AttrValueVo>();

		List<AttrValueVo> list = new ArrayList<AttrValueVo>();

		Object[] sql = transferSql(whereSqlMap);

		QueryResult<AttrValue> result = null;
		if (sql == null) {
			result = getScrollData(startIndex, rows, null, null, order);
		} else {
			result = getScrollData(startIndex, rows, (String) sql[0],
					(Object[]) sql[1], order);
		}

		List<AttrValue> products = (List<AttrValue>) result.getResultlist();
		try {
			for (AttrValue p : products) {
				AttrValueVo v_p = new AttrValueVo();
				p.transferPo2Vo(v_p, true);
				list.add(v_p);
			}
		} catch (Exception e) {
			throw new ProductException("产品查询Dao出错了", e);
		}
		r.setResultlist(list);
		r.setTotalrecord(result.getTotalrecord());
		return r;
	}
	
	
	public List<AttrValueVo> getAllAttrVal() {
		StringBuilder tsb = new StringBuilder();
		tsb.append("select an from AttrValue an");
		List<Object> list = this.executeQuery(tsb.toString(), null);
		List<AttrValueVo> avvList = null;
		if (list.size() > 0) {
			avvList = new ArrayList<AttrValueVo>();
			for (Object obj : list) {
				AttrValueVo avv = new AttrValueVo();
				((AttrValue) obj).transferPo2Vo(avv, true);
				avvList.add(avv);
			}
		}
		return avvList;
	}
	
	
	public int addAttrValue(UserInfo currentUser, AttrValueVo attrValue) {
		AttrName an = queryAttrName(attrValue.getAttrID());
		AttrValue av = new AttrValue();
		av.transferPo2Vo(attrValue, false);
		av.setAttrName(an);
		em.persist(av);
		return av.getAttrValueID();
	}
	
	
	public int updateAttrValue(UserInfo currentUser, AttrValueVo attrValueVo) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("update AttrValue av set av.valueName = ?1,av.valueCode=?2,av.comment = ?3 where av.attrValueID = ?4");
			Object[] params = new Object[] { attrValueVo.getValueName(),
					attrValueVo.getValueCode(), attrValueVo.getComment(),
					attrValueVo.getAttrValueID() };
			int num = executeUpdate(sb.toString(), params);
			return num;
		} catch (Exception e) {
			throw new ProductException("Dao产品更新出错", e);
		}
	}

	
	public boolean deleteAttrValueByID(UserInfo currentUser, int attrValueID) {
		StringBuilder sb = new StringBuilder();
		sb.append("select av from AttrValue av where av.attrValueID=?1");
		List<AttrValue> obj = this.<AttrValue> executeQuery(sb.toString(),
				new Object[] { attrValueID });

		if (obj != null && obj.size() == 1) {
			AttrValue avv = (AttrValue) obj.get(0);
			List<ProductDynamicAttr> dynamicAttrs = avv.getDynamicAttrs();
			if (dynamicAttrs == null || dynamicAttrs.isEmpty()) {
				em.remove(avv);
				return true;
			}
		}
		return false;
	}
	
	
	public List<AttrValueVo> getAttrValueById(int attrID) {
		List<AttrValueVo> list = new ArrayList<AttrValueVo>();
		StringBuilder tsb = new StringBuilder();
		tsb.append("select av from AttrValue av where av.attrName.attrID=?1");
		List<AttrValue> avs = this.executeQuery(tsb.toString(),
				new Object[] { attrID });
		for (AttrValue attrValue : avs) {
			AttrValueVo vo = new AttrValueVo();
			attrValue.transferPo2Vo(vo, true);
			list.add(vo);
		}
		return list;
	}
	
}
