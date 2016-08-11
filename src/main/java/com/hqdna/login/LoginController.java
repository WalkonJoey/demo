package com.hqdna.login;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.commonTools.Cache;
import com.hqdna.login.util.exception.LoginException;
import com.hqdna.permission.service.IPermissionService;
import com.hqdna.permission.vo.PermItem;
import com.hqdna.user.service.IUserService;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

@Controller
@RequestMapping("/loginManage")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class LoginController extends BaseController {

	@Resource(name = "userService")
	private IUserService userService;

	@Resource(name = "permissionService")
	private IPermissionService permissionService;

	/**
	 * @Title: LoginController.java
	 * @Package com.woguang.myled.business.login
	 * @author Berry
	 * @Description: 用户登录校验
	 * @param request
	 * @return
	 * @return ModelAndView
	 * @date 2014-7-18 下午1:52:18
	 * @throws
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		ModelAndView mav = new ModelAndView("/main");
		String resultStr = "OK";
		boolean f = false;
		UserInfo currentUser = null;
		String userNameStr = request.getParameter("user.loginName");
		String passwordStr = request.getParameter("user.password");
		String imputtedDigits = request.getParameter("imputtedDigits");
		
		try {
			String randomImage=(String)request.getSession().getAttribute("checkImg");
//			校验验证码，暂时不验证
			if(!randomImage.equals(imputtedDigits)){
				return new ModelAndView("index", "result", "验证码错误");
			}
			
			
			// 获取当前用户
			Object result = userService.login(userNameStr, passwordStr);

			if (result instanceof String) {
				resultStr = (String) result;
			} else {
				// 登录成功，将当前用户的信息存进session以便页面使用
				currentUser = (UserInfo) result;
				/*Cache<UserInfo> cache = cm
						.createAndGetCacheByName(IUserConstants.ONLINE_USERS);
				UserInfo user = cache.putIfAbsent(currentUser.getUserID(),
						currentUser);*/
				Cache<UserInfo> cache = getOnlineUserCache();
				UserInfo user = cache.putIfAbsent(currentUser.getUserID(),
						currentUser);
				if (user != null
						&& !request.getRemoteAddr().equalsIgnoreCase(
								user.getIpAddress())) {
					resultStr = "此账号已经登录，请先退出";
				} else {
					long loginTime = System.currentTimeMillis();
					currentUser.setLoginTime(new Timestamp(loginTime));
					model.addAttribute(IUserConstants.CURRENT_USER, currentUser);
					f = true;
				}
			}
			// 输出登录成功的状态码
		} catch (Exception e) {
			f = false;
			throw new LoginException("登录出错", e);
		}

		if (f) {
			// 加入在线用户缓存中
			currentUser.setIpAddress(request.getRemoteAddr());

			// 获取当前用户的菜单
			List<PermItem> list = new ArrayList<PermItem>();
			Map<String, List<PermItem>> menuList = permissionService
					.getMenusByUser(currentUser);
			for (String str : menuList.keySet()) {
				list.addAll(menuList.get(str));
			}
			Collections.sort(list);
			// mav.addObject("menuList", list);
			request.getSession().setAttribute("menuList", list);
			if (logger.isDebugEnabled()) {
				logger.debug(currentUser.getUserCnName() + "登录成功");
			}
			return mav;
		} else {
			return new ModelAndView("index", "result", resultStr);
		}
	}

	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			SessionStatus status) {
		ModelAndView mav = new ModelAndView(new RedirectView("login"));

		try {
			// 更改用户状态,以及用户登录时间
			userService.logout(currentUser);
		} catch (Exception e) {
			throw new LoginException("退出出错", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("用户<" + currentUser.getUserCnName() + ">成功退出！");
		}

		// 清除缓存
		Cache<UserInfo> cache = cm.getCacheByName(IUserConstants.ONLINE_USERS);
		if (cache != null) {
			cache.removeFromCache(currentUser.getUserID());
		}
		// 清除session
		request.getSession().removeAttribute(IUserConstants.CURRENT_USER);
		status.setComplete();
		return mav;
	}

	@RequestMapping(value = "getMenu")
	public void loadMenu(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, Model model) {
		if (logger.isDebugEnabled()) {
			logger.debug("获取当前有权限的菜单目录");
		}
		// 获取当前用户
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		try {
			List<PermItem> list = new ArrayList<PermItem>();

			Map<String, List<PermItem>> menuList = permissionService
					.getMenusByUser(currentUser);
			for (String str : menuList.keySet()) {
				list.addAll(menuList.get(str));
			}
			Collections.sort(list);
			model.addAttribute("menuList", list);
		} catch (Exception e) {
			throw new LoginException("退出出错", e);
		}
	}

}