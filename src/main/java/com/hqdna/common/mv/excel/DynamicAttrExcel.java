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

import com.hqdna.common.commonTools.ExcelUtil;

public class DynamicAttrExcel{
	
	private String sheetName;// excel sheet名
	private String fileName;// excel文件名
	private String titles[];// excel表头名 （sheet内容的第一行）

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

	public DynamicAttrExcel(){}
	public DynamicAttrExcel(String sheetName,String fileName,String[] titles){
		this.sheetName = sheetName;
		this.fileName = fileName;
		this.titles = titles;
	}

	protected Cell getCell(Sheet sheet, int row, int col) {
		Row sheetRow = sheet.getRow(row);
		if (sheetRow == null) {
			sheetRow = sheet.createRow(row);
		}
		Cell cell = sheetRow.getCell(col);
		if (cell == null) {
			cell = sheetRow.createCell(col);
		}
		return cell;
	}
	protected Cell getCell(Row row, int col) {
		Cell cell = row.getCell(col);
		if (cell == null) {
			cell = row.createCell(col);
		}
		return cell;
	}
	/**
	 * 
	 * @param inputStream
	 * @param clazz   加上这个参数是为了做数据验证
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <T>List<Map<String,Object>> readExcelByInputStream(InputStream inputStream,Class<T> clazz){
			//T obj = clazz.newInstance();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			try {
				Workbook rwb = WorkbookFactory.create(inputStream);//Workbook 对于xls格式的和xlsx的都能读取
				Sheet st = rwb.getSheetAt(0);//excel第一页放产品，第二页放对应的格式代码
				if (st == null) {
					return null;
				}
				list = parseSheetInfo(st,clazz);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			return list;
	}
	private <T>List<Map<String, Object>> parseSheetInfo(Sheet sheet,Class<T> clazz) {
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
						String classType = (clazz).getDeclaredField(params[y]).getType().toString();
						itemMap.put(params[y], ExcelUtil.judgeCellType(classType, cell));
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
						throw new RuntimeException("第"+(y+1)+"行，第"+(x+1)+"列数据格式不对"+e.getMessage());
					} catch (SecurityException e) {
						e.printStackTrace();
						throw new RuntimeException("第"+(y+1)+"行，第"+(x+1)+"列数据格式不对"+e.getMessage());
					}
			}
			list.add(itemMap);
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

}
