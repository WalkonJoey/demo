package com.hqdna.common.baseDao;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hqdna.common.commonTools.GenericsUtils;
import com.hqdna.common.page.QueryResult;

/**
 * 数据对象访问通用抽象类
 * 
 * @param <T>
 */
@SuppressWarnings("rawtypes")
@Transactional
public abstract class DaoSupport<T> implements DAO<T> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this
			.getClass());
	@PersistenceContext
	protected EntityManager em;

	public void clear() {
		em.clear();
	}

	public void delete(Serializable... entityids) {
		for (Object id : entityids) {
			em.remove(em.getReference(this.entityClass, id));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int executeUpdate(String sql, Object[] params) {
		Query query = em.createQuery(sql);
		for (int i = 1; i <= params.length; i++) {
			query.setParameter(i, params[i - 1]);
		}
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int executeByNativeSQL(String sql, Object[] params) {
		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				query.setParameter(i, params[i - 1]);
			}
		}
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public <TX> List<TX> executeQuery(String sql, Object[] params) {
		Query query = em.createQuery(sql);
		if (params != null) {
			for (int i = 1; i <= params.length; i++) {
				query.setParameter(i, params[i - 1]);
			}
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public T find(Serializable entityId) {
		if (entityId == null)
			throw new RuntimeException(this.entityClass.getName()
					+ ":传入的实体id不能为空");
		return em.find(this.entityClass, entityId);
	}

	public T getReference(Serializable entityId) {
		if (entityId == null)
			throw new RuntimeException(this.entityClass.getName()
					+ ":传入的实体id不能为空");
		return em.getReference(this.entityClass, entityId);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public T findByNativeSQL(String sql) {
		Query query = em.createNativeQuery(sql);
		if (sql == null)
			throw new RuntimeException("传入的实体SQL不能为空");
		return (T) query.getSingleResult();
	}

	public void save(T entity) {
		em.persist(entity);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public long getCount() {
		return (Long) em.createQuery(
				"select count(" + getCountField(this.entityClass) + ") from "
						+ getEntityName(this.entityClass) + " o")
				.getSingleResult();
	}

	public void update(T entity) {
		em.merge(entity);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			LinkedHashMap<String, String> orderby) {
		return getScrollData(firstindex, maxresult, null, null, orderby);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams) {
		return getScrollData(firstindex, maxresult, wherejpql, queryParams,
				null);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult) {
		return getScrollData(firstindex, maxresult, null, null, null);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData() {
		return getScrollData(-1, -1);
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		String entityname = getEntityName(this.entityClass);
		Query query = em.createQuery("select distinct o from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql) + buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult);
		qr.setResultlist(query.getResultList());
		query = em.createQuery("select count("
				+ getCountField(this.entityClass)
				+ ") from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.getSingleResult());
		return qr;
	}

	protected static void setQueryParams(Query query, Object[] queryParams) {
		if (queryParams != null && queryParams.length > 0) {
			for (int i = 0; i < queryParams.length; i++) {
				query.setParameter(i + 1, queryParams[i]);
			}
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public <TT> QueryResult<TT> getScrollData(int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Class<?> oneClass) {
		QueryResult<TT> qr = new QueryResult<TT>();
		String entityname = getEntityName(oneClass);
		Query query = em.createQuery("select o from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql) + buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult);
		qr.setResultlist(query.getResultList());
		query = em.createQuery("select count("
				+ getCountField(oneClass)
				+ ") from "
				+ entityname
				+ " o "
				+ (wherejpql == null || "".equals(wherejpql.trim()) ? ""
						: "where " + wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.getSingleResult());
		return qr;
	}

	/**
	 * 组装order by语句
	 * 
	 * @param orderby
	 * @return
	 */
	protected static String buildOrderby(LinkedHashMap<String, String> orderby) {
		StringBuffer orderbyql = new StringBuffer("");
		if (orderby != null && orderby.size() > 0) {
			orderbyql.append(" order by ");
			for (String key : orderby.keySet()) {
				orderbyql.append("o.").append(key).append(" ")
						.append(orderby.get(key)).append(",");
			}
			orderbyql.deleteCharAt(orderbyql.length() - 1);
		}
		return orderbyql.toString();
	}

	/**
	 * 获取实体的名称
	 * 
	 * @param <E>
	 * @param clazz
	 *            实体类
	 * @return
	 */
	protected static <E> String getEntityName(Class<E> clazz) {
		String entityname = clazz.getSimpleName();
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity != null && entity.name() != null
				&& !"".equals(entity.name())) {
			entityname = entity.name();
		}
		return entityname;
	}

	/**
	 * 获取统计属性,该方法是为了解决hibernate解析联合主键select count(o) from Xxx o语句BUG而增加,
	 * hibernate对此jpql解析后的sql为select
	 * count(field1,field2,...),显示使用count()统计多个字段是错误的
	 * 
	 * @param <E>
	 * @param clazz
	 * @return
	 */
	protected static <E> String getCountField(Class<E> clazz) {
		String out = "o";
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector
					.getBeanInfo(clazz).getPropertyDescriptors();
			for (PropertyDescriptor propertydesc : propertyDescriptors) {
				Method method = propertydesc.getReadMethod();
				if (method != null
						&& method.isAnnotationPresent(EmbeddedId.class)) {
					PropertyDescriptor[] ps = Introspector.getBeanInfo(
							propertydesc.getPropertyType())
							.getPropertyDescriptors();
					out = "o."
							+ propertydesc.getName()
							+ "."
							+ (!ps[1].getName().equals("class") ? ps[1]
									.getName() : ps[0].getName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * 将SQL语句从MAP形式转化为标准SQL形式
	 * 
	 * @param whereSqlMap
	 * @return
	 */
	protected Object[] transferSql(Map<String, Object> whereSqlMap) {
		if (whereSqlMap == null) {
			return null;
		}

		StringBuilder hql = new StringBuilder(" 1=1 ");

		Object[] params = null;
		List<Object> listString = new ArrayList<Object>();

		for (String param : whereSqlMap.keySet()) {
			Object value = whereSqlMap.get(param);
			if (param.trim().length() == 0)
				continue;
			if (value == null) {
				hql.append(" and (").append(param).append(") ");
			} else {
				hql.append(" and ").append(param).append(" ");
				listString.add(value);
			}
		}

		params = listString.toArray(new Object[listString.size()]);

		return new Object[] { hql.toString(), params };
	}

	/**
	 * 更新对象属性值
	 * 
	 * @param st
	 *            对象
	 * @param sets
	 *            需要修改的属性名集合
	 * @param wheres
	 *            依据条件的属性名集合
	 * @return 供update调用的SQL语句
	 */
	protected String generateUpdateSql(Object st, String[] sets, String[] wheres) {
		StringBuilder sb = new StringBuilder();
		sb.append(" update ").append(st.getClass().getSimpleName());

		List<Object> params = new ArrayList<Object>();

		Map<String, Object> setMap = new HashMap<String, Object>();
		Map<String, Object> whereMap = new HashMap<String, Object>();
		if (sets != null) {
			for (String setFld : sets) {
				setMap.put(setFld, null);
			}
		}
		for (String setFld : wheres) {
			whereMap.put(setFld, null);
		}
		analysis(st, setMap, whereMap);
		sb.append(" set ");
		int i = 1;
		for (String setFld : setMap.keySet()) {
			sb.append(setFld).append("=?").append(i).append(",");
			params.add(i - 1, setMap.get(setFld));
			i++;
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(" where ");

		for (String whereFld : whereMap.keySet()) {
			sb.append(whereFld).append("=?").append(i).append(",");
			params.add(i - 1, whereMap.get(whereFld));
			i++;
		}
		sb.delete(sb.length() - 1, sb.length());
		return sb.toString();

	}

	protected void analysis(Object st, Map<String, Object> setMap,
			Map<String, Object> whereMap) {
		boolean isEmptyWhenInit = setMap.isEmpty();
		Field[] flds = st.getClass().getDeclaredFields();
		try {
			for (Field fld : flds) {
				Class fldType = fld.getType();
				String fldName = fld.getName();
				Method method = null;
				try {
					method = st.getClass().getMethod(
							"get" + fld.getName().substring(0, 1).toUpperCase()
									+ fld.getName().substring(1));
				} catch (Exception e) {
					continue;
				}

				if (Modifier.isStatic(fld.getModifiers())) {
					continue;
				}
				Object value = null;
				if (fldType.equals(int.class)) {
					Integer varValue = (Integer) method.invoke(st);
					if (varValue >= 0) {
						value = varValue;
					}
				} else if (fldType.equals(long.class)) {
					Long varValue = (Long) method.invoke(st);
					if (varValue >= 0) {
						value = varValue;
					}
				} else if (fldType.equals(float.class)) {
					Float varValue = (Float) method.invoke(st);
					if (varValue >= 0) {
						value = varValue;
					}
				} else if (fldType.equals(double.class)) {
					Double varValue = (Double) method.invoke(st);
					if (varValue >= 0) {
						value = varValue;
					}
				} else {
					Object varValue = method.invoke(st);
					if (varValue != null) {
						value = varValue;
					}
				}

				if (setMap.containsKey(fldName) || isEmptyWhenInit) {
					setMap.put(fldName, value);
				}

				if (whereMap.containsKey(fldName)) {
					whereMap.put(fldName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <TT> List<TT> getDataList(Class<?> entityClass,
			Map<String, Object> whereSqlMap) {
		String entityname = null;
		if (entityClass != null) {
			entityname = entityClass.getSimpleName();
		} else {
			entityname = getEntityName(this.entityClass);
		}

		StringBuilder sql = new StringBuilder("from ");
		sql.append(entityname).append(" o ");
		Object[] whereSql = transferSql(whereSqlMap);
		sql.append(" where ").append(whereSql[0]);
		List<TT> list = executeQuery(sql.toString(), (Object[]) whereSql[1]);
		return list;
	}
}
