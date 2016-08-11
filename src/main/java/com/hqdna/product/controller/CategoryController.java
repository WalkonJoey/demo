package com.hqdna.product.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.common.page.TreeNode;
import com.hqdna.product.bean.AttrGroup;
import com.hqdna.product.service.ICategoryService;
import com.hqdna.product.service.IProductAttrService;
import com.hqdna.product.util.exception.ProductException;
import com.hqdna.product.vo.AttrGroupVo;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.CategoryVo;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/categoryManage")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class CategoryController extends BaseController {

	@Resource(name = "categoryService")
	private ICategoryService categoryService;
	@Resource(name = "productAttrService")
	private IProductAttrService productAttrService;

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
	 * 获取产品分类中的大类，用作新增供应商时的供应商代码
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param pgInfo
	 * @param vendorID
	 * @return
	 */
	@RequestMapping(value = "/getCategoryList4Dialog")
	public ModelAndView getProductCategory4Dialog(HttpServletRequest request,
			HttpSession session, PageInfo pgInfo, String isSearch,
			String param_name, String param_value) {
		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);
		String name = request.getParameter("param_name");
		String value = request.getParameter("param_value");
		if (name != null && value != null) {
			String sqlName = "o." + name;
			whereSqlMap.put(
					"lower(" + sqlName + ") like '%" + value.toLowerCase()
							+ "%'", null);
		}
		whereSqlMap.put("categoryParentID = ?", 0);
		LinkedHashMap<String, String> order = getOrderMap(pgInfo);
		QueryResult<CategoryVo> levelOneCategoryList = categoryService
				.getProductLevelOneCategory(pgInfo, whereSqlMap, order);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", levelOneCategoryList.getTotalrecord());
		attributes.put("rows", levelOneCategoryList.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}


	/**
	 * 返回产品分类树    productCategory.jsp页面
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getPtCateloryTree", produces = "application/json;charset=UTF-8")
	// 加上后面的一句返回的数据不会出现乱码
	@ResponseBody
	public String getRolePermTreeViaAssignPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		// 获取原有的权限
		Map<Integer, List<CategoryVo>> cateloryMap = categoryService
				.getProductCategory();
		//生成NC的分类编码
		/*int i = 0;
		for (int categoryID : cateloryMap.keySet()) {
			int j = 1;
			if(i==0){
				for (CategoryVo vo : cateloryMap.get(categoryID)) {
					if(j<10){
						vo.setNcCode("WG0"+j);
					}else{
						vo.setNcCode("WG"+j);
					}
					j++;
					categoryService.updateCategory(vo);
				}
			}else if(i>0&&i<10){
				for (CategoryVo vo : cateloryMap.get(categoryID)) {
					if(j<10){
						vo.setNcCode("WG0"+i+"0"+j);
					}else{
						vo.setNcCode("WG0"+i+""+j);
					}
					j++;
					categoryService.updateCategory(vo);
				}
			}else{
				for (CategoryVo vo : cateloryMap.get(categoryID)) {
					if(j<10){
						vo.setNcCode("WG"+i+"0"+j);
					}else{
						vo.setNcCode("WG"+i+""+j);
					}
					j++;
					categoryService.updateCategory(vo);
				}
			}
			i++;
		}*/
		TreeNode rootTree = new TreeNode();
		rootTree.setId(String.valueOf("0"));
		rootTree.setText("所有分类");
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("categoryID", 0);
		rootTree.setAttributes(attrMap);
		if (cateloryMap != null && cateloryMap.size() > 0) {
			rootTree.setChildren(transToTree(0, cateloryMap));
		} else {
			rootTree.setChildren(null);
		}
		JSONArray ja = JSONArray.fromObject(rootTree);

		return ja.toString();

	}

	/**
	 * 将 Map<String,List<CategoryVo>> map中，键为String
	 * parentKey的值List<CategoryVo>转为List<TreeNode>
	 * 
	 * @param parentKey
	 * @param map
	 * @return
	 */
	private List<TreeNode> transToTree(int parentKey,
			Map<Integer, List<CategoryVo>> map) {
		List<CategoryVo> tempList = map.get(parentKey);// 键值对应的list不可能为空
		List<TreeNode> treeList = new ArrayList<TreeNode>();
		for (CategoryVo categoryVo : tempList) {
			TreeNode childNode = new TreeNode();
			childNode.setId(String.valueOf(categoryVo.getCategoryID()));
			childNode.setText(categoryVo.getCategoryCnName());
			if (map.containsKey(categoryVo.getCategoryID())) {
				childNode.setChildren(transToTree(categoryVo.getCategoryID(),
						map));
			}
			Map<String, Object> attri = new HashMap<String, Object>();
			attri.put("comment", categoryVo.getComment());
			attri.put("declareName", categoryVo.getDeclareName());
			childNode.setAttributes(attri);
			treeList.add(childNode);
		}
		return treeList;
	}

	/**
	 * 增加产品分类      productCategory.jsp页面
	 * 
	 * @param request
	 * @param session
	 * @param category
	 * @param attrID
	 * @return
	 */
	@RequestMapping(value = "/addCategory", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json addCategoryViaAddCategoryPg(HttpServletRequest request,
			HttpSession session, CategoryVo category, Integer[] attrID) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			category.setCreateDt(Timestamp.valueOf(DateUtil.formatDateTime(now)));
			category.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			int categoryID = categoryService.createNewCategory(currentUser,
					category);
			if (attrID != null) {// 如果选择了属性
				List<Integer> newList = new ArrayList<Integer>(attrID.length);
				newList.addAll(Arrays.asList(attrID));
				AttrGroupVo attrGroupVo = new AttrGroupVo();
				attrGroupVo.setComment(category.getComment());
				int attrGroupID = productAttrService.createNewAttrGroup(
						currentUser, attrGroupVo);// 单一的创建组
				productAttrService.assignAttr4AttrGroup(currentUser, attrGroupID,
						null, newList);// Arrays.asList(attrID)
				categoryService.assignAttrGroup4Category(currentUser,
						categoryID, -1, attrGroupID);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 修改产品分类 包括分类属性        productCategory.jsp页面
	 * 
	 * @param request
	 * @param session
	 * @param category
	 * @param attrID
	 * @return
	 */
	@RequestMapping(value = "/updateCategory", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json updateCategoryViaAddCategoryPg(HttpServletRequest request,
			HttpSession session, CategoryVo category, Integer[] attrID) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			Date now = new Date();
			category.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			category.setOperatorID(currentUser.getUserID());
			category.setOperatorName(currentUser.getUserCnName());
			/*
			 * if (category.getCategoryParentID() == 0) {// 如果父节点为0，那么只是增加
			 * productService.updateCategoryByID(currentUser, category); } else
			 * {
			 */
			categoryService.updateCategoryByID(currentUser, category);
			List<Integer> beforeAttrIdList = categoryService
					.getAttrsID4Category(category.getCategoryID());
			Set<AttrGroup> agSet = categoryService
					.queryAttrGroupByCategoryID(category.getCategoryID());
			List<Integer> newList = null;
			if (attrID != null) {

				newList = new ArrayList<Integer>(attrID.length);
				newList.addAll(Arrays.asList(attrID));
			}
			if (agSet != null && agSet.size() > 0) {// 如果第一次分配属性组
				for (AttrGroup attrGroup : agSet) {
					productAttrService.assignAttr4AttrGroup(currentUser,
							attrGroup.getAttrGroupID(), beforeAttrIdList,
							newList);// Arrays.asList(attrID)
					categoryService.assignAttrGroup4Category(currentUser,
							category.getCategoryID(), -1,
							attrGroup.getAttrGroupID());
				}
			} else {// 如果没有分配属性组
				AttrGroupVo attrGroupVo = new AttrGroupVo();
				attrGroupVo.setComment(category.getComment());
				int attrGroupID = productAttrService.createNewAttrGroup(
						currentUser, attrGroupVo);// 单一的创建组
				productAttrService.assignAttr4AttrGroup(currentUser, attrGroupID,
						null, newList);// Arrays.asList(attrID)
				categoryService.assignAttrGroup4Category(currentUser,
						category.getCategoryID(), -1, attrGroupID);
			}
			// }

			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 通过分类ID获取分类信息 包括分类关联的属性信息       productCategory.jsp
	 * 
	 * @param request
	 * @param session
	 * @param categoryID
	 * @return
	 */
	@RequestMapping(value = "/getCategoryInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getCategoryInfoViaAddCategoryPg(HttpServletRequest request,
			HttpSession session, int categoryID) {
		Json json = new Json();
		try {
			CategoryVo categoryVo = categoryService.getCategoryById(categoryID);
			categoryVo.setAttributesIds(categoryService
					.getAttrsID4Category(categoryID));
			json.setObj(categoryVo);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 删除产品分类             productCategory.jsp页面
	 * 
	 * @param request
	 * @param session
	 * @param categoryID
	 * @return
	 */
	@RequestMapping(value = "/deleteCategory", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json deleteCategoryViaAddCategoryPg(HttpServletRequest request,
			HttpSession session, int categoryID) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		try {
			categoryService.deleteCategoryByID(currentUser, categoryID);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}


	/**
	 * 更新申报名称     productCategory.jsp
	 * @param request
	 * @param session
	 * @param categoryID
	 * @return
	 */
	@RequestMapping(value = "/updDeclareName", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json updDeclareName(HttpServletRequest request,
			HttpSession session, String categoryCode,String declareName) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		
		try {
			int categoryId = Integer.parseInt(categoryCode);
			categoryService.updDeclareName(currentUser, categoryId,declareName);
			json.setSuccess(true);
		} catch (ProductException e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
		}catch (NumberFormatException e) {
			json.setSuccess(false);
			json.setMsg("请填写正确的分类编码！");
		}
		return json;
	}
	
	/**
	 * 通过导入Excel的第一行判断更新分类的哪一个字段     productCategory.jsp
	 * @param request
	 * @param file
	 * @param currentUser
	 * @return
	 */
	@RequestMapping(value = "/batchUpdDeclareName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json batchUpdDeclareName(HttpServletRequest request,
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
					categoryService.updateDynCategoryAttr(rStockReferList,currentUser);
					if (logger.isDebugEnabled()) {
						logger.debug("Excel导入数据有:" + rStockReferList.size() + "条");
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Excel导入数据有:" + rStockReferList.size() + "条，已经成功修改！");
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