/**
 * 
 */
package com.hqdna.common.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.hqdna.user.util.IUserConstants;

/**
 * 过滤器 过滤图片以及一些不用登录就能访问的链接
 */
public class ValidateFilter implements Filter {

	public static boolean isContains(String container, String[] regx) {
		boolean result = false;

		for (int i = 0; i < regx.length; i++) {
			if ((container.indexOf(regx[i])) != -1) {
				return true;
			}
		}
		return result;
	}

	public FilterConfig config;

	public void setFilterConfig(FilterConfig config) {
		this.config = config;
	}

	public FilterConfig getFilterConfig() {
		return config;
	}

	@SuppressWarnings("static-access")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpreq = (HttpServletRequest) request;
		HttpServletResponse httpres = (HttpServletResponse) response;
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(
				(HttpServletResponse) response);
		String logonStrings = config.getInitParameter("logonStrings");
		String includeStrings = config.getInitParameter("includeStrings");
		String excludeStrings = config.getInitParameter("excludeStrings");
		String redirectPath = httpreq.getContextPath()
				+ config.getInitParameter("redirectPath");
		String disabletestfilter = config.getInitParameter("disabletestfilter");

		if (disabletestfilter.toUpperCase().equals("Y")) {
			String[] logonList = logonStrings.split(";");
			String[] includeList = includeStrings.split(";");
			String[] excludeList = excludeStrings.split(";");
			Object user = httpreq.getSession().getAttribute(
					IUserConstants.CURRENT_USER);

			if (user == null) {
				String projName = httpreq.getContextPath(); // 获取项目名称

				/* 通过项目名称访问系统，系统默认会自动跳转到web.xml中节点<welcome-file-list>指定值页面 */
				if ((projName + "/").equals(httpreq.getRequestURI())) {// 跳转到index.jsp
					chain.doFilter(request, response);
					return;
				}

				/* 系统初始化，跳转到登录页面 */
				if ((projName + "/system/init/")
						.equals(httpreq.getRequestURI())
						|| (projName + "/system/init").equals(httpreq
								.getRequestURI())) {
					chain.doFilter(request, response);
					return;
				}

				/* 过滤js文件，不能用isContains函数，因为.jsp就包含.js字符 */
				if (httpreq.getRequestURI().endsWith(".js")) {
					chain.doFilter(request, response);
					return;
				}

				if (this.isContains(httpreq.getRequestURI(), excludeList)) {
					chain.doFilter(request, response);
					return;
				}

				if (this.isContains(httpreq.getRequestURI(), logonList)) {
					Map<String, String[]> params = httpreq.getParameterMap();
					if (params != null && params.get("user.loginName") != null
							&& httpreq.getMethod().equalsIgnoreCase("Post")) {
						chain.doFilter(request, response);
					} else {
						httpres.sendRedirect(redirectPath);
					}
					return;
				}

				if (this.isContains(httpreq.getRequestURI(), includeList)) {
					httpres.sendRedirect(redirectPath);// 部署时要注意配置文件
					return;
				}

				httpres.sendRedirect(redirectPath);
			} else {
				chain.doFilter(request, response);
			}

		}
	}

	public void destroy() {
		this.config = null;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}
}
