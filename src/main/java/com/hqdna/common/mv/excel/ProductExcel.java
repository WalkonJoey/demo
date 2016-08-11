package com.hqdna.common.mv.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.hqdna.common.commonTools.DateUtil;
import com.hqdna.common.commonTools.ExcelUtil;
import com.hqdna.common.commonTools.NetPicUtil;
import com.hqdna.common.commonTools.ReflectUtil;
import com.hqdna.product.vo.ProductVo;

public class ProductExcel extends AbstractExcelHandler{
	
	private List<ProductVo> productList; // 数据
	private String sheetName;// excel sheet名
	private String fileName;// excel文件名
	private String titles[];// excel表头名 （sheet内容的第一行）

	public List<ProductVo> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductVo> productList) {
		this.productList = productList;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public ProductExcel(){}
	public ProductExcel(List<ProductVo> productList,String sheetName,String fileName,String[] titles){
		this.productList = productList;
		this.sheetName = sheetName;
		this.fileName = fileName;
		this.titles = titles;
	}

	/*产品图片	SKU	产品型号	供应商代码	产品一级目录 	产品二级目录	英文名称	中文名称	关键词  	品牌	质保期	     认证类型	产品参数	产品描述	
	 * 包装清单	批发价（元）	建议零售价（元）	净重（g）	发货重量（g）	包装长（cm）	包装宽（cm）	包装高（cm）	发货时效（小时）	
	 * 起定量	发货地	   	供货单位
*/
	
	String[] newParams = new String[]{"skuNo","vendorProdCode","productName","vendorCode","specParam","picturePath","sellPrice","beforePackWeight","packageLength",
			"packageWidth","packageHeight","createDt","categoryCodes"};//将产品分类放在categoryCodes里面
	String[] availParam = new String[]{
			"picturePath","skuNo","productModel","vendorCode","productCategory","twoLevelCategory","productEnName","productCnName","keyword","brand","shelfLife",
			"authType","specParam","description","packageList","purchasePrice","salePrice",
			"beforePackWeight","afterPackWeight","packageLength","packageWidth","packageHeight","deliveryTime",
			"minOrderQty","address","vendorName"
	};
	
	/**
	 * 
	 * @param clazz 要解析的类
	 * @param hssfSheet Excel表格的页
	 * @param params 属性名称
	 * @return
	 */
	private List<Object[]> parseSheetInfo(Class clazz, Sheet hssfSheet,String[] params){
		List<Object[]> list = new ArrayList<Object[]>();
		int lastRowIndex = hssfSheet.getLastRowNum();
		int k = 0;
		for (int x = 1; x <= lastRowIndex; x++, k++) {// 第一行为产品标题 x为行
			list.add(new Object[params.length]);
			for (int y = 0; y < params.length; y++) {// y为列
				Cell cell = getCell(hssfSheet, x, y);
				String classType;
				try {
					//获取该类对应属性的数据类型
					classType = clazz.getDeclaredField(params[y]).getType()
							.toString();
					//判断单元格的类型，返回 单元格类型对应的Java类型 的单元格中的值
					list.get(k)[y] = ExcelUtil.judgeCellType(classType, cell);
					/*if(y<25){//前25个为产品的基础属性
					}else{//其他为产品的动态属性
						list.get(k)[y] = ExcelUtil.judgeCellType("class java.lang.String", cell);
					}*/
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	private List<Object[]> parseSheetInfo02(Class clazz, Sheet hssfSheet,String[] params){
		List<Object[]> list = new ArrayList<Object[]>();
		int lastRowIndex = hssfSheet.getLastRowNum();
		int k = 0;
		for (int x = 1; x <= lastRowIndex; x++, k++) {// 第一行为产品标题 x为行
			list.add(new Object[params.length]);
			for (int y = 0; y < params.length; y++) {// y为列
				Cell cell = getCell(hssfSheet, x, y);
				String classType;
				try {
					//获取该类对应属性的数据类型
					classType = clazz.getDeclaredField(params[y]).getType().toString();
					//判断单元格的类型，返回 单元格类型对应的Java类型 的单元格中的值
					list.get(k)[y] = ExcelUtil.judgeCellType(classType, cell);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	public List<Object[]> readExcel(InputStream inputStream) {
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			Workbook rwb = WorkbookFactory.create(inputStream);//Workbook 对于xls格式的和xlsx的都能读取
			Sheet st = rwb.getSheetAt(0);//excel第一页放产品，第二页放对应的格式代码
			if (st == null) {
				return null;
			}
			list = parseSheetInfo(ProductVo.class, st, availParam);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Object[]> readProductExcel(InputStream inputStream) {
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			Workbook rwb = WorkbookFactory.create(inputStream);//Workbook 对于xls格式的和xlsx的都能读取
			Sheet st = rwb.getSheetAt(0);//excel第一页放产品，第二页放对应的格式代码
			if (st == null) {
				return null;
			}
			list = parseSheetInfo02(ProductVo.class, st, newParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<Map<String,Object>> readExcelByInputStream(InputStream inputStream){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			Workbook rwb = WorkbookFactory.create(inputStream);//Workbook 对于xls格式的和xlsx的都能读取
			Sheet st = rwb.getSheetAt(0);//excel第一页放产品，第二页放对应的格式代码
			if (st == null) {
				return null;
			}
			list = parseSheetInfo(st);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * sheet的第一列必须是关键字  比如sku
	 * @param sheet
	 * @return
	 */
	private List<Map<String, Object>> parseSheetInfo(Sheet sheet) {
		Row firstRow = sheet.getRow(0);
		short fRowCellNum = firstRow.getLastCellNum();
		String[] params = new String[fRowCellNum];
		for(int i = 0;i<fRowCellNum;i++){
			Cell cell = getCell(firstRow, i);
			params[i] = (String)ExcelUtil.judgeCellType("class java.lang.String", cell);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int lastRowIndex = sheet.getLastRowNum();
		for (int x = 1; x <= lastRowIndex; x++) {// 第一行为产品标题 x为行
			Map<String, Object> itemMap = new HashMap<String,Object>();
			for (int y = 0; y < params.length; y++) {// y为列
				Cell cell = getCell(sheet, x, y);
				try {
					itemMap.put(params[y], ExcelUtil.judgeCellType("class java.lang.String", cell));
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			list.add(itemMap);
		}
		return list;
	}

	public void writeExcel(Map<String, Object> model, Workbook workbook) {
		long start = DateUtil.nowMilli();
		try {
			@SuppressWarnings("unchecked")
			List<ProductVo> entityVoList = (List<ProductVo>)model.get("entityVoList"); 
			titles = (String[]) model.get("titles");
			sheetName = (String)model.get("sheetName");
			createExcel(model, workbook, sheetName.toString(),entityVoList);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		if(logger.isDebugEnabled()){
			logger.debug("导出采购单用时："+DateUtil.cal(start, DateUtil.nowMilli()));
		}
	}
	
	private void createExcel(Map<String, Object> model,Workbook workbook,String sheetName,List<ProductVo> entityVoList) throws Exception{
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);
		Row titleRow = sheet.createRow(0);
		for (int i=0;i<titles.length;i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titles[i]);
		}
		int noInitIndex = 1;
		for (int n = 0; n < entityVoList.size(); n++) {
			ProductVo itemVo = entityVoList.get(n);
			Row dataRow = sheet.createRow(noInitIndex);
			// 添加分录图片
			createPoItemPic(workbook, sheet, itemVo,noInitIndex);

			Cell cell01 = dataRow.createCell(0);
			cell01.setCellValue(n + 1);

			Cell cell02 = dataRow.createCell(1);
			cell02.setCellValue(itemVo.getSkuNo());
			Cell cell03 = dataRow.createCell(2);
			cell03.setCellValue(itemVo.getPurchasePrice().doubleValue());
			Cell cell05 = dataRow.createCell(4);
			cell05.setCellValue(itemVo.getAddress());
			Cell cell06 = dataRow.createCell(5);
			cell06.setCellValue(itemVo.getVendorCode());
			noInitIndex++;
		}
	}
	private void createPoItemPic(Workbook workbook,XSSFSheet sheet,ProductVo itemVo,int noTempStartIndex){
		if(itemVo.getPicturePath()!=null&&!"".equals(itemVo.getPicturePath())){
			try {
				byte[] picData = NetPicUtil.getLocalPic(itemVo.getVendorCode(),itemVo.getSkuNo(),itemVo.getPicturePath());
				if(picData!=null){
					addProdPic(workbook, picData, sheet, noTempStartIndex);
				}else{
					byte[] picData2 = NetPicUtil.getLocalPic(true);//获取本地默认的图片
					if(picData2!=null){
						addProdPic(workbook, picData2, sheet, noTempStartIndex);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				byte[] picData2 = NetPicUtil.getLocalPic(true);//获取本地默认的图片
				if(picData2!=null){
					addProdPic(workbook, picData2, sheet, noTempStartIndex);
				}
			}
			
		}else{
			byte[] picData2 = NetPicUtil.getLocalPic(true);//获取本地默认的图片
			if(picData2!=null){
				addProdPic(workbook, picData2, sheet, noTempStartIndex);
			}
		}
	}
	private void addProdPic(Workbook workbook,byte[] picData,XSSFSheet sheet,int noTempStartIndex){
		if(picData!=null){
			XSSFDrawing prodPicDrawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor prodPicAnchor = new XSSFClientAnchor();
			CTMarker ctmFrom  = prodPicAnchor.getFrom();
			ctmFrom.setCol(3);
			ctmFrom.setColOff(100);
			ctmFrom.setRow(noTempStartIndex);
			ctmFrom.setRowOff(100);
			CTMarker ctmTo  = prodPicAnchor.getTo();
			ctmTo.setCol(4);
			ctmTo.setColOff((long)200);
			ctmTo.setRow(noTempStartIndex+1);
			ctmTo.setRowOff((long)200);
			prodPicAnchor.setAnchorType(XSSFClientAnchor.MOVE_AND_RESIZE);
			prodPicDrawing.createPicture(prodPicAnchor, workbook.addPicture(picData,XSSFWorkbook.PICTURE_TYPE_JPEG));
			//XSSFPicture tempPic = prodPicDrawing.createPicture(prodPicAnchor, workbook.addPicture(picData,XSSFWorkbook.PICTURE_TYPE_JPEG));
			//tempPic.resize(0.48);
		}
	}
	public List<ProductVo> parseProduct(List<Object[]> objList) {
		List<ProductVo> productList = new ArrayList<ProductVo>();
		for (Object[] objects : objList) {
			ProductVo productVo = new ProductVo();
			for(int i = 0;i<newParams.length;i++){
				if(objects[i]!=null&&!"".equals(objects[i].toString())){
					try {
						ReflectUtil.setProperty(productVo, newParams[i],objects[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			productList.add(productVo);
		}
		return productList;
	}
	public List<ProductVo> parse(List<Object[]> objList) {
		List<ProductVo> productList = new ArrayList<ProductVo>();
		for (Object[] objects : objList) {
			ProductVo productVo = new ProductVo();
			for(int i = 0;i<availParam.length;i++){
				if(objects[i]!=null&&!"".equals(objects[i].toString())){
					try {
						ReflectUtil.setProperty(productVo, availParam[i],objects[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			productList.add(productVo);
		}
		return productList;
	}
}
