package com.hqdna.common.baseController;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hqdna.common.base.AbstractBase;
import com.hqdna.common.page.PageInfo;
import com.hqdna.permission.util.IPermConstants;
import com.hqdna.permission.vo.PermItem;

public class BaseController extends AbstractBase {
	/**
	 * 获取sql过滤map
	 * 
	 * @return
	 */
	protected Map<String, Object> getWhereSqlMap(HttpServletRequest request,
			PageInfo pgInfo) {
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		PermItem[] permItems = (PermItem[]) request
				.getAttribute(IPermConstants.PERM_SQL);
		if (permItems == null || permItems.length == 0) {
			return whereSqlMap;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < permItems.length; i++) {
			PermItem pi = permItems[i];
			if (pi.getWhereSql() != null
					&& pi.getWhereSql().trim().length() > 0) {
				sb.append(" ").append(pi.getWhereSql()).append(" ");
			}else{
				sb.append(" (1=1) ");
			}
		}
		if(sb.toString().trim().length()>0){
			whereSqlMap.put("("+sb.toString()+")", null);
		}
		return whereSqlMap;
	}

	/**
	 * 获取sql过滤map
	 * 
	 * @return
	 */
	protected Map<String, Object> getWhereSqlMap(PageInfo pgInfo) {
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();

		return whereSqlMap;
	}

	/**
	 * 获取排序map
	 * 
	 * @return
	 */
	protected LinkedHashMap<String, String> getOrderMap(PageInfo pgInfo) {
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		if (pgInfo.getSort() != null && !pgInfo.getSort().trim().equals("")
				&& pgInfo.getOrder() != null) {
			orderMap.put(pgInfo.getSort(), pgInfo.getOrder());
		}
		return orderMap;
	}

	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception ex) {

		request.setAttribute("ex", ex);
		ex.printStackTrace();
		logger.error(ex.getMessage());
		return "error";
	}

	
	/**
	 * 下载Excel模板方法
	 * 
	 * @param request
	 * @param response
	 * @param storeName 存放在服务器上的模板路径名称
	 * @param contentType 
	 * @param exportName  导出给用户的模板名称
	 * @throws Exception
	 */
	protected void downloadExcelTemplate(HttpServletRequest request,
			HttpServletResponse response, String storeName, String contentType,
			String realName) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		String ctxPath = request.getSession().getServletContext()
				.getRealPath("web/images/upload/excelTemplate/");
		String downLoadPath = ctxPath + File.separator + storeName;

		long fileLength = new File(downLoadPath).length();

		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}
}
