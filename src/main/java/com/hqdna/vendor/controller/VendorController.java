package com.hqdna.vendor.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.mv.excel.DynamicAttrExcel;
import com.hqdna.common.page.Combobox;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.permission.util.IPermConstants;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.Perm;
import com.hqdna.permission.vo.PermType;
import com.hqdna.user.service.IUserService;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;
import com.hqdna.vendor.service.IVendorService;
import com.hqdna.vendor.vo.VendorVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 供应商控制器类
 * 
 */
@Controller
@RequestMapping("/vendor")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class VendorController extends BaseController {
	@Resource(name = "vendorService")
	private IVendorService vendorService;
	@Resource(name = "userService")
	private IUserService userService;
	@RequestMapping(params = "vendorManagePage")
	public String forward() {
		return "vendorMgr/vendorList";
	}

	/**
	 * 获取所有供应商的json列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVendorList", produces = "application/json;charset=UTF-8")
	public Object getUsersJson(HttpServletRequest request,
			HttpServletResponse response, PageInfo pgInfo, HttpSession session,String vendorName,String vendorCode)
			throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		
		Map<String, Object> whereSqlMap = getWhereSqlMap(request,pgInfo);
		String permSql = (String) request.getAttribute(IPermConstants.PERM_SQL);
		if (!StringUtils.isEmpty(permSql)) {
			whereSqlMap.put(permSql, null);
		}
		// 添加查询条件
		if (vendorName != null&& !vendorName.trim().equals("")) {
				whereSqlMap.put("  o.vendorName like '%%"
						+ vendorName.trim() + "%%' ", null);
			}
		if (vendorCode != null
					&& !vendorCode.trim().equals("")) {
				whereSqlMap.put("  o.vendorCode like '%%"
						+ vendorCode.trim() + "%%' ", null);
		}
		// 排序
		LinkedHashMap<String, String> order = getOrderMap(pgInfo);
		QueryResult<VendorVo> qr = vendorService.getVendorList(currentUser, pgInfo, whereSqlMap, order);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", qr.getTotalrecord());
		attributes.put("rows", qr.getResultlist());
		return attributes;
	}

	/**
	 * 
	 * 转向新增编辑页面
	 * 
	 * @param currentUser
	 * @throws IOException
	 */
	@RequestMapping(value = "/fd/{page}/{vendorID}")
	public ModelAndView forwardPage(
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			@PathVariable int vendorID, @PathVariable int page)
			throws IOException {
		ModelAndView mv = new ModelAndView();
		if (page == 0) {
			VendorVo vendorVo = vendorService.getVendorByID(vendorID);
			mv.addObject("vendor", vendorVo);
			mv.setViewName("vendorMgr/vendorAddOrUpdate");
		}
		return mv;
	}

	/**
	 * 
	 * 新增
	 * 
	 */
	@RequestMapping(value = "/saveVendor/{vendorID}", method = RequestMethod.POST)
	@ResponseBody
	public VendorVo saveVendor(HttpSession session, @PathVariable int vendorID,
			VendorVo vendor) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		if (vendorID <= 0) {
			vendorService.createNewVendor(currentUser, vendor);
		} else {
			vendor.setVendorID(vendorID);
			vendorService.updateVendor(currentUser, vendor);
		}
		return vendor;
	}

	/**
	 * 
	 * 删除方法
	 * 
	 */
	@RequestMapping(value = "/deleteVendor")
	@ResponseBody
	public Json deleteVendor(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String ids) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			String[] idArray = ids.split(",");
			int[] newIDs = new int[idArray.length];
			for (int i = 0; i < idArray.length; i++) {
				newIDs[i] = Integer.parseInt(idArray[i]);
			}
			vendorService.deleteVendorByID(currentUser, newIDs);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 
	 * 条件查询
	 * 
	 */
	@RequestMapping("/queryVendorList")
	public ModelAndView queryVendorList(PageInfo pgInfo,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			HttpServletRequest request, HttpSession session) throws IOException {

		// 过滤SQL
		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);

		// String permSql = (String)
		// request.getAttribute(IPermConstants.PERM_SQL);
		// if (!StringUtils.isEmpty(permSql)) {
		// whereSqlMap.put(permSql, null);
		// }

		// 查询条件
		String vendorCode = request.getParameter("vendorCode");
		String vendorName = request.getParameter("vendorName");
		String documentaryID = request.getParameter("documentaryID");
		if (vendorCode != null && !vendorCode.trim().equals("")) {
			whereSqlMap.put("  o.vendorCode like '%" + vendorCode.trim()
					+ "%' ", null);
		}

		if (documentaryID != null && !documentaryID.trim().equals("")) {
			whereSqlMap.put("  o.documentary.userID)  ='" + documentaryID.trim()
					+ "' ", null);
		}
		
		if (vendorName != null && !vendorName.trim().equals("")) {
			whereSqlMap.put("  lower(o.vendorName) like '%" + vendorName.trim().toLowerCase()
					+ "%' ", null);
		}
		QueryResult<VendorVo> vos = vendorService.getVendorList(currentUser,
				pgInfo, whereSqlMap, getOrderMap(pgInfo));

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", vos.getTotalrecord());
		attributes.put("rows", vos.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}

	
	
	@RequestMapping("/queryVendorFilterPayable")
	public ModelAndView queryVendorFilterPayable(PageInfo pgInfo,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			HttpServletRequest request, HttpSession session) throws IOException {

		// 过滤SQL
		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);

		// 查询条件
		String vendorCode = request.getParameter("vendorCode");
		String vendorName = request.getParameter("vendorName");
		String documentaryID = request.getParameter("documentaryID");
		if (vendorCode != null && !vendorCode.trim().equals("")) {
			whereSqlMap.put("  o.vendorCode like '%" + vendorCode.trim()
					+ "%' ", null);
		}

		if (documentaryID != null && !documentaryID.trim().equals("")) {
			whereSqlMap.put("  o.documentary.userID)  ='" + documentaryID.trim()
					+ "' ", null);
		}
		
		if (vendorName != null && !vendorName.trim().equals("")) {
			whereSqlMap.put("  lower(o.vendorName) like '%" + vendorName.trim().toLowerCase()
					+ "%' ", null);
		}
		QueryResult<VendorVo> vos = vendorService.getVendorList(currentUser,
				null, whereSqlMap, getOrderMap(pgInfo));
		List<VendorVo> vendorList = vos.getResultlist();
		//获取所有未审批通过的采购订单对应的供应商
		//List<String> vendorCodeList = vendorService.getAllUnDealPoVendorCode();
		/*List<String> vendorCodeList = vendorService.getAllUnDealPayableVendorCode();
		
		if(vendorCodeList!=null&&vendorCodeList.size()>0){
			Iterator<VendorVo> iterator = vendorList.iterator();
			while(iterator.hasNext()){
				VendorVo tempVendor = iterator.next();
				if(vendorCodeList.contains(tempVendor.getVendorCode())){
					iterator.remove();
				}
			}
		}*/
		/*for (VendorVo vendorVo : vendorList) {
			if(vendorService.isNeedPayable(vendorVo.getVendorCode())){
				vendorVo.setComment("有应付");
			}else{
				vendorVo.setComment("");
			}
			
		}*/
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", vendorList.size());
		attributes.put("rows", vendorList);
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}
	@RequestMapping("/getAllDocumentary")
	@ResponseBody
	public String getAllDocumentary(
			HttpServletRequest request, HttpSession session) throws IOException {
		List<String> docList = vendorService.getAllDocumentary();
		
		JSONArray jsonArray = new JSONArray();
		for (String userID : docList) {
			UserInfo documentary = userService.queryUserByID(userID);
			Combobox tNode = new Combobox();
			tNode.setId(documentary.getUserID());
			tNode.setText(documentary.getUserCnName());
			JSONObject json = JSONObject.fromObject(tNode);
			jsonArray.add(json);
		}
		return jsonArray.toString();
	}
	/**
	 * po查询时，根据跟单人查询
	 * @param request
	 * @param response
	 * @param session
	 * @param pgInfo
	 * @return
	 */
	@RequestMapping(value = "/getDocumentary4Dialog")
	public ModelAndView getDocumentary4Dialog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, PageInfo pgInfo) {

		ModelAndView mav = new ModelAndView();
		Map<String, Object> attributes = new HashMap<String, Object>();
			// 0的时候表示查询可以设置为跟单的人员
		QueryResult<UserInfo> users = userService.getAllDocumentary();
		attributes.put("total", users.getTotalrecord());
		attributes.put("rows", users.getResultlist());

		mav.addAllObjects(attributes);
		return mav;
	}
	/**
	 * 获取所有供应商的列表,供选择对话框使用 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVendorList4Dialog")
	public ModelAndView getVendorList4Dialog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,VendorVo vendorVo, PageInfo pgInfo)
			throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		// 过滤SQL
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		// 添加查询条件
		if (vendorVo.getVendorName() != null&& !vendorVo.getVendorName().trim().equals("")) {
				whereSqlMap.put("  o.vendorName like '%%"
						+ vendorVo.getVendorName().trim() + "%%' ", null);
		}
		if (vendorVo.getVendorCode() != null&& !vendorVo.getVendorCode().trim().equals("")) {
				whereSqlMap.put("  o.vendorCode like '%%"
						+ vendorVo.getVendorCode().trim() + "%%' ", null);
		}
		//只查询启用了的供应商
		whereSqlMap.put(" isEnable=? ", (byte)1);
		QueryResult<VendorVo> vos = vendorService.getVendorList(currentUser,
				pgInfo, whereSqlMap, null);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", vos.getTotalrecord());
		attributes.put("rows", vos.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}

	/**
	 * 查看供应商详情 added by Aaron on 20141008
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/view/{vendorID}")
	public ModelAndView getVendorView(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable int vendorID) throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		// 过滤SQL
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		if (vendorID != 0) {
			whereSqlMap.put(" vendorID=? ", vendorID);
		}
		QueryResult<VendorVo> vos = vendorService.getVendorList(currentUser,
				null, whereSqlMap, null);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("vendorMgr/vendorView");
		mav.addObject("vendor", vos.getResultlist().get(0));
		return mav;
	}

	/**
	 * 获取供应商,供TAB选项卡用 added by Aaron on 20140929
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVendorTab/{vendorID}")
	public ModelAndView getVendorTab(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable int vendorID) throws IOException {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		// 过滤SQL
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		if (vendorID != 0) {
			whereSqlMap.put(" vendorID=? ", vendorID);
		}
		QueryResult<VendorVo> vos = vendorService.getVendorList(currentUser,
				null, whereSqlMap, null);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("component/vendorTab");
		mav.addObject("vendor_tab", vos.getResultlist().get(0));
		return mav;
	}

	
	@RequestMapping(value = "/importDynAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Perm(module = Module.purchase, type = PermType.Edit)
	@ResponseBody
	public Json importDynAttr(HttpServletRequest request,
			@RequestParam MultipartFile file,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser) {
		Json json = new Json();
		String excelStr = "";
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("导入的文件为:" + file.getOriginalFilename());
			}
			// 获取要导数据到数据库的excel文件的名字
			String filename = file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf(".") + 1)
					.toLowerCase();
			InputStream input = file.getInputStream();
			DynamicAttrExcel attrExcel = new DynamicAttrExcel();

			// 判断excel文件后缀名xls和xlsx
			if ("xls".equals(filename) || "xlsx".equals(filename)) {// 导入2003版excel或导入2007或以上版excel
				List<Map<String,Object>> attrList = attrExcel.readExcelByInputStream(input,VendorVo.class);
				if (attrList.size() > 0) {
					vendorService.updateTaxRate(attrList,currentUser);
					if (logger.isDebugEnabled()) {
						logger.debug("Excel导入数据有:" + attrList.size() + "条");
					}
					json.setSuccess(true);
					json.setMsg(excelStr);

				} else {
					excelStr = "导入的excel内容为空！";
					if (logger.isDebugEnabled()) {
						logger.debug(excelStr);
					}
					json.setSuccess(false);
					json.setMsg(excelStr);
				}

			} else {
				excelStr = "不支持文件的类型,导入失败！";
				if (logger.isDebugEnabled()) {
					logger.debug(excelStr);
				}
				json.setSuccess(false);
				json.setMsg(excelStr);
			}
		}catch (IOException e1) {
			e1.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e1.getMessage());
		}catch (RuntimeException e2) {
			e2.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e2.getMessage());
		}
		return json;
	}
}
