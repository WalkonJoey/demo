package com.hqdna.common.commonTools;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

/**
 * 缓存类
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class Cache<T> {

	private String cacheName;
	private Logger logger = Logger.getLogger(this.getClass());
	private ConcurrentMap<String, T> map = new ConcurrentHashMap<String, T>();

	public Cache(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * 增加对象到缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public T putIfAbsent(String key, T value) {
		if (logger.isDebugEnabled()) {
			logger.debug("向缓存：" + this.cacheName + "中添加元素:" + key);
		}
		return map.putIfAbsent(key, value);
	}

	/**
	 * 强迫增加对象到缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public T putForce(String key, T value) {
		if (logger.isDebugEnabled()) {
			logger.debug("向缓存：" + this.cacheName + "中添加元素:" + key);
		}
		return map.put(key, value);
	}

	/**
	 * 从缓存中获取对象
	 * 
	 * @param key
	 * @return
	 */
	public T getFromCache(String key) {
		return map.get(key);
	}

	/**
	 * 是否存在相应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	/**
	 * 从缓存中删除对象
	 * 
	 * @param key
	 */
	public void removeFromCache(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("从缓存：" + this.cacheName + "中删除元素:" + key);
		}
		map.remove(key);
	}

	public ConcurrentMap<String, T> getCacheMap() {
		return this.map;
	}

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<T> getList() {
		return Arrays.asList((T[]) this.map.values().toArray());
	}

	public int getSize() {
		return this.map.size();
	}

	protected void log(String message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}
}
