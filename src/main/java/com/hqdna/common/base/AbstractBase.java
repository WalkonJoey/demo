package com.hqdna.common.base;

import javax.annotation.Resource;

import com.hqdna.common.commonTools.Cache;
import com.hqdna.common.commonTools.CacheManager;
import com.hqdna.permission.dao.IPermissionDao;
import com.hqdna.product.dao.ICategoryDao;
import com.hqdna.product.dao.IProdAttrValDao;
import com.hqdna.product.dao.IProductDao;
import com.hqdna.product.service.ICategoryService;
import com.hqdna.product.service.IProductAttrService;
import com.hqdna.product.service.IProductService;
import com.hqdna.user.dao.IUserDao;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.dao.IVendorDao;
/**
 * 基类，创建所有的bean,然后让其他类继承，因为spring本来就是在加载的时候创建所有的实例对象
 * */
public abstract class AbstractBase extends AbstractBaseLogProvider {
	// 缓存管理器
	protected CacheManager cm = CacheManager.getCacheManager();
	@Resource(name="userDao")
	protected IUserDao userDao;
	@Resource(name="permissionDao")
	protected IPermissionDao permissionDao;
	@Resource(name = "productDao")
	protected IProductDao productDao;
	@Resource(name = "prodAttrValDao")
	protected IProdAttrValDao prodAttrValDao;
	@Resource(name = "categoryDao")
	protected ICategoryDao categoryDao;
	@Resource(name= "vendorDao")
	protected IVendorDao vendorDao;
	@Resource(name = "productService")
	protected IProductService productService;
	@Resource(name = "productAttrService")
	protected IProductAttrService productAttrService;
	@Resource(name = "categoryService")
	protected ICategoryService categoryService;
	
	/**
	 * 存放在线用户的cache
	 * @return
	 */
	protected Cache<UserInfo> getOnlineUserCache(){
		return cm.createAndGetCacheByName(IUserConstants.ONLINE_USERS);
	}
	
	
}
