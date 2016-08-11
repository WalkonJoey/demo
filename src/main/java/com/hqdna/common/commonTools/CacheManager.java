package com.hqdna.common.commonTools;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理类
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class CacheManager {

	private static CacheManager cm = new CacheManager();

	// 缓存集对象
	private ConcurrentMap<String, Cache<?>> cacheMap = new ConcurrentHashMap<String, Cache<?>>();

	private CacheManager() {
	}

	public static CacheManager getCacheManager() {
		return cm;
	}

	/**
	 * 按指定缓存标记创建缓存
	 * 
	 * @param cacheName
	 */
	public <T> Cache<T> createAndGetCacheByName(String cacheName) {
		cacheMap.putIfAbsent(cacheName, new Cache<T>(cacheName));
		return (Cache<T>) cacheMap.get(cacheName);
	}

	/**
	 * 通过缓存标记获取相应的缓存,如果存在直接返回，不存在返回null
	 * 
	 * @param cacheName
	 *            缓存标记
	 * @return
	 */
	public <T> Cache<T> getCacheByName(String cacheName) {
		return (Cache<T>)cacheMap.get(cacheName);
	}

	/**
	 * 按缓存标记以及缓存中的对象KEY，获取相应的VALUE;只要缓存或KEY任意一个不存在都返回NULL
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public <T> T getCacheValueByName(String cacheName, String key) {
		if (!cacheMap.containsKey(cacheName) || cacheMap.get(cacheName) == null) {
			return null;
		}
		return (T) ((Cache<T>) cacheMap.get(cacheName)).getFromCache(key);
	}

	/**
	 * 按缓存标记以及缓存中的对象KEY，VALUE添加到缓存中
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public <T> boolean addToCache(String cacheName, String key, T value) {
		createAndGetCacheByName(cacheName).putIfAbsent(key, value);
		return true;
	}

	/**
	 * 清除缓存 （这里暂时仅仅是从缓存管理器中清除）
	 * 
	 * @param cacheName
	 */
	public void clear(String cacheName) {
		if (cacheMap.containsKey(cacheName)) {
			cacheMap.remove(cacheName);
		} else {
			cacheMap.clear();
		}
	}
}
