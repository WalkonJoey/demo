package com.hqdna.common.mv.report;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.mime.MimeUtility;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.hqdna.common.baseException.BaseException;
import com.hqdna.common.mv.excel.AmazonReviewExcel;
import com.hqdna.common.mv.excel.IExcelHandler;
import com.hqdna.common.mv.excel.ProductExcel;
import com.hqdna.common.mv.excel.TemplateExportExcel;

/**
 * Excel视图渲染类,支持xls和xlsx格式
 * 
 *
 */
public final class ExcelMultipleView extends AbstractView {

	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.ms-excel";

	/** The extension to look for existing templates old format */
	private static final String EXTENSION = ".xls";

	/** The extension to look for existing templates new format */
	private static final String X_EXTENSION = ".xlsx";
	/** auto decide which poi api to be used */
	private static final String EXCEL_TYPE = "format";
	/** excel type:eg:purchaseOrder,product,saleOrder and so on... */
	private String excelType = null;

	private Map<String, Class<? extends IExcelHandler>> formatMappings;

	private String url ;

	/**
	 * Default Constructor. Sets the content type of the view to
	 * "application/vnd.ms-excel".
	 */
	public ExcelMultipleView() {
		setContentType(CONTENT_TYPE);
		formatMappings = new HashMap<String, Class<? extends IExcelHandler>>();
		// 这里添加新的匹配excel
		formatMappings.put("templateExportExcel", TemplateExportExcel.class);//好像没用 在导出库存时候用到
		formatMappings.put("productExcel", ProductExcel.class);
		formatMappings.put("amazonReviewExcel", AmazonReviewExcel.class);//用于导出amazon用户评论
		
	}

	/**
	 * Set the URL of the Excel workbook source, without localization part nor
	 * extension.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	protected String getExcelType() {
		return excelType;
	}

	protected void setExcelType(String excelType) {
		this.excelType = excelType;
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		check(model);//检查model中是否设置了format字段，将format赋值给excelType

		Class<? extends IExcelHandler> viewClass = this.formatMappings.get(excelType);
		//instantiateClass(Class clazz)
        //Convenience method to instantiate a class using its no-arg constructor.  通过无参构造函数方便的方法实例化一个类
		IExcelHandler excelHandler = BeanUtils.instantiateClass(viewClass);//viewClass形如PurchaseOrderExcel.class的类
		this.setUrl(url);
		this.buildExcelDocument(model, request, response, excelHandler);
	}

	/**
	 * 
	 */
	protected final void buildExcelDocument(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response,
			IExcelHandler excelHandler) throws Exception {
		Workbook workbook;
		if (this.url != null) {
			workbook = getTemplateSource(this.url, request);
		} else {
			//这里初始化导出的excel后缀名为 xlsx
			workbook = new XSSFWorkbook();
			if (logger.isDebugEnabled()) {
			logger.debug("Created Excel Workbook from scratch");
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("创建excel文档中...");
		}

		// 实际调用
		excelHandler.writeExcel(model, workbook);
		// Set the content type.
		response.setContentType(getContentType());
		String fileName = ((ExcelParam) model.get(ExcelParam.PARAM))
				.getFileName();
		if (logger.isDebugEnabled()) {
			logger.debug("即将导出的Excel：" + fileName);
		}

		response.setCharacterEncoding("UTF-8");
		//设置导出文件的文件名
		setFileName(request, response, fileName);
		// Should we set the content length here?
		// response.setContentLength(workbook.getBytes().length);

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
	}

	/**
	 * 根据浏览器类型生成相应编码的文件名
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private void setFileName(HttpServletRequest request,
			HttpServletResponse response, String fileName)
			throws UnsupportedEncodingException {
		String userAgent = request.getHeader("user-agent");
		if (logger.isDebugEnabled()) {
			logger.debug("userAgent：" + userAgent);
		}
		String accurateFileName = null;
		if (userAgent.toLowerCase().contains("firefox")) {
			// firefox浏览器
			accurateFileName = MimeUtility.decodeText(fileName);
			if (logger.isDebugEnabled()) {
				logger.debug("火狐浏览器下的excel名称：" + accurateFileName);
			}
		} else if (userAgent.toLowerCase().contains("chrome")) {
			// chrome浏览器
			accurateFileName = URLEncoder.encode(fileName, "UTF-8");
			if (logger.isDebugEnabled()) {
				logger.debug("chrome浏览器下的excel名称：" + accurateFileName);
			}
		} else if (userAgent.toLowerCase().contains("trident")) {
			// IE浏览器
			accurateFileName = URLEncoder.encode(fileName, "UTF-8");
			if (logger.isDebugEnabled()) {
				logger.debug("IE浏览器下的excel名称：" + accurateFileName);
			}
		} else {
			// 其他浏览器
			accurateFileName = fileName;
			if (logger.isDebugEnabled()) {
				logger.debug("其他浏览器下的excel名称：" + accurateFileName);
			}
		}
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ accurateFileName + ".xlsx\"");
	}

	/**
	 * 获取模板文件
	 * 
	 * @param url
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected Workbook getTemplateSource(String url, HttpServletRequest request)
			throws Exception {
		LocalizedResourceHelper helper = new LocalizedResourceHelper(
				getApplicationContext());
		Locale userLocale = RequestContextUtils.getLocale(request);

		// new format first 优先新格式
		Resource inputFile = helper.findLocalizedResource(url, X_EXTENSION,
				userLocale);
		if (inputFile == null) {
			inputFile = helper
					.findLocalizedResource(url, EXTENSION, userLocale);
		}
		// Create the Excel document from the source.
		if (logger.isDebugEnabled()) {
			logger.debug("Loading Excel workbook from " + inputFile);
		}
		Workbook wb = WorkbookFactory.create(inputFile.getInputStream());
		return wb;
	}

	/**
	 * 校验输入的数据是否正确
	 * 
	 * @param model
	 */
	private void check(Map<String, Object> model) {
		String formatName = (String) model.get(EXCEL_TYPE);
		if (formatName == null) {
			new BaseException("没有定义excel模板类型");
		}
		if (formatMappings.containsKey(formatName)) {
			setExcelType(formatName);
		}
		new BaseException("定义的excel模板类型不存在");
	}

}
