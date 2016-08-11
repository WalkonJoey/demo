package com.hqdna.product.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hqdna.common.baseController.BaseController;
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.mv.excel.DynamicProductExcel;
import com.hqdna.common.mv.excel.ProductExcel;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.Perm;
import com.hqdna.permission.vo.PermType;
import com.hqdna.product.service.ICategoryService;
import com.hqdna.product.service.IProductAttrService;
import com.hqdna.product.service.IProductService;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.AttrValueVo;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

@Controller
@RequestMapping("/productAttrManage")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class ProductAttrController extends BaseController {

	@Resource(name = "productService")
	private IProductService productService;
	@Resource(name = "productAttrService")
	private IProductAttrService productAttrService;
	@Resource(name = "categoryService")
	private ICategoryService categoryService;
	/**
	 * 导航到productCategory.jsp页面
	 * 
	 * @return
	 */
	@RequestMapping(params = "productCategoryPage")
	public String productCategoryPgViaListPg(HttpServletRequest request) {
		// 获取素有属性列表
		List<AttrNameVo> attrNameList = productAttrService.getAttrNameList(
				null, null).getResultlist();
		request.setAttribute("attrList", attrNameList);
		return "/productMgr/productCategory";
	}

	/**
	 * 导航到productAttrMgrPage.jsp页面 这里是点击目录的导航跳转，直接带参数的，注意下面用 params 而不是value
	 * 
	 * @return
	 */
	@RequestMapping(params = "productAttrMgrPage")
	public String productAttrMgrPagePgViaListPg() {
		return "/productMgr/productAttrManage";
	}
	/**
	 * 导航到addOrUpdtPdtAttrName.jsp页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "addOrUpdtPdtAttrNamePg")
	public String addPdtAttrNameViaAddAttrPg(HttpServletRequest request,
			int attrID) {
		if (attrID == -1) {
			request.setAttribute("AttrNameInfo", null);
		} else {
			AttrNameVo anv = productAttrService.getAttrNameById(attrID);
			request.setAttribute("AttrNameInfo", anv);
		}
		return "/productMgr/addOrUpdtPdtAttrName";
	}

	/**
	 * 导航到addOrUpdtPdtAttrVal.jsp页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "addOrUpdtPdtAttrValPg")
	public String addPdtAttrValViaAddAttrPg() {
		return "/productMgr/addOrUpdtPdtAttrVal";
	}

	
	
	/**
	 * 批量导入产品
	 * 
	 * @param request
	 * @param file
	 * @param currentUser
	 * @return
	 */
	@RequestMapping(value = "/importProductAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json importProductAttr(HttpServletRequest request,
			@RequestParam MultipartFile file,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser) {
		
		Json json = new Json();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("导入的文件为:" + file.getOriginalFilename());
			}
			// 获取要导数据到数据库的excel文件的名字
			String filename = file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf(".") + 1)
					.toLowerCase();
			InputStream input = file.getInputStream();
			ProductExcel productExel = new ProductExcel();
			// 判断excel文件后缀名xls和xlsx
			if ("xls".equals(filename) || "xlsx".equals(filename)) {// 导入2003版excel或导入2007或以上版excel
				List<Map<String,Object>> rProdAttrList = productExel.readExcelByInputStream(input);
				if(rProdAttrList!=null&&rProdAttrList.size()>0){
					productService.updateExcelAttr(rProdAttrList);
				}
			}
			json.setSuccess(true);
			json.setMsg("更新成功！");
		}catch(Exception e){
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}


	/*************************************************************************************************
	 * 属性管理界面
	 */

	/**
	 * 获取属性名列表，供datagrid显示
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/findPdtAttrNames4dg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Perm(module = Module.product, type = PermType.View)
	public Object findPdtAttrNamesViaAttrMgrPg(HttpServletRequest request,
			HttpServletResponse response, PageInfo pgInfo, HttpSession session,
			String attrName, String attrCode, String isSearch) {

		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		// 过滤SQL
		Map<String, Object> whereSqlMap = getWhereSqlMap(request, pgInfo);
		// 添加查询条件
		if (isSearch != null) {
			if (attrName != null && !attrName.trim().equals("")) {
				whereSqlMap.put("  lower(o.attrName) like '%%"
						+ attrName.toLowerCase().trim() + "%%' ", null);
			}
			if (attrCode != null && !attrCode.trim().equals("")) {
				whereSqlMap.put("  lower(o.attrCode) like '%%"
						+ attrCode.toLowerCase().trim() + "%%' ", null);
			}
		}
		// 排序
		LinkedHashMap<String, String> order = getOrderMap(pgInfo);

		QueryResult<AttrNameVo> qr = productAttrService.getAttrNameList(
				currentUser, pgInfo, whereSqlMap, order);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", qr.getTotalrecord());
		attributes.put("rows", qr.getResultlist());
		return attributes;

	}

	/**
	 * 通过attrID获取可选属性值列表，供datagrid显示
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param attrID
	 * @return
	 */
	@RequestMapping(value = "/getPdtAttrValByNId4dg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Perm(module = Module.product, type = PermType.View)
	public ModelAndView getPdtAttrValByNId4dgViaAttrMgrPg(
			HttpServletRequest request, HttpServletResponse response,
			PageInfo pgInfo, String isSearch, HttpSession session, int attrID) {

		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		// 过滤SQL
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		// 添加查询条件
		if (isSearch != null) {
			if (attrID > 0) {
				whereSqlMap.put(" o.attrName.attrID = ?", attrID);
			}
		}
		// 排序
		LinkedHashMap<String, String> order = getOrderMap(pgInfo);

		QueryResult<AttrValueVo> attrValues = productAttrService.getAttrValue(
				currentUser, pgInfo, whereSqlMap, order);
		ModelAndView mav = new ModelAndView();
		/*
		 * List<AttrValueVo> attrValues =
		 * productService.getAttrValueById(attrID);
		 */
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", attrValues.getTotalrecord());
		attributes.put("rows", attrValues.getResultlist());
		mav.addAllObjects(attributes);

		return mav;
	}

	/**
	 * 获取所有的属性值
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllAttrVal", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getAllAttrValViaAttrMgrPg(HttpServletRequest request,
			HttpSession session) {
		Json json = new Json();
		try {
			List<AttrValueVo> attrValues = productAttrService.getAllAttrVal();
			json.setObj(attrValues);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 增加新属性名
	 * 
	 * @param request
	 * @param session
	 * @param attrNameVo
	 * @return
	 */
	@RequestMapping(value = "/addPdtAttrName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json addPdtAttrNameViaAddAttrPg(HttpServletRequest request,
			HttpSession session, AttrNameVo attrNameVo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			attrNameVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			productAttrService.createNewAttrName(currentUser, attrNameVo);
			/*
			 * AttrGroupVo attrGroupVo = new AttrGroupVo();
			 * attrGroupVo.setComment(category.getComment()); int attrGroupID =
			 * productService.createNewAttrGroup(currentUser,
			 * attrGroupVo);//单一的创建组
			 * productService.assignAttr4AttrGroup(currentUser, attrGroupID,
			 * null, iattrIDs);//Arrays.asList(attrID)
			 * productService.assignAttrGroup4Category(currentUser, categoryID,
			 * -1, attrGroupID);
			 */
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 更新属性名
	 * 
	 * @param request
	 * @param session
	 * @param attrNameVo
	 * @return
	 */
	@RequestMapping(value = "/updatePdtAttrName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json updatePdtAttrNameViaAddAttrPg(HttpServletRequest request,
			HttpSession session, AttrNameVo attrNameVo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			attrNameVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			int result = productAttrService.updateAttrName(currentUser, attrNameVo);
			if (result == 1) {
				json.setSuccess(true);
			} else {
				json.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 增加属性名的对应可选属性值
	 * 
	 * @param request
	 * @param session
	 * @param attrValueVo
	 * @return
	 */
	@RequestMapping(value = "/addPdtAttrValue", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json addPdtAttrValueViaAddAttrPg(HttpServletRequest request,
			HttpSession session, AttrValueVo attrValueVo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			attrValueVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			productAttrService.createNewAttrValue(currentUser, attrValueVo);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 更新属性值
	 * 
	 * @param request
	 * @param session
	 * @param attrValueVo
	 * @return
	 */
	@RequestMapping(value = "/updatePdtAttrValue", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json updatePdtAttrValueViaAddAttrPg(HttpServletRequest request,
			HttpSession session, AttrValueVo attrValueVo) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			attrValueVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			int result = productAttrService.updateAttrValue(currentUser,
					attrValueVo);
			if (result == 1) {
				json.setSuccess(true);
			} else {
				json.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 删除属性，包括所有关联
	 * 
	 * @param request
	 * @param session
	 * @param attrID
	 * @return
	 */
	@RequestMapping(value = "/deleteAttr", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json deleteAttrViaAddAttrPg(HttpServletRequest request,
			HttpSession session, int attrID) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			boolean canDelete = productAttrService.canDeleteAttr(currentUser,
					attrID);// 先判断是否关联了分组
			if (canDelete) {
				// productService.deleteAttrValuesByAttrId(currentUser,
				// attrID);//再删除属性值表中该属性的所有属性值记录
				boolean result = productAttrService.deleteAttrNameByID(currentUser,
						attrID);// 最后删除属性名称记录
				if (result) {
					json.setSuccess(true);
				} else {
					json.setSuccess(false);
				}
			} else {
				json.setSuccess(false);
				json.setMsg("属性已关联分类，不能删除！如要删除，请先删除分类。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 删除属性的可选属性值
	 * 
	 * @param request
	 * @param session
	 * @param attrValueID
	 * @return
	 */
	@RequestMapping(value = "/deleteAttrValue", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json deleteAttrValueViaAddAttrPg(HttpServletRequest request,
			HttpSession session, int attrValueID) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			productAttrService.deleteAttrValueByID(currentUser, attrValueID);
			json.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 设置产品所有的属性名和属性值到attribute，返回给页面展示
	 * 
	 * @param request
	 * @param session
	 * @param categoryID
	 * @return
	 */
	@RequestMapping(value = "/setAttrNameAndValue", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json setAttrNameAndValue(HttpServletRequest request,
			HttpSession session, int categoryID) {
		Json json = new Json();
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = null;
			List<Integer> attrIdList = categoryService
					.getAttrsID4Category(categoryID);
			for (Integer attrID : attrIdList) {
				map = new HashMap<String, Object>();
				AttrNameVo anv = productAttrService.getAttrNameById(attrID);
				map.put("attrName", anv);
				List<AttrValueVo> valueList = productAttrService
						.getAttrValueById(attrID);
				map.put("attrValueList", valueList);
				list.add(map);
			}
			json.setObj(list);
			json.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}


	/*@RequestMapping(value = "/getDynamicAttrByProdID", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getDynamicAttrByProdIDViaUpdatePg(HttpServletRequest request,
			HttpSession session, int productID) {
		Json json = new Json();
		try {
			ProductVo productVo = productService.getProductByID(productID);
			Set<ProductDynamicAttrVo> pdaSet = productVo
					.getProductDynamicAttr();
			if (pdaSet != null) {
				List<Map<String, Integer>> pdaMapList = new ArrayList<Map<String, Integer>>();
				for (ProductDynamicAttrVo productDynamicAttrVo : pdaSet) {
					Map<String, Integer> tempMap = new HashMap<String, Integer>();
					tempMap.put("attrName",
							productDynamicAttrVo.getAttrNameID());
					tempMap.put("attrValue",
							productDynamicAttrVo.getAttrValueID());
					pdaMapList.add(tempMap);
				}
				json.setObj(pdaMapList);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}*/
	/**
	 * 通过导入Excel的第一行判断更新产品的哪一个字段
	 * @param request
	 * @param file
	 * @param currentUser
	 * @return
	 */
	@RequestMapping(value = "/importDynProductAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json importDynProductAttr(HttpServletRequest request,
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
			DynamicProductExcel stockReferExcel = new DynamicProductExcel();

			// 判断excel文件后缀名xls和xlsx
			if ("xls".equals(filename) || "xlsx".equals(filename)) {// 导入2003版excel或导入2007或以上版excel
				List<Map<String,Object>> rStockReferList = stockReferExcel.readExcelByInputStream(input);
				if (rStockReferList.size() > 0) {
					productAttrService.updateDynProductAttr(rStockReferList,currentUser);
					if (logger.isDebugEnabled()) {
						logger.debug("Excel导入数据有:" + rStockReferList.size() + "条");
					}
					if (logger.isDebugEnabled()) {
						logger.debug(excelStr);
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