package com.hqdna.product.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.net.NioChannel;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.commonTools.NetPicUtil;
import com.hqdna.common.mv.excel.DynamicProductExcel;
import com.hqdna.common.mv.excel.ProductExcel;
import com.hqdna.common.mv.report.ExcelParam;
import com.hqdna.common.page.Json;
import com.hqdna.common.page.PageInfo;
import com.hqdna.common.page.QueryResult;
import com.hqdna.permission.vo.Module;
import com.hqdna.permission.vo.Perm;
import com.hqdna.permission.vo.PermType;
import com.hqdna.product.vo.AttrNameVo;
import com.hqdna.product.vo.ProductVo;
import com.hqdna.user.util.IUserConstants;
import com.hqdna.user.vo.UserInfo;

@Controller
@RequestMapping("/productManage")
@SessionAttributes(IUserConstants.CURRENT_USER)
public class ProductController extends BaseController {

	/**
	 * 返回product页面
	 * 
	 * @author Joey
	 * @return
	 */
	@RequestMapping(params = "productManagePage")
	public String forward() {
		return "/productMgr/productManage";
	}
	@RequestMapping(params = "amazonReviewPage")
	public String forwardReview() {
		return "/productMgr/amazonReview";
	}

	/************************************************
	 * 非目录跳转action,toolbar跳转action
	 ************************************************/

	/**
	 * 导航到updateProduct.jsp页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateProductPg")
	public String updateProductPgViaListPg(HttpServletRequest request,
			int productID) {
		if (!"".equals(productID) && productID >= 0) {
			request.setAttribute("productInfo",
					productService.getProductByID(productID));
			return "/productMgr/updateProduct";
		}else{
			return "/productMgr/updateProduct";
		}
		
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

	@RequestMapping(value = "productDetailPg")
	public String productDetailPgViaAddAttrPg(HttpServletRequest request,
			int productID, String skuNo) {
		ProductVo productVo = null;

		if (skuNo != null && !"".equals(skuNo)) {
			request.setAttribute("skuNo", skuNo);
			productVo = productService.getProductBySkuNo(skuNo);
			request.setAttribute("productInfo", productVo);
			request.setAttribute("productID", productVo.getProductID());
		} else if (productID > 0) {
			request.setAttribute("productID", productID);
			productVo = productService.getProductByID(productID);
			request.setAttribute("productInfo", productVo);
			request.setAttribute("skuNo", productVo.getSkuNo());
		} else {
			logger.debug("请传递产品的ID或者产品的sku码");
		}
		return "/productMgr/productDetail";
	}

	@RequestMapping(value = "updateProductPicPg")
	public String updateProductPicPgViaListPg(HttpServletRequest request,
			int productID) {
		request.setAttribute("productID", productID);
		return "/productMgr/updateProductPic";
	}
	@RequestMapping(value = "importAttachmentPg")
	public String importAttachmentPg(HttpServletRequest request,
			int productID) {
		request.setAttribute("productID", productID);
		return "/productMgr/importAttachment";
	}

	/***************************************************************************************
	 * 产品管理界面
	 */

	/**
	 * 通过条件，返回产品列表，供前台datagrid显示
	 * 
	 * @param request
	 * @param response
	 * @param pgInfo
	 * @param session
	 * @param productVo
	 * @return
	 */
	@RequestMapping(value = "/findProductsByCondition", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Perm(module = Module.product, type = PermType.View)
	public Object findProductsViaListPg(HttpServletRequest request,
			HttpServletResponse response, PageInfo pgInfo, HttpSession session,
			ProductVo productVo, String createDtB, String createDtE,
			String isSearch,String batchSkuNo) {
		// 过滤SQL
		Map<String, Object> whereSqlMap = getWhereSqlMap(request, pgInfo);
		// 添加查询条件
		if (productVo != null && isSearch != null) {
			if (productVo.getSkuNo() != null
					&& !productVo.getSkuNo().trim().equals("")) {
				whereSqlMap.put("  o.skuNo like '%%"
						+ productVo.getSkuNo().trim() + "%%' ", null);
			}
			if (productVo.getProductEnName() != null
					&& !productVo.getProductEnName().trim().equals("")) {
				whereSqlMap.put("  lower(o.productEnName) like '%%"
						+ productVo.getProductEnName().toLowerCase().trim()
						+ "%%' ", null);
			}
			if (productVo.getBillStatus() > -1) {
				whereSqlMap.put("  o.billStatus =?", productVo.getBillStatus());
			}
			if (productVo.getVendorID() > 0) {
				whereSqlMap.put("  o.vendor.vendorID =?",
						productVo.getVendorID());
			}
			if (createDtB != null && !createDtB.trim().equals("")) {
				createDtB += " 00:00:00";
				whereSqlMap.put("  o.createDt >= DATE_FORMAT('" + createDtB
						+ "','%Y-%m-%d')", null);
			}
			if (createDtE != null && !createDtE.trim().equals("")) {
				createDtE += " 00:00:00";
				whereSqlMap.put(
						"  o.createDt < DATE_FORMAT('"
								+ DateUtil.dayAddOrSub(createDtE, 1)
								+ "','%Y-%m-%d')", null);
			}
		}

		// 排序
		LinkedHashMap<String, String> order = getOrderMap(pgInfo);
		QueryResult<ProductVo> qr = null;
		if(batchSkuNo!=null&&!"".equals(batchSkuNo)){
			List<String> skuNoList = new ArrayList<String>();
			String[] skuNoArr = batchSkuNo.split(",");
			for(int i = 0;i<skuNoArr.length;i++){
				if(skuNoArr[i]!=null&&!"".equals(skuNoArr[i].trim())){
					skuNoList.add(skuNoArr[i].trim());
				}
			}
			qr = productService.getProductList(pgInfo, whereSqlMap, order,skuNoList);
		}else{
			qr = productService.getProductList(pgInfo,whereSqlMap, order);
		}
		
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", qr.getTotalrecord());
		attributes.put("rows", qr.getResultlist());
		return attributes;
	}

	/**
	 * 获取产品列表，用过弹出对话框选择
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @param pgInfo
	 * @param vendorID
	 * @return
	 */
	@RequestMapping(value = "/getProduct4Dialog")
	public ModelAndView getProduct4Dialog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, PageInfo pgInfo) {
		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);
		String vendorID = request.getParameter("vendorID");
		String skuNo = request.getParameter("skuNo");
		if (vendorID != null && vendorID.trim().length() > 0) {
			whereSqlMap.put(" vendor.vendorID =? ", Integer.valueOf(vendorID));
		}
		whereSqlMap.put(" isRegister =? ", (byte) 1);
		if (skuNo != null && skuNo.trim().length() > 0) {
			whereSqlMap.put(" skuNo like '%"+skuNo.trim()+"%'", null);
		}
		/*if(purchaseType!=null &&!purchaseType.trim().equals("")){
			whereSqlMap.put(" purchaseType =? ", Byte.parseByte(purchaseType));
		}*/
		QueryResult<ProductVo> products = productService.getProductList(pgInfo, whereSqlMap);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", products.getTotalrecord());
		attributes.put("rows", products.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}

	/**
	 * 获取所有产品列表，用于其他弹出对话框选择
	 *
	 * @param request
	 * @param response
	 * @param session
	 * @param pgInfo
	 * @return
	 */
	@RequestMapping(value = "/getProduct4_Dialog")
	public ModelAndView getProduct4_Dialog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, PageInfo pgInfo) {
		Map<String, Object> whereSqlMap = getWhereSqlMap(pgInfo);
		String name = request.getParameter("param_name");
		String value = request.getParameter("param_value");
		if (name != null && value != null) {
			String sqlName = "o." + name;
			whereSqlMap.put(
					" lower(" + sqlName + ") like '%" + value.toLowerCase()
							+ "%'", null);
		}
		whereSqlMap.put(" isRegister =? ", (byte) 1);

		QueryResult<ProductVo> products = productService.getProductList(pgInfo, whereSqlMap);

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", products.getTotalrecord());
		attributes.put("rows", products.getResultlist());
		ModelAndView mav = new ModelAndView();
		mav.addAllObjects(attributes);
		return mav;
	}

	/**
	 * 新增产品 只是将产品放到数据库，还没有正式提交备案
	 * 
	 * @param request
	 * @param session
	 * @param productVo
	 * @return
	 * 
	 */
	@RequestMapping(value = "/addProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json addProdcutsViaListPg(HttpServletRequest request,
			HttpSession session, ProductVo productVo) {
		Json json = new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		try {
			Date now = new Date();
			// 一下在页面获取不到的属性，在这里统一加上，关联对象，在这里设置对象的ID
			productVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			productVo.setCreateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			productVo.setCreatorID(currentUser.getUserID());
			productVo.setOperatorID(currentUser.getUserID());

			productService.addProduct(productVo, currentUser);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg(e.getMessage());
			json.setSuccess(false);
		}
		return json;
	}

	
	/**
	 * 批量导入产品
	 * 
	 * @param request
	 * @param file
	 * @param currentUser
	 * @return
	 */
	@RequestMapping(value = "/importProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json importProduct(HttpServletRequest request,
			@RequestParam MultipartFile file,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser) {
		Json json = new Json();
		int excelNum = 0;
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
			ProductExcel productExel = new ProductExcel();

			// 判断excel文件后缀名xls和xlsx
			if ("xls".equals(filename) || "xlsx".equals(filename)) {// 导入2003版excel或导入2007或以上版excel
				List<Object[]> rProductList = productExel.readExcel(input);
				if (rProductList.size() > 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("Excel导入数据有:" + rProductList.size() + "条");
					}
					List<ProductVo> prodlist = productExel.parse(rProductList);

					for (ProductVo productVo : prodlist) {
						ProductVo existProdcuct = productService
								.getProductBySkuNo(productVo.getSkuNo());
						if (existProdcuct != null) {
							if (logger.isDebugEnabled()) {
								logger.debug("sku为：" + productVo.getSkuNo()
										+ "的产品数据库中已经存在！");
							}
							json.setSuccess(false);
							json.setMsg("sku为：" + productVo.getSkuNo()
									+ "的产品数据库中已经存在！");
							return json;
						}

						Date now = new Date();
						productVo.setCreatorID(currentUser.getUserID());
						productVo.setOperatorID(currentUser.getUserID());
						productVo.setCreateDt(Timestamp.valueOf(DateUtil
								.formatDateTime(now)));
						productVo.setOperateDt(Timestamp.valueOf(DateUtil
								.formatDateTime(now)));
					}
					// 先验证完在添加到数据库
					for (ProductVo proVo : prodlist) {
						int productID = productService.addProduct(proVo,
								currentUser);
						if (productID < 0) {
							logger.debug("新增产品:" + proVo.getProductCnName()
									+ "失败");
						} else {
							excelNum++;
						}
					}
					excelStr = "成功导入数据：" + excelNum + "条";
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

		} catch (Exception e) {
			e.printStackTrace();
			excelStr = e.getMessage();
			json.setSuccess(false);
			json.setMsg(excelStr);
		}
		return json;
	}

	/**
	 * 下载产品模板
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 */
	@RequestMapping(value = "/downloadTemplate")
	@ResponseBody
	public void downExcel(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String fileName,String exportName)
			throws Exception {

		//String exportName = DateUtil.formatOnlyDate(new Date()) + "销售订单模板.xlsx";
		String contentType = "application/octet-stream";
		exportName = URLDecoder.decode(exportName,"utf8");
		try {
			downloadExcelTemplate(request, response, fileName, contentType, exportName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/downloadAttachment")
	@ResponseBody
	public void downloadAttachment(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String fileUrl,String fileName)
			throws Exception {

		//String exportName = DateUtil.formatOnlyDate(new Date()) + "销售订单模板.xlsx";
		String contentType = "application/octet-stream";
		fileName = URLDecoder.decode(fileName,"utf8");
		fileUrl = URLDecoder.decode(fileUrl,"utf8");
		try {
			response.setContentType("text/html;charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;

			String downLoadPath = request.getSession().getServletContext()
					.getRealPath(fileUrl);

			long fileLength = new File(downLoadPath).length();

			response.setContentType(contentType);
			response.setHeader("Content-disposition", "attachment; filename="
					+ new String(fileName.getBytes("utf-8"), "ISO8859-1"));
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更新产品基本信息和属性信息 不包括更新图片
	 * 
	 * @param request
	 * @param session
	 * @param productVo
	 * @param attrListData
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json updateProdcutViaListPg(HttpServletRequest request,
			HttpSession session, ProductVo productVo, String attrListData)
			throws UnsupportedEncodingException {
		Json json = new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);

		try {
			ProductVo bfProductVo = productService.getProductByID(productVo
					.getProductID());
			Date now = new Date();
			// 一下在页面获取不到的属性，在这里统一加上，关联对象，在这里设置对象的ID
			productVo.setOperateDt(Timestamp.valueOf(DateUtil
					.formatDateTime(now)));
			productVo.setOperatorID(currentUser.getUserID());
			// 设置一些没有传递前台不能修改的项目
			productVo.setCreateDt(bfProductVo.getCreateDt());
			productVo.setCreatorID(bfProductVo.getCreatorID());
			productVo.setPicturePath(bfProductVo.getPicturePath());
			// 属性也没有改变，不设置的话更新的时候会出问题
			productService.updateProduct(productVo, currentUser);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 删除产品，包括图片
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param skuNos
	 *            sku码，以逗号分隔
	 * @return
	 */
	@RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
	@ResponseBody
	public Json deleteProductViaListPg(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String skuNos) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		Json json = new Json();
		if (skuNos == null) {
			json.setSuccess(false);
			return json;
		}
		String[] skuArr = skuNos.split(",");
		try {
			for (String skuNo : skuArr) {
				// 通过产品的SKU获取产品的存放图片路径，删除所有的图片
				ProductVo productVo = productService.getProductBySkuNo(skuNo);
				if (productVo != null) {
					String picPaths = productVo.getPicturePath();
					if (picPaths != null) {
						deletePicByDBPath(request, picPaths.split(","),
								productVo.getSkuNo());// 首先删除上传的图片
						String realPath = request.getSession()
								.getServletContext()
								.getRealPath("web/images/upload/" + skuNo);
						deleteFileOrDir(realPath);// 删除服务器上对应文件夹
					}
				}
				/*
				 * // 删除动态属性表中的该产品动态属性
				 * productService.deleteDynamicAttrByProdId(productVo
				 * .getProductID());
				 */
				// 删除产品基本信息
				productService.deleteProductBySkuNo(currentUser, skuNo);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}




	private boolean deletePicByDBPath(HttpServletRequest request,
			String[] dbPathArr, String skuNo) {
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
		try {
			// 服务器的真正路径
			String realPath = request.getSession().getServletContext()
					.getRealPath("web/images/upload/" + skuNo);
			// 本地的模拟的服务器地址
			/*
			 * String projectPath =
			 * "E:\\svnReporistory\\erp\\trunk\\v2.0\\wgerp\\WebContent\\web\\images\\upload\\"
			 * + skuNo;
			 */
			for (String needDPic : dbPathArr) {
				if (needDPic != null && !needDPic.isEmpty()) {
					// 存放在数据库的图片路径不全，用存放的全路径替换
					String realName = needDPic.replaceAll("web/images/upload/"
							+ skuNo, "");
					/*
					 * deleteFileOrDir(projectPath + realName);// 删除项目目录下的文件
					 */deleteFileOrDir(realPath + realName);// 删除服务器上的文件
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 这个是更新产品图片提交时调用的
	 * 
	 * @param request
	 * @param session
	 * @param mainPic
	 * @param productPics
	 * @param productID
	 * @param oldPics
	 * @return
	 */
	@RequestMapping(value = "/updateProductPic", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String uploadProdPicViaUpdatePicPg(HttpServletRequest request,
			HttpSession session, @RequestParam MultipartFile mainPic,
			@RequestParam MultipartFile[] productPics, int productID,
			String oldPics) {
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		StringBuilder allPicPath = new StringBuilder();
		ProductVo productVo = productService.getProductByID(productID);
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
		String realPath = request.getSession().getServletContext()
				.getRealPath("web/images/upload/" + productVo.getSkuNo());

		/*
		 * String projectPath =
		 * "E:\\svnReporistory\\erp\\trunk\\v2.0\\wgerp\\WebContent\\web\\images\\upload\\"
		 * + productVo.getSkuNo();
		 */
		// 判断以前的的图片是否删除
		String oldPics02 = productVo.getPicturePath();
		if (oldPics02 != null && !oldPics02.isEmpty()) {
			String[] oldPics02Arr = oldPics02.split(",");// 删除前的所有图片路径
			String[] needDeletePics = new String[oldPics02Arr.length];// 保存要删除的图片
			String[] oldPicsArr = oldPics.split(",");// 删除部分图片后的所有图片路径
			int count = 0;
			for (String pic02 : oldPics02Arr) {
				boolean bool = false;
				for (String pic : oldPicsArr) {
					if (pic.equals(pic02)) {
						bool = true;
					}
				}
				if (!bool) {
					needDeletePics[count++] = pic02;
				}
			}
			deletePicByDBPath(request, needDeletePics, productVo.getSkuNo());
		}
		if (mainPic == null || mainPic.isEmpty()) {
			System.out.println("文件未上传");
			allPicPath.append(oldPics);// 没有修改主图片的情况
		} else {
			String pic_type = mainPic.getContentType();
			String mainPicName = "main";
			mainPicName = mainPicName.concat(getImageFileSuffix(pic_type));// 给文件名增加后缀
			try {
				FileUtils.copyInputStreamToFile(mainPic.getInputStream(),
						new File(realPath, mainPicName));
				allPicPath.append("web/images/upload/" + productVo.getSkuNo()
						+ "/" + mainPicName);// 先将主图片添加到字符串
				allPicPath.append(oldPics);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			long i = System.currentTimeMillis();
			for (MultipartFile myfile : productPics) {
				if (myfile.isEmpty()) {
					System.out.println("文件未上传");
				} else {
					String pic_type = myfile.getContentType();
					String picName = String.valueOf(i);
					i++;
					if (pic_type.equals("image/jpeg")) {
						picName = picName.concat(".jpg");
					} else if (pic_type.equals("image/png")) {
						picName = picName.concat(".png");
					} else if (pic_type.equals("image/bmp")) {
						picName = picName.concat(".bmp");
					} else if (pic_type.equals("image/gif")) {
						picName = picName.concat(".gif");
					} else {
						picName = picName.concat(".jpg");
					}

					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, picName));
					allPicPath.append(",web/images/upload/"
							+ productVo.getSkuNo() + "/" + picName);
				}
			}
			productVo.setPicturePath(allPicPath.toString());
			productService.updateProduct(productVo, currentUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "<script type='text/javascript'>parent.showPromptMessages('提示','产品修改成功！');parent.refreshTab('产品信息');setInterval(parent.deletePanel('修改产品图片'),500);</script>";
	}

	
	/**
	 * 这个是更新产品图片提交时调用的
	 * 
	 * @param request
	 * @param session
	 * @param mainPic
	 * @param productPics
	 * @param productID
	 * @param oldPics
	 * @return
	 */
	@RequestMapping(value = "/importAttachments", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json importAttachments(HttpServletRequest request,
			HttpSession session,@RequestParam MultipartFile[] productPics, int productID,
			String oldPics) {
		Json json =new Json();
		UserInfo currentUser = (UserInfo) session
				.getAttribute(IUserConstants.CURRENT_USER);
		StringBuilder allPicPath = new StringBuilder();
		ProductVo productVo = productService.getProductByID(productID);
		// 如果用的是Tomcat服务器，则文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\WEB-INF\\upload\\文件夹中
		String realPath = request.getSession().getServletContext()
				.getRealPath("web/images/upload/" + productVo.getSkuNo());

		/*
		 * String projectPath =
		 * "E:\\svnReporistory\\erp\\trunk\\v2.0\\wgerp\\WebContent\\web\\images\\upload\\"
		 * + productVo.getSkuNo();
		 */
		// 判断以前的的图片是否删除
		String oldPics02 = productVo.getAttachmentPath();
		if (oldPics02 != null && !oldPics02.isEmpty()) {
			String[] oldPics02Arr = oldPics02.split(",");// 删除前的所有图片路径
			String[] needDeletePics = new String[oldPics02Arr.length];// 保存要删除的图片
			String[] oldPicsArr = oldPics.split(",");// 删除部分图片后的所有图片路径
			int count = 0;
			for (String pic02 : oldPics02Arr) {
				boolean bool = false;
				for (String pic : oldPicsArr) {
					if (pic.equals(pic02)) {
						bool = true;
					}
				}
				if (!bool) {
					needDeletePics[count++] = pic02;
				}
			}
			deletePicByDBPath(request, needDeletePics, productVo.getSkuNo());
		}
		try {
			if(oldPics!=null&&!"".equals(oldPics.toString())){
				allPicPath.append(oldPics);
			}
			int i = 0;
			for (MultipartFile myfile : productPics) {
				if (myfile.isEmpty()) {
					System.out.println("文件未上传");
				} else {
					//String picName = myfile.getName();
					String picName = myfile.getOriginalFilename();

					FileUtils.copyInputStreamToFile(myfile.getInputStream(),
							new File(realPath, picName));
					if(i==0){
						if(allPicPath==null||"".equals(allPicPath.toString())){
							allPicPath.append("web/images/upload/"
									+ productVo.getSkuNo() + "/" + picName);
						}else{
							allPicPath.append(",web/images/upload/"
									+ productVo.getSkuNo() + "/" + picName);
						}
						
					}else{
						allPicPath.append(",web/images/upload/"
								+ productVo.getSkuNo() + "/" + picName);
					}
					i++;
				}
			}
			productVo.setAttachmentPath(allPicPath.toString());;
			productService.updateProduct(productVo, currentUser);
			json.setSuccess(true);
			json.setMsg("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("操作失败");
		}
		return json;
	}
	/**
	 * 根据文件路径删除文件
	 * 
	 * @param path
	 * @return
	 */
	private boolean deleteFileOrDir(String path) {
		File targetFile = new File(path);
		if (targetFile.isDirectory()) { // 如果是 文件夹
			try {
				FileUtils.deleteDirectory(targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (targetFile.isFile()) {// 如果是文件
			targetFile.delete();
		}
		return true;
	}

	/**
	 * 通过文件类型，返回自定义文件后缀
	 * 
	 * @param contentType
	 * @return
	 */
	private String getImageFileSuffix(String contentType) {
		String picSuffix = "";
		if (contentType.equals("image/jpeg")) {
			picSuffix = ".jpg";
		} else if (contentType.equals("image/png")) {
			picSuffix = ".png";
		} else if (contentType.equals("image/bmp")) {
			picSuffix = ".bmp";
		} else if (contentType.equals("image/gif")) {
			picSuffix = ".gif";
		} else {
			picSuffix = ".jpg";
		}
		return picSuffix;
	}

	/**
	 * 获取产品的所有信息包括属性信息，在产品详情页显示
	 * 
	 * @param request
	 * @param session
	 * @param productID
	 * @return
	 */
	@RequestMapping(value = "/getProductDetail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getProductDetailViaDetailPg(HttpServletRequest request,
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			int productID, String skuNo) {
		Json json = new Json();
		try {
			ProductVo productVo = null;
			if (productID > 0) {
				productVo = productService.getProductByID(productID);
			} else {
				productVo = productService.getProductBySkuNo(skuNo);
			}
			// 动态属性列表
			Map<String, String> picPath = new HashMap<String, String>();
			picPath.put("picPath", productVo.getPicturePath());
			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("picture", picPath);
			json.setObj(obj);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}

	/**
	 * 返回产品图片，供显示图片大图
	 * 
	 * @param request
	 * @param session
	 * @param productID
	 * @return
	 */
	@RequestMapping(value = "/getProductPics", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getProductPicsViaDetailPg(HttpServletRequest request,
			HttpSession session, int productID) {
		Json json = new Json();
		try {
			ProductVo productVo = productService.getProductByID(productID);

			Map<String, String> picPath = new HashMap<String, String>();
			picPath.put("picPath", productVo.getPicturePath());

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("picture", picPath);
			json.setObj(obj);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	/**
	 * 返回产品图片，供显示图片大图
	 * 
	 * @param request
	 * @param session
	 * @param productID
	 * @return
	 */
	@RequestMapping(value = "/getProductAttachments", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getProductAttachments(HttpServletRequest request,
			HttpSession session, int productID) {
		Json json = new Json();
		try {
			ProductVo productVo = productService.getProductByID(productID);

			Map<String, Object> obj = new HashMap<String, Object>();
			obj.put("picPath", productVo.getAttachmentPath());
			obj.put("skuNo", productVo.getSkuNo());
			json.setObj(obj);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}
	/**
	 * 获取供应商相关的在售产品列表,供TAB选项卡用 added by Aaron on 20141008
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getProductListTab/{isRegister}")
	public ModelAndView getProductListTab(HttpServletRequest request,
			PageInfo pageInfo, HttpServletResponse response,
			HttpSession session, @PathVariable byte isRegister, int vendorID)
			throws IOException {
		// 过滤SQL
		Map<String, Object> whereSqlMap = new LinkedHashMap<String, Object>();
		if (vendorID != 0) {
			whereSqlMap.put(" vendorID=? ", vendorID);
		}
		if (isRegister == 0 || isRegister == 1) {
			whereSqlMap.put(" isRegister=? ", isRegister);
		}
		QueryResult<ProductVo> vos = productService.getProductList(null,whereSqlMap);

		ModelAndView mav = new ModelAndView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("total", vos.getTotalrecord());
		attributes.put("rows", vos.getResultlist());
		mav.addAllObjects(attributes);
		return mav;
	}

	@RequestMapping(value = "/getDynamicAttrByProdID", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getDynamicAttrByProdIDViaUpdatePg(HttpServletRequest request,
			HttpSession session, int productID) {
		Json json = new Json();
		try {
			ProductVo productVo = productService.getProductByID(productID);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
		}
		return json;
	}



	/**
	 * 通过导入Excel的第一行判断更新产品的哪一个字段
	 * @param request
	 * @param file
	 * @param currentUser
	 * @return
	 */
	@RequestMapping(value = "/importDynProductAttr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@Perm(module = Module.product, type = PermType.Edit)
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
	
	@RequestMapping(value = "/exportExcel")
	public ModelAndView exportExcel(
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			String prodIDs,HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("采购订单准备导出");
		}
		String[] poIDArr = prodIDs.split("-");
		List<Integer> poIDList = new ArrayList<Integer>();
		for (int i=0;i<poIDArr.length;i++) {
			try {
				poIDList.add(Integer.parseInt(poIDArr[i]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<ProductVo> prodList = productService.getProdByIDList(poIDList);
		String titles[] = { "序号", "SKU","采购价格","图片","供方型号","供应商编码"};
		model.addAttribute("entityVoList", prodList);
		model.addAttribute("titles", titles);
		model.addAttribute("sheetName", "prodPic");
		model.addAttribute("format", "productExcel");

		ExcelParam param = new ExcelParam();
		param.setFileName("Product"+DateUtil.formatSDateTime(new Date()));
		model.addAttribute(ExcelParam.PARAM, param);
		ModelAndView mv = new ModelAndView("purchaseOrderXls", model);
		return mv;
	}
	
	@RequestMapping(value = "/getAmazonReview", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Json getAmazonReview(HttpServletRequest request,
			HttpSession session, String amazonReviewUrl) {
		Json json = new Json();
		List<Map<String,String>> reviewStarMapList = new ArrayList<Map<String,String>>();
		try {
			reviewStarMapList = NetPicUtil.getAmazonReviewList(amazonReviewUrl);
			json.setMsg("获取成功");
			json.setObj(reviewStarMapList);
			json.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
		}
		return json;
	}
	
	@RequestMapping(value = "/getAndExportReview")
	public ModelAndView getAndExportReview(
			@ModelAttribute(IUserConstants.CURRENT_USER) UserInfo currentUser,
			String amazonReviewUrl,String reviewPageNum,HttpServletRequest request, HttpServletResponse response, ModelMap model){
		if (logger.isDebugEnabled()) {
			logger.debug("采购订单准备导出");
		}
		Json json = new Json();
		if(amazonReviewUrl==null||"".equals(amazonReviewUrl)){
			json.setSuccess(false);
			json.setMsg("请填写正确的url");
		}
		List<Map<String,String>> reviewStarMapList = null;
		Map<String,String> producInfo = null;
		if(reviewPageNum==null||"".equals(reviewPageNum)){
			reviewStarMapList = new ArrayList<Map<String,String>>();
			try {
				Document doc = NetPicUtil.getDocumentByUrl(amazonReviewUrl);
				reviewStarMapList = NetPicUtil.getAmazonReviewList(doc);
				producInfo = NetPicUtil.getAmazonProductInfo(doc);
				Map<String,Object> objMap = new HashMap<String, Object>();
				objMap.put("productReview", reviewStarMapList);
				objMap.put("productInfo", producInfo);
				json.setMsg("获取成功");
				json.setObj(objMap);
				json.setSuccess(true);
			} catch (NullPointerException e){
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			}catch (RuntimeException e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			}
		}else{
			
			reviewStarMapList = new ArrayList<Map<String,String>>();
			int reviewPageNumber = 0;
			try {
				Document doc = NetPicUtil.getDocumentByUrl(amazonReviewUrl);
				producInfo = NetPicUtil.getAmazonProductInfo(doc);
				reviewPageNumber = Integer.parseInt(reviewPageNum);
				if(amazonReviewUrl.indexOf("pageNumber")==-1){
					if(amazonReviewUrl.indexOf("/ref=")==-1){
						amazonReviewUrl = amazonReviewUrl+"&pageNumber=1";
					}else{
						amazonReviewUrl = amazonReviewUrl +"ref=cm_cr_arp_d_paging_btm_2?pageNumber=1";
					}
				}
				amazonReviewUrl = amazonReviewUrl.substring(0,amazonReviewUrl.indexOf("pageNumber=")+11);
				for (int i=1;i<=reviewPageNumber;i++) {
					amazonReviewUrl = amazonReviewUrl +i;
					reviewStarMapList.addAll(NetPicUtil.getAmazonReviewList(amazonReviewUrl));
				}
				Map<String,Object> objMap = new HashMap<String, Object>();
				objMap.put("productReview", reviewStarMapList);
				objMap.put("productInfo", producInfo);
				json.setMsg("获取成功");
				json.setObj(objMap);
				json.setSuccess(true);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg("请填写正确格式的评论数");
			}catch (NullPointerException e){
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			}catch (RuntimeException e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				json.setSuccess(false);
				json.setMsg(e.getMessage());
			}
		}
		
		String titles[] = { "序号", "星级","姓名","日期","评论"};
		model.addAttribute("json", json);
		model.addAttribute("titles", titles);
		model.addAttribute("sheetName", "amazon评论");
		model.addAttribute("format", "amazonReviewExcel");

		ExcelParam param = new ExcelParam();
		param.setFileName("评论"+DateUtil.formatSDateTime(new Date()));
		model.addAttribute(ExcelParam.PARAM, param);
		ModelAndView mv = new ModelAndView("purchaseOrderXls", model);
		return mv;
	}
}