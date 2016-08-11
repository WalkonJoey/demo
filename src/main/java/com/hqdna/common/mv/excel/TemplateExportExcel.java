package com.hqdna.common.mv.excel;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hqdna.common.commonTools.ExcelUtil;

/**
 * 导出数据，这个是供导出库存使用的
 *
 */
public class TemplateExportExcel extends AbstractExcelHandler {
	

	private List<Object[]> listData; // 数据
	private String strName;// excel标题名
	private String fileName;// excel文件名
	private String titles[];// excel表头名



	public List<Object[]> getListData() {
		return listData;
	}

	public void setListData(List<Object[]> listData) {
		this.listData = listData;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
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

	public TemplateExportExcel() {
	}

	public TemplateExportExcel(List<Object[]> pList, String strName,
			String fileName, String titles[]) {
		this.listData = pList;
		this.strName = strName;
		this.fileName = fileName;
		this.titles = titles;
	}

	public List<Object[]> readExcel(InputStream inputP) {
		return null;
	}

	public void writeExcel(Map<String, Object> model, Workbook workbook) {
		try {
			reportExcel(model, workbook);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void reportExcel(Map<String, Object> model, Workbook workbook)
			throws RuntimeException {
		listData = (List<Object[]>) model.get("listData");
		titles = (String[]) model.get("titles");
		CreationHelper cHelp = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet(model.get("strName").toString());
		Row titleRow = sheet.createRow(0);
		int index = 0;
		// 写入表头
		for (String str : titles) {
			RichTextString text = cHelp.createRichTextString(str);
			Cell cell = titleRow.createCell(index);
			cell.setCellValue(text);
			index++;
		}
		ExcelUtil.setSpecialValueToExcel(1,listData, sheet, cHelp);
		
//		listData = (List<Object>) model.get("listData");
//		titles = (String[]) model.get("titles");
//		Class clazz = (Class)model.get("class");
	}

}
